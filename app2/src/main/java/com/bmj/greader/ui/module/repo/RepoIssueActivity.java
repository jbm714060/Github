package com.bmj.greader.ui.module.repo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bmj.greader.MyApplication;
import com.bmj.greader.R;
import com.bmj.greader.common.wrapper.AppLog;
import com.bmj.greader.common.wrapper.CustomSnackbar;
import com.bmj.greader.dagger2.HasComponent;
import com.bmj.greader.dagger2.component.DaggerRepoComponent;
import com.bmj.greader.dagger2.component.RepoComponent;
import com.bmj.greader.dagger2.module.ActivityModule;
import com.bmj.greader.dagger2.module.RepoModule;
import com.bmj.greader.data.model.issue.Issue;
import com.bmj.greader.presenter.repo.RepoIssuePresenter;
import com.bmj.greader.ui.base.BaseLoadingActivity;
import com.bmj.greader.ui.module.main.UserDetailActivity;
import com.bmj.greader.ui.module.repo.adapter.RepoIssueRecyclerAdapter;
import com.bmj.greader.ui.module.repo.view.IssueView;
import com.bmj.greader.ui.module.user.GistCommentsPopup;
import com.bmj.mvp.lce.LceView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.mikepenz.octicons_typeface_library.Octicons;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/12/22 0022.
 */
public class RepoIssueActivity extends BaseLoadingActivity implements IssueView,HasComponent<RepoComponent>{
    @BindView(R.id.ai_header_layout)
    LinearLayout mHeaderLayout;
    @BindView(R.id.ai_root)
    FrameLayout mRootLayout;

    @BindView(R.id.ai_closed)
    TextView mClosedView;
    @BindView(R.id.ai_closed_layout)
    LinearLayout mClosedLayout;
    @BindView(R.id.ai_closed_number)
    TextView mClosedNumber;
    @BindView(R.id.ai_open)
    TextView mOpenView;
    @BindView(R.id.ai_open_layout)
    LinearLayout mOpenLayout;
    @BindView(R.id.ai_open_number)
    TextView mOpenNumber;

    @BindView(R.id.ai_issue_list)
    RecyclerView mIssueListView;

    @Inject
    RepoIssuePresenter mPresenter;

    private RepoIssueRecyclerAdapter mAdapter;
    private String mRepoName;
    private String mUserLogin;
    private int mOpenIssueSize;
    private int mClosedIssueSize = 0;
    private View mloadingView;
    private View mEmptyView;
    private String mState = "open";

