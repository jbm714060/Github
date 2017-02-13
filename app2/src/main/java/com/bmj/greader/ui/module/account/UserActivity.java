package com.bmj.greader.ui.module.account;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.bmj.greader.MyApplication;
import com.bmj.greader.R;
import com.bmj.greader.common.wrapper.AppLog;
import com.bmj.greader.dagger2.HasComponent;
import com.bmj.greader.dagger2.component.DaggerRepoComponent;
import com.bmj.greader.dagger2.component.RepoComponent;
import com.bmj.greader.dagger2.module.ActivityModule;
import com.bmj.greader.dagger2.module.RepoModule;
import com.bmj.greader.data.model.User;
import com.bmj.greader.data.model.UserDetail;
import com.bmj.greader.presenter.repo.UserDetailPresenter;
import com.bmj.greader.ui.base.BaseLoadingActivity;
import com.bmj.greader.ui.module.main.UserDetailActivity;
import com.bmj.greader.ui.module.repo.RepoListActivity;
import com.bmj.greader.ui.module.repo.UserListActivity;
import com.bmj.greader.ui.widget.UserCard;
import com.bmj.mvp.lce.LceView;

/**
 * Created by Administrator on 2016/11/15 0015.
 */
public class UserActivity extends BaseLoadingActivity implements LceView<UserDetail>,HasComponent<RepoComponent>{

    @BindView(R.id.user_card)
    UserCard mUserCard;

    @BindView(R.id.repository_count)
    TextView mRepoCount;
    @BindView(R.id.followers_count)
    TextView mFollowerCount;
    @BindView(R.id.following_count)
    TextView mFollowingCount;
    @BindView(R.id.starred_repo_count)
    TextView mStarredRepoCount;

    @Inject
    UserDetailPresenter mPresenter;

    private  String mUsername;

    private static final String EXTRA_USER_NAME = "extra_user_name";
    private static final String EXTRA_USER = "extra_user";

    public static void launch(Context context,String username){
        Intent intent = new Intent(context,UserDetailActivity.class);
        //intent.putExtra(EXTRA_USER_NAME,username);userlogin
        intent.putExtra("userlogin",username);
        context.startActivity(intent);
    }

    public static void launch(Context context,User user){
        Intent intent = new Intent(context,UserDetailActivity.class);
        //intent.putExtra(EXTRA_USER_NAME,user.getLogin());
        intent.putExtra("userlogin",user.getLogin());
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getComponent().inject(this);
        setContentView(R.layout.activity_user);
        ButterKnife.bind(this);
        mPresenter.attachView(this);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initViews();
        loadUser();
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

    private void initViews(){

    }

    private void loadUser(){
        mUsername = getIntent().getStringExtra(EXTRA_USER_NAME);
        if(!TextUtils.isEmpty(mUsername)){
            setTitle(mUsername);
            mPresenter.getUserDetail(mUsername,false);
        }
    }

    @Override
    public String getLoadingMessage() {
        return "Loading";
    }

    @OnClick({R.id.repo_layout,R.id.starred_layout,R.id.followers_layout,R.id.following_layout})
    protected void OnClick(View view){
        switch (view.getId()){
            case R.id.repo_layout:
                RepoListActivity.launchToShowRepos(this, mUsername);
                break;
            case R.id.starred_layout:
                RepoListActivity.launchToShowStars(this, mUsername);
                break;
            case R.id.following_layout:
                UserListActivity.launchToShowFollowing(this, mUsername);
                break;
            case R.id.followers_layout:
                UserListActivity.launchToShowFollowers(this, mUsername);
                break;
        }
    }

    private void showView(View doView){
        if(doView.getVisibility() == View.VISIBLE) {
            Animation hideAnim = AnimationUtils.loadAnimation(this,android.R.anim.slide_out_right);
            doView.setAnimation(hideAnim);
            doView.setVisibility(View.GONE);
        } else {
            Animation showAnim =AnimationUtils.loadAnimation(this,android.R.anim.slide_in_left);
            doView.setAnimation(showAnim);
            doView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public RepoComponent getComponent() {
        return DaggerRepoComponent.builder()
                .applicationComponent(MyApplication.get(this).getComponent())
                .activityModule(new ActivityModule(this))
                .repoModule(new RepoModule())
                .build();
    }

    @Override
    public void showContent(UserDetail data) {
        mUserCard.setUser(data.getBaseUser());
        mFollowerCount.setText(getResources().getString(R.string.act_user_followers_count,
                data.getBaseUser().getFollowers()));
        mFollowingCount.setText(getResources().getString(R.string.act_user_following_count,
                data.getBaseUser().getFollowing()));
        mRepoCount.setText(getResources().getString(R.string.act_user_repository,
                data.getSelfRepoList().size()));
        mStarredRepoCount.setText(getResources().getString(R.string.act_user_repository,
                data.getStarredRepoList().size()));
    }

    @Override
    public void showError(Throwable e) {
        AppLog.e(e);
    }

    @Override
    public void showEmpty() {

    }
}
