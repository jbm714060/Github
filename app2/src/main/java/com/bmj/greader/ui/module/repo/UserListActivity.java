package com.bmj.greader.ui.module.repo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import com.bmj.greader.common.wrapper.AppLog;
import com.bmj.greader.presenter.base.OnLoadMoreListener;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.bmj.greader.MyApplication;
import com.bmj.greader.R;
import com.bmj.greader.dagger2.HasComponent;
import com.bmj.greader.dagger2.component.DaggerRepoComponent;
import com.bmj.greader.dagger2.component.RepoComponent;
import com.bmj.greader.dagger2.module.ActivityModule;
import com.bmj.greader.dagger2.module.RepoModule;
import com.bmj.greader.data.api.RepoApi;
import com.bmj.greader.data.model.User;
import com.bmj.greader.data.pref.AccountPref;
import com.bmj.greader.presenter.repo.UserListPresenter;
import com.bmj.greader.ui.base.BaseLoadingActivity;
import com.bmj.greader.ui.module.account.UserActivity;
import com.bmj.greader.ui.module.repo.adapter.UserListRecyclerAdapter;
import com.bmj.mvp.lce.LceView;

/**
 * Created by Administrator on 2016/11/21 0021.
 */
public class UserListActivity extends BaseLoadingActivity implements
        LceView<ArrayList<User>>,HasComponent<RepoComponent>{

    @BindView(R.id.user_list)
    RecyclerView mUserListView;

    @Inject
    UserListPresenter mPresenter;

    private UserListRecyclerAdapter mAdapter;
    private int page_size = 30;

    private static final String EXTRA_USER_NAME = "extra_user_name";
    private static final String ACTION_FOLLOWING = "nine.november.com.ACTION_FOLLOWING";
    private static final String ACTION_FOLLOWERS = "nine.november.com.ACTION_FOLLOWERS";

    public static void launchToShowFollowers(Context context,String name){
        Intent intent = new Intent(context,UserListActivity.class);
        intent.putExtra(EXTRA_USER_NAME,name);
        intent.setAction(ACTION_FOLLOWERS);
        context.startActivity(intent);
    }

    public static void launchToShowFollowing(Context context,String name){
        Intent intent = new Intent(context,UserListActivity.class);
        intent.putExtra(EXTRA_USER_NAME,name);
        intent.setAction(ACTION_FOLLOWING);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        ButterKnife.bind(this);
        getComponent().inject(this);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mPresenter.attachView(this);
        mPresenter.setOnLoadMoreListener(new OnLoadMoreListener<ArrayList<User>>() {
            @Override
            public void onStart() {}
            @Override
            public void onEnd() {}
            @Override
            public void onSuccess(ArrayList<User> users) {
                if(users == null || users.size() < page_size)
                    mAdapter.openLoadMore(false);
                if(users != null)
                    mAdapter.notifyDataChangedAfterLoadMore(users, true);
            }

            @Override
            public void onError(Throwable e) {
                AppLog.e(e);
                mAdapter.openLoadMore(false);
            }
        });

        initViews();
        loadData();
    }

    private void loadData(){
        String action = getIntent().getAction();
        String username = getIntent().getStringExtra(EXTRA_USER_NAME);
        boolean isSelf = AccountPref.isSelf(this,username);
        if(ACTION_FOLLOWERS.equals(action)){
            setTitle(isSelf?getString(R.string.my_followers):getString(R.string.followers,username));
            mPresenter.loadUsers(username,isSelf, RepoApi.FOLLOWER);
        }else if(ACTION_FOLLOWING.equals(action)){
            setTitle(isSelf?getString(R.string.my_following):getString(R.string.following,username));
            mPresenter.loadUsers(username,isSelf,RepoApi.FOLLOWING);
            getResources().getString(R.string.my_followers);
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

    private void initViews(){
        mAdapter = new UserListRecyclerAdapter(null);
        mAdapter.setOnRecyclerViewItemClickListener(mItemClickListener);
        mAdapter.openLoadMore(page_size,true);
        mAdapter.setLoadingView(LayoutInflater.from(this).inflate(R.layout.view_load_more,null));
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                mPresenter.loadMoreUsers();
            }
        });

        mUserListView.setLayoutManager(new LinearLayoutManager(this));
        mUserListView.addItemDecoration(new HorizontalDividerItemDecoration
                .Builder(this)
                .color(Color.GRAY)
                .size(1)
                .build()
        );
        mUserListView.setAdapter(mAdapter);
    }

    BaseQuickAdapter.OnRecyclerViewItemClickListener mItemClickListener =
            new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
                @Override
                public void onItemClick(View view, int i) {
                    User user = mAdapter.getItem(i);
                    UserActivity.launch(UserListActivity.this,user);
                }
            };

    @Override
    public RepoComponent getComponent() {
        return DaggerRepoComponent.builder()
                .applicationComponent(MyApplication.get(this).getComponent())
                .repoModule(new RepoModule())
                .activityModule(new ActivityModule(this))
                .build();
    }

    @Override
    public void showContent(ArrayList<User> data) {
        mAdapter.setNewData(data);
    }

    @Override
    public void showError(Throwable e) {

    }

    @Override
    public void showEmpty() {

    }

    @Override
    public String getLoadingMessage() {
        return getString(R.string.loading);
    }
}
