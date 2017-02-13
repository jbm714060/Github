package com.bmj.greader.ui.module.repo;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.transition.Fade;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.utils.ToastUtils;
import com.bmj.greader.common.util.StringUtil;
import com.bmj.greader.common.util.TransitionHelper;
import com.bmj.greader.common.wrapper.CustomSnackbar;
import com.bmj.greader.data.net.response.Content;
import com.bmj.greader.ui.base.SimpleTransitionListener;
import com.bmj.greader.ui.module.main.UserDetailActivity;
import com.chad.library.adapter.base.BaseQuickAdapter;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.bmj.greader.MyApplication;
import com.bmj.greader.R;
import com.bmj.greader.dagger2.HasComponent;
import com.bmj.greader.dagger2.component.DaggerRepoComponent;
import com.bmj.greader.dagger2.component.RepoComponent;
import com.bmj.greader.dagger2.module.ActivityModule;
import com.bmj.greader.dagger2.module.RepoModule;
import com.bmj.greader.data.model.Repo;
import com.bmj.greader.data.model.User;
import com.bmj.greader.data.pref.AccountPref;
import com.bmj.greader.presenter.repo.RepoDetailPresenter;
import com.bmj.greader.presenter.repo.StarActionPresenter;
import com.bmj.greader.ui.base.BaseLoadingActivity;
import com.bmj.greader.ui.module.repo.adapter.ContributorListAdapter;
import com.bmj.greader.ui.module.repo.adapter.ForkUserListAdapter;
import com.bmj.greader.ui.module.repo.view.RepoDetailView;
import com.bmj.greader.ui.widget.RepoItemView;
import com.mukesh.MarkdownView;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/11/18 0018.
 */
