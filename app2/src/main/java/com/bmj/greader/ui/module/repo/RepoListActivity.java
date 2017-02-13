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
import com.bmj.greader.data.model.Repo;
import com.bmj.greader.data.pref.AccountPref;
import com.bmj.greader.presenter.repo.RepoListPresenter;
import com.bmj.greader.ui.base.BaseLoadingActivity;
import com.bmj.greader.ui.module.repo.adapter.RepoListRecyclerAdapter;
import com.bmj.mvp.lce.LceView;

/**
 * Created by Administrator on 2016/11/21 0021.
 */
public class RepoListActivity extends BaseLoadingActivity implements LceView<ArrayList<Repo>>,HasComponent<RepoComponent>{
    @BindView(R.id.repo_list)
    RecyclerView mRepoListView;

    @Inject
    RepoListPresenter mPresenter;

    private RepoListRecyclerAdapter mAdapter;
    private int page_size = 30;

    private static final String EXTRA_USER_NAME = "extra_user_name";
    private static final String ACTION_REPOS = "nine.november.com.ACTION_REPOS";
    private static final String ACTION_STARRED_REPOS = "nine.november.com.ACTION_STARRED_REPOS";

    public static void launchToShowRepos(Context context,String name){
        Intent intent = new Intent(context,RepoListActivity.class);
        intent.putExtra(EXTRA_USER_NAME,name);
        intent.setAction(ACTION_REPOS);
        context.startActivity(intent);
    }

    public static void launchToShowStars(Context context,String name){
        Intent intent = new Intent(context,RepoListActivity.class);
        intent.putExtra(EXTRA_USER_NAME,name);
        intent.setAction(ACTION_STARRED_REPOS);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repo_list);
        ButterKnife.bind(this);
        getComponent().inject(this);
        mPresenter.attachView(this);
        mPresenter.setOnLoadMoreListener(new OnLoadMoreListener<ArrayList<Repo>>() {
            @Override
            public void onStart() {}
            @Override
            public void onEnd() {}
            @Override
            public void onSuccess(ArrayList<Repo> repos) {
                if(repos == null || repos.size() < page_size)
                    mAdapter.openLoadMore(false);
                if(repos != null)
                    mAdapter.notifyDataChangedAfterLoadMore(repos, true);
            }

            @Override
            public void onError(Throwable e) {
                AppLog.e(e);
                mAdapter.openLoadMore(false);
            }
        });

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initViews();
        loadData();
    }

    private void loadData(){
        String username = getIntent().getStringExtra(EXTRA_USER_NAME);
        String action = getIntent().getAction();
        boolean isSelf = AccountPref.isSelf(this,username);

        if(action.equals(ACTION_REPOS)) {
            setTitle(isSelf?getString(R.string.my_repositories):getString(R.string.repositories,username));
            mPresenter.loadRepos(username, isSelf, RepoApi.OWNER_REPOS);
        }else if(action.equals(ACTION_STARRED_REPOS)){
            setTitle(isSelf?getString(R.string.my_stars):getString(R.string.your_stars,username));
            mPresenter.loadRepos(username, isSelf, RepoApi.STARRED_REPOS);
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

    public void initViews(){
        mAdapter = new RepoListRecyclerAdapter(null);
        mAdapter.setOnRecyclerViewItemClickListener(mItemClickListener);
        mAdapter.openLoadMore(page_size,true);
        mAdapter.setLoadingView(LayoutInflater.from(this).inflate(R.layout.view_load_more,null));
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                mPresenter.loadMoreRepos();
            }
        });

        mRepoListView.setLayoutManager(new LinearLayoutManager(this));
        mRepoListView.addItemDecoration(new HorizontalDividerItemDecoration
                .Builder(this)
                .color(Color.TRANSPARENT)
                .size(getResources().getDimensionPixelSize(R.dimen.divider_height))
                .build()
        );
        mRepoListView.setAdapter(mAdapter);
    }

    private BaseQuickAdapter.OnRecyclerViewItemClickListener mItemClickListener =
            new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
                @Override
                public void onItemClick(View view, int i) {
                    Repo repo = mAdapter.getItem(i);
                    RepoDetailActivity.launch(RepoListActivity.this,repo.getOwner().getLogin(),repo.getName());
                }
            };

    @Override
    public String getLoadingMessage() {
        return getString(R.string.loading);
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
    public void showContent(ArrayList<Repo> data) {
        mAdapter.setNewData(data);
    }

    @Override
    public void showError(Throwable e) {

    }

    @Override
    public void showEmpty() {

    }
}
