package com.bmj.greader.ui.module.repo;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.utils.ScreenUtils;
import com.bmj.greader.data.model.MultiItem;
import com.bmj.greader.data.model.User;
import com.bmj.greader.data.net.response.SearchRepoResp;
import com.bmj.greader.data.net.response.SearchUserResp;
import com.bmj.greader.ui.module.compz.TextPopup;
import com.bmj.greader.ui.module.main.UserDetailActivity;
import com.bmj.greader.ui.module.repo.adapter.SearchUserRecyclerAdapter;
import com.bmj.greader.ui.module.repo.adapter.SearchUserRecyclerAdapter2;
import com.bmj.greader.ui.widget.AdvancedSearchView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.bmj.greader.MyApplication;
import com.bmj.greader.R;
import com.bmj.greader.common.util.InputMethodUtil;
import com.bmj.greader.dagger2.HasComponent;
import com.bmj.greader.dagger2.component.DaggerRepoComponent;
import com.bmj.greader.dagger2.component.RepoComponent;
import com.bmj.greader.dagger2.module.ActivityModule;
import com.bmj.greader.dagger2.module.RepoModule;
import com.bmj.greader.data.model.Repo;
import com.bmj.greader.presenter.repo.SearchPresenter;
import com.bmj.greader.ui.base.BaseLoadingActivity;
import com.bmj.greader.ui.module.repo.adapter.RepoListRecyclerAdapter;
import com.bmj.greader.ui.module.repo.view.SearchView;

/**
 * Created by Administrator on 2016/11/16 0016.
 */
public class SearchActivity extends BaseLoadingActivity implements SearchView,HasComponent<RepoComponent>{
    @BindView(R.id.as_root_layout)
    LinearLayout mRootLayout;

    @BindView(R.id.repo_list)
    RecyclerView mRepoListView;
    @BindView(R.id.user_list)
    RecyclerView mUserList;

    @BindView(R.id.advanced_view)
    AdvancedSearchView mAdvancedView;
    @BindView(R.id.as_toolbar_back)
    ImageButton mToolbarBack;
    @BindView(R.id.as_search_content)
    EditText mSearchEdit;
    @BindView(R.id.as_content_clear)
    ImageButton mContentClear;
    @BindView(R.id.as_toolbar_search)
    TextView mSearchButton;
    @BindView(R.id.search_result_count)
    TextView mSearchCountText;

    @Inject
    SearchPresenter mPresenter;

    private RepoListRecyclerAdapter mAdpter;
    private SearchUserRecyclerAdapter2 mUserAdapter;

    public static void launch(Context context){
        context.startActivity(new Intent(context,SearchActivity.class));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        getComponent().inject(this);
        mPresenter.attachView(this);
        initViews();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    @Override
    public String getLoadingMessage() {
        return getString(R.string.load_searching);
    }

    private void initViews(){
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mSearchCountText.setTag(new TextCountHolder(mSearchCountText));

        View mLoadingView = getLayoutInflater().inflate(R.layout.view_load_more,mRootLayout,false);

        mAdpter = new RepoListRecyclerAdapter(null);
        mAdpter.setOnRecyclerViewItemClickListener(mItemClickListener);
        mAdpter.setOnLoadMoreListener(loadMoreRepoListener);
        mAdpter.setLoadingView(mLoadingView);
        mRepoListView.setLayoutManager(new LinearLayoutManager(this));
        mRepoListView.addItemDecoration(new HorizontalDividerItemDecoration
                .Builder(this)
                .size(getResources().getDimensionPixelSize(R.dimen.divider_height))
                .color(Color.TRANSPARENT)
                .build()
        );
        mRepoListView.setAdapter(mAdpter);

        //mUserAdapter = new SearchUserRecyclerAdapter(null);
        mUserAdapter = new SearchUserRecyclerAdapter2(null,this);
        mUserAdapter.setOnLoadMoreListener(loadMoreUserListener);
        mUserAdapter.setLoadingView(mLoadingView);
        mUserAdapter.setOnRecyclerViewItemClickListener(mUserItemClickListener);
        mUserList.setLayoutManager(new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL));
        mUserList.setAdapter(mUserAdapter);

        autoSoftInput();
        mSearchEdit.addTextChangedListener(mWatcher);
    }