public class RepoDetailActivity extends BaseLoadingActivity implements RepoDetailView,HasComponent<RepoComponent>{
    @BindView(R.id.forks_count)
    TextView mForksCount;
    @BindView(R.id.contributors_count)
    TextView mContributorsCount;
    @BindView(R.id.fork_list)
    RecyclerView mForkListView;
    @BindView(R.id.contributor_list)
    RecyclerView mContributorListView;
    @BindView(R.id.repo_item_view)
    RepoItemView mRepoItemView;
    @BindView(R.id.contributor_layout)
    LinearLayout mContributorLayout;
    @BindView(R.id.fork_layout)
    LinearLayout mForkLayout;
    @BindView(R.id.root_layout)
    LinearLayout rootLayout;
    @BindView(R.id.readme_content)
    MarkdownView mReadmeView;
    @BindView(R.id.issues_layout)
    LinearLayout mIssueLayout;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.repo_detail_title)
    TextView mTitleView;

    @Inject
    RepoDetailPresenter mRepoDetailPresenter;
    @Inject
    StarActionPresenter mStarActionPresenter;

    private ForkUserListAdapter mForkUserAdapter;
    private ContributorListAdapter mContributorAdapter;

    private static final String EXTRA_OWNER = "extra_owner";
    private static final String EXTRA_REPO_NAME = "extra_repo_name";

    private String mOwner;
    private String mRepoName;
    private Repo mRepo;

    public static void launch(Context context,String ownerLogin,String repoName,View... shareElements){
        Intent intent = new Intent(context,RepoDetailActivity.class);
        intent.putExtra(EXTRA_OWNER,ownerLogin);
        intent.putExtra(EXTRA_REPO_NAME,repoName);

        ActivityOptionsCompat optionsCompat = null;
        if(shareElements.length > 0){
            Pair<View,String>[] pairs = TransitionHelper.createSafeTransitionParticipants((Activity)context,false,false,
                    new Pair<View,String>(shareElements[0],context.getString(R.string.transition_repodetail_title)));
            optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity)context,pairs);
        }
        if(optionsCompat != null)
            context.startActivity(intent,optionsCompat.toBundle());
        else
            context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getComponent().inject(this);
        setContentView(R.layout.activity_repo_detail);

        mOwner = getIntent().getStringExtra(EXTRA_OWNER);
        mRepoName= getIntent().getStringExtra(EXTRA_REPO_NAME);

        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mTitleView.setText(mRepoName);

        initViews();

        mRepoDetailPresenter.attachView(this);
        mStarActionPresenter.attachView(this);

        setWindownAnimations();
    }

    private void setWindownAnimations(){
        getWindow().setAllowEnterTransitionOverlap(true);
        getWindow().setAllowReturnTransitionOverlap(true);
        getWindow().getSharedElementEnterTransition().setDuration(500);
        getWindow().getSharedElementExitTransition().setDuration(500);

        Transition transition = TransitionInflater.from(this).inflateTransition(R.transition.changecolor);
        getWindow().setEnterTransition(transition);
        transition.addListener(new SimpleTransitionListener(){
            @Override
            public void onTransitionEnd(Transition transition) {
                super.onTransitionEnd(transition);
                transition.removeListener(this);
                int x = (rootLayout.getLeft()+rootLayout.getRight())/2;
                Animator animator = ViewAnimationUtils.createCircularReveal(rootLayout,x,0,0,rootLayout.getHeight());
                animator.setDuration(500);
                animator.setInterpolator(new AccelerateDecelerateInterpolator());
                animator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        loadDetailData();
                    }
                });
                animator.start();
            }
        });

        Fade fade = new Fade();
        getWindow().setReturnTransition(fade);
        fade.setDuration(100);
        fade.setStartDelay(400);
        fade.addListener(new SimpleTransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {
                transition.removeListener(this);
                int cx = (rootLayout.getLeft() + rootLayout.getRight()) / 2;
                int cy = (rootLayout.getTop() + rootLayout.getBottom()) / 2;
                int initialRadius = rootLayout.getHeight();

                Animator anim = ViewAnimationUtils.createCircularReveal(rootLayout, cx, cy, initialRadius, 0);
                anim.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        rootLayout.setVisibility(View.INVISIBLE);
                    }
                });
                anim.setDuration(400);
                anim.start();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRepoDetailPresenter.detachView();
        mStarActionPresenter.detachView();
    }

    @Override
    public String getLoadingMessage() {
        return null;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initViews(){
        mForkListView.setLayoutManager(
                new GridLayoutManager(this,1, LinearLayoutManager.HORIZONTAL,false));
        mForkUserAdapter = new ForkUserListAdapter(this,null);
        mForkUserAdapter.setOnRecyclerViewItemClickListener(
                new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
                    @Override
                    public void onItemClick(View view, int i) {
                        Repo repo = mForkUserAdapter.getItem(i);
                        UserDetailActivity.launch(view.getContext(),repo.getOwner().getLogin());
                    }
                }
        );
        mForkListView.setAdapter(mForkUserAdapter);

        mContributorListView.setLayoutManager(
                new GridLayoutManager(this,1,LinearLayoutManager.HORIZONTAL,false));
        mContributorAdapter = new ContributorListAdapter(this,null);
        mContributorAdapter.setOnRecyclerViewItemClickListener(
                new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
                    @Override
                    public void onItemClick(View view, int i) {
                        User user = mContributorAdapter.getItem(i);
                        UserDetailActivity.launch(RepoDetailActivity.this,user.getLogin());
                    }
                }
        );
        mContributorListView.setAdapter(mContributorAdapter);
        mReadmeView.setOpenUrlInBrowser(true);
    }

    private void loadDetailData(){
        Intent intent = getIntent();
        if(intent != null){
            if(!TextUtils.isEmpty(mOwner) && !TextUtils.isEmpty(mRepoName)){
                mRepoDetailPresenter.loadRepo(mOwner,mRepoName);
            }
        }
    }

    @Override
    public void showRepo(Repo repo){
        mRepo = repo;
        mOwner = mRepo.getOwner().getLogin();
        mRepoName = mRepo.getName();

        setTitle(mRepoName);

        mRepoItemView.setRepo(mRepo);
        mRepoItemView.setRepoActionListener(mRepoActionListener);

        mIssueLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void showRepoContributors(ArrayList<User> contributors){
        if(contributors != null) {
            mContributorsCount.setText(getResources().getString(R.string.contributors_count,
                    contributors.size()));
            mContributorAdapter.setNewData(contributors);
        }
    }

    @Override
    public void showRepoForks(ArrayList<Repo> forks){
        int forksSize = mRepo.getForks_count();
        if(forksSize == 0 || forks == null){
            mForkLayout.setVisibility(View.GONE);
        }else{
            mForkLayout.setVisibility(View.VISIBLE);
            mForksCount.setText(getResources().getString(R.string.forks_count,forksSize));
            mForkUserAdapter.setNewData(forks);
        }
    }

    @Override
    public void showReadMe(Content content){
        if(content != null) {
            mReadmeView.setMarkDownText(StringUtil.base64Decode(content.content));
        }
    }

    @Override
    public void showIsStarred(boolean isStarred){
        mRepoItemView.setStarIcon(isStarred);
    }

    @Override
    public RepoComponent getComponent() {
        return DaggerRepoComponent.builder()
                .applicationComponent(MyApplication.get(this).getComponent())
                .activityModule(new ActivityModule(this))
                .repoModule(new RepoModule())
                .build();
    }

    private RepoItemView.RepoActionListener mRepoActionListener = new RepoItemView.RepoActionListener() {
        @Override
        public void onStarAction(Repo repo) {
            if(AccountPref.checkLogon(RepoDetailActivity.this)){
                mStarActionPresenter.starRepo(repo.getOwner().getLogin(),repo.getName());
            }else{
                CustomSnackbar.make(rootLayout,"({oct_info} You must be signed in to star a repository",Snackbar.LENGTH_LONG)
                        .setTextColor(R.color.md_white_1000).show();
            }
        }

        @Override
        public void onUnstarAction(Repo repo) {
            if(AccountPref.checkLogon(RepoDetailActivity.this)){
                mStarActionPresenter.unstarRepo(repo.getOwner().getLogin(),repo.getName());
            }
        }

        @Override
        public void onUserAction(String username) {
            UserDetailActivity.launch(RepoDetailActivity.this,username);
        }
    };

    @Override
    public void starSuccess() {
        mRepoItemView.setStarIcon(true);
        ToastUtils.showShortToast(this,"Star Success");
    }

    @Override
    public void starFailed() {
        ToastUtils.showShortToast(this,"Star Failed");
    }

    @Override
    public void unstarSuccess() {
        mRepoItemView.setStarIcon(false);
        ToastUtils.showShortToast(this,"UnStar Success");
    }

    @Override
    public void unstarFailed() {
        ToastUtils.showShortToast(this,"UnStar Failed");
    }

    @OnClick({R.id.code_layout,R.id.issues_layout,R.id.readme_layout})
    public void onClick(View view){
        switch(view.getId()){
            case R.id.code_layout:
                RepoTreeActivity.launch(this, mOwner, mRepoName);
                break;
            case R.id.issues_layout:
                RepoIssueActivity.launch(this, mOwner, mRepoName,
                        mRepo.getOpen_issues_count());
                break;
            case R.id.readme_layout:
                ToastUtils.showShortToast(this,"README中有些截图无法正常显示，请到Code中查看");
                break;
        }
    }
}