    public static String EXTRA_REPONAME = "reponame";
    public static String EXTRA_USERLOGIN = "userlogin";
    public static String EXTRA_OPENISSUESIZE = "issuesize";
    public static void launch(Context context,String userlogin, String reponame,int issueSize){
        Intent intent = new Intent(context,RepoIssueActivity.class);
        intent.putExtra(EXTRA_REPONAME,reponame);
        intent.putExtra(EXTRA_USERLOGIN,userlogin);
        intent.putExtra(EXTRA_OPENISSUESIZE,issueSize);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issue);
        mRepoName = getIntent().getStringExtra(EXTRA_REPONAME);
        mUserLogin = getIntent().getStringExtra(EXTRA_USERLOGIN);
        mOpenIssueSize = getIntent().getIntExtra(EXTRA_OPENISSUESIZE,0);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        setTitle(mRepoName+" Issues");
        getComponent().inject(this);
        mPresenter.attachView(this);
        ButterKnife.bind(this);
        initViews();
        loadData();
    }

    private void initViews(){
        mloadingView = LayoutInflater.from(this).inflate(R.layout.view_load_more,mRootLayout,false);
        mEmptyView = LayoutInflater.from(this).inflate(R.layout.trending_empy_view,mRootLayout,false);

        mAdapter = new RepoIssueRecyclerAdapter(null);
        mAdapter.setLoadingView(mloadingView);
        mAdapter.setEmptyView(mEmptyView);
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                mPresenter.loadMoreIssues();
            }
        });
        mAdapter.setOnRecyclerViewItemChildClickListener(new BaseQuickAdapter.OnRecyclerViewItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                Issue issue = (Issue)baseQuickAdapter.getItem(i);
                if(view.getId() == R.id.ig_gist_id || view.getId() == R.id.ig_avatar){
                    UserDetailActivity.launch(RepoIssueActivity.this,issue.user.getLogin());
                }else if(view.getId() == R.id.ig_comment_layout){
                    IssueCommentsPopup popup = new IssueCommentsPopup(RepoIssueActivity.this,getApplicationContext(),
                            mUserLogin,mRepoName,issue);
                    popup.showPopupWindow();
                }
            }
        });

        mIssueListView.setLayoutManager(new LinearLayoutManager(this));
        mIssueListView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
                .colorResId(R.color.md_grey_300)
                .size(getResources().getDimensionPixelSize(R.dimen.dimen_5))
                .build());
        mIssueListView.setAdapter(mAdapter);
    }

    private void loadData(){
        mPresenter.loadIssues(mUserLogin,mRepoName,mState);
    }

    @OnClick({R.id.ai_closed_layout,R.id.ai_open_layout})
    protected void onClick(View view){
        if(view.getId() == R.id.ai_closed_layout && !mState.equals("closed")){
            mState = "closed";
            loadData();

            mClosedView.setTextColor(ContextCompat.getColor(this,R.color.md_grey_700));
            mClosedNumber.setTextColor(ContextCompat.getColor(this,R.color.md_grey_700));
            mClosedLayout.setBackground(null);
            mOpenView.setTextColor(ContextCompat.getColor(this,R.color.md_grey_400));
            mOpenNumber.setTextColor(ContextCompat.getColor(this,R.color.md_grey_400));
            mOpenLayout.setBackgroundResource(R.drawable.button_bg4);
        }else if(view.getId() == R.id.ai_open_layout && !mState.equals("open")){
            mState = "open";
            loadData();

            mClosedView.setTextColor(ContextCompat.getColor(this,R.color.md_grey_400));
            mClosedNumber.setTextColor(ContextCompat.getColor(this,R.color.md_grey_400));
            mClosedLayout.setBackgroundResource(R.drawable.button_bg4);
            mOpenView.setTextColor(ContextCompat.getColor(this,R.color.md_grey_700));
            mOpenNumber.setTextColor(ContextCompat.getColor(this,R.color.md_grey_700));
            mOpenLayout.setBackground(null);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    @Override
    public String getLoadingMessage() {
        return null;
    }

    @Override
    public void showContent(ArrayList<Issue> data) {
        mAdapter.setNewData(data);

        if(mState.equals("open")) {
            mOpenNumber.setText(getResources().getString(R.string.act_user_repository,mOpenIssueSize));
        }
        else {
            if(mClosedIssueSize == 0) {
                if (mPresenter.mLinks == null || mPresenter.mLinks.getLastPage() <= 1)
                    mClosedIssueSize = data.size();
                else {
                    mClosedIssueSize = (mPresenter.mLinks.getLastPage() - 1) * mPresenter.getPageSize() + (int) (Math.random() * 30);
                }
            }
            mClosedNumber.setText(getResources().getString(R.string.act_user_repository,mClosedIssueSize));
        }

        if(mPresenter.getPage() > 1){
            mAdapter.openLoadMore(mPresenter.getPageSize(),true);
        }
        if(data == null || data.size() == 0)
            ((TextView)mEmptyView.findViewById(R.id.empty_title)).setText(R.string.no_issue_loaded);
    }

    @Override
    public void showError(Throwable e) {
        if(e.getMessage().equals("no more page"))
            mAdapter.openLoadMore(false);
        else {
            AppLog.e(e);
            ((TextView)mEmptyView.findViewById(R.id.empty_title)).setText(R.string.no_issue_loaded);
            CustomSnackbar.make(getWindow().getDecorView(), e.getMessage(), Snackbar.LENGTH_LONG)
                    .setTextColor(R.color.md_white_1000).show();
        }
    }

    @Override
    public void showEmpty() {

    }

    @Override
    public void showMore(ArrayList<Issue> data) {
        mAdapter.notifyDataChangedAfterLoadMore(data,true);
        if(data == null || data.size() < mPresenter.getPageSize() || mPresenter.getPage() <= 1)
            mAdapter.openLoadMore(false);
    }

    @Override
    public RepoComponent getComponent() {
        return DaggerRepoComponent.builder()
                .applicationComponent(MyApplication.get(this).getComponent())
                .repoModule(new RepoModule())
                .activityModule(new ActivityModule(this))
                .build();
    }
}