    BaseQuickAdapter.RequestLoadMoreListener loadMoreUserListener = new BaseQuickAdapter.RequestLoadMoreListener() {
        @Override
        public void onLoadMoreRequested() {
            mPresenter.loadMoreUsers();
        }
    };

    BaseQuickAdapter.RequestLoadMoreListener loadMoreRepoListener = new BaseQuickAdapter.RequestLoadMoreListener() {
        @Override
        public void onLoadMoreRequested() {
            mPresenter.loadMoreRepos();
        }
    };

    private void autoSoftInput(){
        mSearchEdit.setFocusable(true);
        mSearchEdit.setFocusableInTouchMode(true);
        mSearchEdit.requestFocus();

        Timer timer = new Timer();
        timer.schedule(new TimerTask() { //让软键盘延时弹出，以更好的加载Activity
            public void run() {
                InputMethodManager inputManager =
                        (InputMethodManager)mSearchEdit.getContext().
                                getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(mSearchEdit, 0);
            }
        }, 800);
    }

    private TextWatcher mWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if(TextUtils.isEmpty(editable.toString()))
                mContentClear.setVisibility(View.GONE);
            else
                mContentClear.setVisibility(View.VISIBLE);
        }
    };

    /*@Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int[] location = new int[2];
        mSearchEdit.getLocationOnScreen(location);
        RectF rect = new RectF(location[0], location[1], location[0] + mSearchEdit.getWidth(),
                location[1] + mSearchEdit.getHeight());
        float x = ev.getRawX(); // 获取相对于屏幕左上角的 x 坐标值
        float y = ev.getRawY(); // 获取相对于屏幕左上角的 y 坐标值
        boolean isInViewRect = rect.contains(x, y);
        if(!isInViewRect){
            mSearchEdit.setCursorVisible(false);
        }else{
            mSearchEdit.setCursorVisible(true);
        }
        return super.dispatchTouchEvent(ev);
    }*/

    @OnClick({R.id.as_toolbar_back,R.id.as_content_clear,R.id.as_toolbar_search,R.id.as_search_content})
    protected void onClick(View view){
        switch (view.getId()){
            case R.id.as_toolbar_back:
                onBackPressed();
                break;
            case R.id.as_content_clear:
                mSearchEdit.setText("");
                mContentClear.setVisibility(View.GONE);
                break;
            case R.id.as_toolbar_search:
                ready2RepoListView();

                HashMap<String,String> searchOp = mAdvancedView.getAdvancedOptions();
                searchOp.put("key",mSearchEdit.getText().toString());
                mPresenter.search(searchOp);

                if(searchOp.get("type").equals("Users")) {
                    mUserList.setVisibility(View.VISIBLE);
                    mRepoListView.setVisibility(View.GONE);
                }else{
                    mUserList.setVisibility(View.GONE);
                    mRepoListView.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.as_search_content:
                ready2AdvancedView();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if(mAdvancedView.getVisibility() == View.VISIBLE){
            ready2RepoListView();
            mSearchCountText.setVisibility(View.VISIBLE);
        }else{
            super.onBackPressed();
        }
    }

    private void ready2AdvancedView(){
        mAdvancedView.SetVisibility(View.VISIBLE);
        mRepoListView.setVisibility(View.GONE);
        mUserList.setVisibility(View.GONE);
        mSearchCountText.setVisibility(View.GONE);
        if(!TextUtils.isEmpty(mSearchEdit.getText()))
            mContentClear.setVisibility(View.VISIBLE);
        else
            mContentClear.setVisibility(View.GONE);
        mSearchEdit.setCursorVisible(true);
    }

    private void ready2RepoListView(){
        mSearchEdit.setCursorVisible(false);
        InputMethodUtil.hideSoftInput(this);
        mAdvancedView.SetVisibility(View.GONE);
        mRepoListView.setVisibility(View.VISIBLE);
        mContentClear.setVisibility(View.GONE);
    }

    @Override
    public RepoComponent getComponent() {
        return DaggerRepoComponent.builder()
                .applicationComponent(MyApplication.get(this).getComponent())
                .repoModule(new RepoModule())
                .activityModule(new ActivityModule(this))
                .build();
    }

    @Override
    public void showSearchRepo(SearchRepoResp result,int page) {
        if(result == null || result.getItems()==null) {
            mAdpter.setNewData(new ArrayList<Repo>());
            ((TextCountHolder)mSearchCountText.getTag()).hide();
        }
        else {
            if(page == 1) {
                mAdpter.setNewData(result.getItems());
                showAnim(result.getItems().size(), result.getTotal_count(),page);
                if(mPresenter.getPage() > 1)
                    mAdpter.openLoadMore(mPresenter.getPageSize(),true);
            }
            else if(page > 1) {
                mAdpter.notifyDataChangedAfterLoadMore(result.getItems(),true);
                showAnim(result.getItems().size(), result.getTotal_count(),page);
                if(mPresenter.getPage() <= 1)
                    mAdpter.openLoadMore(false);
            }
        }
    }

    @Override
    public void showSearchUser(SearchUserResp result,int page) {
        if(result == null || result.getItems()==null) {
            //mUserAdapter.setNewData(new ArrayList<MultiItem>());
            mUserAdapter.setNewData(new ArrayList<User>());
            ((TextCountHolder)mSearchCountText.getTag()).hide();
        }
        else {
            if(page == 1) {
                //mUserAdapter.setNewData(MultiItem.parse(result.getItems()));
                mUserAdapter.setNewData(result.getItems());
                showAnim(result.getItems().size(), result.getTotal_count(),page);
                if(mPresenter.getPage() > 1)
                    mUserAdapter.openLoadMore(mPresenter.getPageSize(),true);
            }
            else if(page > 1) {
                //mUserAdapter.notifyDataChangedAfterLoadMore(MultiItem.parse(result.getItems()),true);
                mUserAdapter.notifyDataChangedAfterLoadMore(result.getItems(),true);
                showAnim(result.getItems().size(), result.getTotal_count(),page);
                if(mPresenter.getPage() <= 1)
                    mUserAdapter.openLoadMore(false);
            }
        }
    }

    private void showAnim(int listSize,long dataCount,int page){
        TextPopup textPopup;

        if(page >= 1) {
            if(page == 1) {
                textPopup = new TextPopup(this,String.valueOf(dataCount));
                ((TextCountHolder) mSearchCountText.getTag()).reset(listSize, dataCount);
            }
            else{
                String showStr = "+" + listSize;
                textPopup = new TextPopup(this,showStr);
                ((TextCountHolder) mSearchCountText.getTag()).refresh(listSize);
            }
            textPopup.getPopupWindow().setTouchable(false);
            textPopup.showPopupWindow();
        }
    }

    private BaseQuickAdapter.OnRecyclerViewItemClickListener mItemClickListener =
            new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
                @Override
                public void onItemClick(View view, int i) {
                    Repo repo = mAdpter.getItem(i);
                    RepoDetailActivity.launch(SearchActivity.this,repo.getOwner().getLogin(),repo.getName(),
                            view.findViewById(R.id.item_repo_name));
                }
            };

    private BaseQuickAdapter.OnRecyclerViewItemClickListener mUserItemClickListener =
            new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
        @Override
        public void onItemClick(View view, int i) {
           // MultiItem item = (MultiItem) mUserAdapter.getItem(i);
            User item =  mUserAdapter.getItem(i);
            UserDetailActivity.launch(SearchActivity.this,item.getLogin());
        }
    };

    private class TextCountHolder{
        private TextView mText;
        public long mCount;
        public int mGetted;

        public TextCountHolder(TextView text){
            mText = text;
        }

        public void refresh(final int getted){
            mText.setVisibility(View.VISIBLE);
            mText.postDelayed(new Runnable() {
                @Override
                public void run() {
                    showText(mGetted,mGetted+=getted);
                }
            },300);
        }

        public void reset(final int getted,long count){
            mGetted = 0;
            mCount = count;
            mText.setText(String.valueOf(mCount));
            mText.setVisibility(View.VISIBLE);
            mText.postDelayed(new Runnable() {
                @Override
                public void run() {
                    showText(mGetted,mGetted+=getted);
                }
            },300);
        }

        private void showText(int start,int end){
            ValueAnimator animator = ValueAnimator.ofInt(start,end);
            animator.setDuration(500);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    int value = (int)valueAnimator.getAnimatedValue();
                    String text = value + "/"+mCount;
                    mText.setText(text);
                }
            });
            animator.start();
        }

        public void hide(){
            mText.setVisibility(View.GONE);
        }
    }
}
