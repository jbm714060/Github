package com.bmj.greader.ui.module.repo;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.OvershootInterpolator;
import android.widget.AbsListView;

import com.bmj.greader.presenter.base.OnLoadMoreListener;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.github.clans.fab.FloatingActionMenu;
import com.mikepenz.octicons_typeface_library.Octicons;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.bmj.greader.common.wrapper.AppLog;
import com.bmj.greader.dagger2.component.MainComponent;
import com.bmj.greader.data.api.RepoApi;
import com.bmj.greader.data.model.Repo;
import com.bmj.greader.presenter.main.MostStarPresenter;
import com.bmj.greader.ui.base.BaseFragment;
import com.bmj.greader.ui.module.repo.adapter.RepoListRecyclerAdapter;
import com.bmj.mvp.lce.LceView;
import com.bmj.greader.R;

/**
 * Created by Administrator on 2016/11/16 0016.
 */
public class MostStarFragment extends BaseFragment implements LceView<ArrayList<Repo>>{

    @BindView(R.id.repo_list)
    RecyclerView mRepoListView;
    @BindView(R.id.menu)
    FloatingActionMenu mFloatMenu;
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout mRefreshLayout;

    private RepoListRecyclerAdapter mAdapter;
    private View emptyView;
    private View loadingView;

    @Inject
    MostStarPresenter mPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getComponent(MainComponent.class).inject(this);
        mPresenter.attachView(this);
        mPresenter.setOnLoadMoreListener(mOnLoadMoreListener);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter.loadMostStars(mCurrentType);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_most_star,container,false);
        emptyView = inflater.inflate(R.layout.trending_empy_view,container,false);
        loadingView = inflater.inflate(R.layout.view_load_more,container,false);
        ButterKnife.bind(this,view);
        initViews();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mFloatMenu.hideMenu(false);
        mFloatMenu.setMenuButtonHideAnimation(AnimationUtils.loadAnimation(getActivity(),
                R.anim.hide_to_bottom));
        mFloatMenu.setMenuButtonShowAnimation(AnimationUtils.loadAnimation(getActivity(),
                R.anim.show_from_bottom));

        mFloatMenu.postDelayed(new Runnable() {
            @Override
            public void run() {
                mFloatMenu.showMenu(true);
            }
        },300);

        mRepoListView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(dy > 0){
                    mFloatMenu.hideMenu(true);
                }
                else if(dy < 0) {
                    mFloatMenu.showMenu(true);
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    private void initViews(){
        createCustomAnimation();

        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                AppLog.d("onRefresh, mCurrentType:" + mCurrentType);
                mPresenter.loadMostStars(mCurrentType);
            }
        });

        mAdapter = new RepoListRecyclerAdapter(null);
        mAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
                Repo repo = mAdapter.getItem(i);
                RepoDetailActivity.launch(getActivity(),repo.getOwner().getLogin(),repo.getName(),
                        view.findViewById(R.id.item_repo_name));
            }
        });
        mAdapter.setEmptyView(emptyView);
        mAdapter.setLoadingView(loadingView);
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                mPresenter.loadMore();
            }
        });

        mRepoListView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        mRepoListView.setAdapter(mAdapter);
        mRepoListView.addItemDecoration(
                new HorizontalDividerItemDecoration
                        .Builder(getActivity())
                        .color(Color.TRANSPARENT)
                        //.size(getResources().getDimensionPixelSize(R.dimen.divider_height))
                        .sizeResId(R.dimen.divider_height)
                        .build());
    }

    @Override
    public void onResume() {
        super.onResume();
       // SetActivityTitle();
    }

    @Override
    public void showLoading() {
        mRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mRefreshLayout.setRefreshing(true);
            }
        });
    }

    @Override
    public void dismissLoading() {
        mRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showContent(ArrayList<Repo> data) {
        AppLog.d("data:" + data);
        if(data!=null) {
            mAdapter.setNewData(data);
            mAdapter.openLoadMore(mPresenter.getPageSize(),true);
        }
    }

    @Override
    public void showError(Throwable e) {
        AppLog.e(e);
    }

    @Override
    public void showEmpty() {

    }

    private int mCurrentType = RepoApi.TYPE_ANDROID;
    @OnClick({R.id.repo_android,R.id.repo_ios,R.id.repo_php,R.id.repo_web,R.id.repo_python})
    public void onClick(View view){
        mFloatMenu.close(true);
        switch (view.getId()) {
            case R.id.repo_android:
                mCurrentType = RepoApi.TYPE_ANDROID;
                break;
            case R.id.repo_ios:
                mCurrentType = RepoApi.TYPE_IOS;
                break;
            case R.id.repo_php:
                mCurrentType= RepoApi.TYPE_PHP;
                break;
            case R.id.repo_web:
                mCurrentType = RepoApi.TYPE_WEB;
                break;
            case R.id.repo_python:
                mCurrentType = RepoApi.TYPE_PYTHON;
                break;
        }
        //SetActivityTitle();
        mPresenter.loadMostStars(mCurrentType);
    }

    private OnLoadMoreListener<ArrayList<Repo>> mOnLoadMoreListener =
        new OnLoadMoreListener<ArrayList<Repo>>() {
            @Override
            public void onStart() {}

            @Override
            public void onEnd() {}

            @Override
            public void onSuccess(ArrayList<Repo> repos) {
                mAdapter.notifyDataChangedAfterLoadMore(repos,true);
                if(repos.size() < mPresenter.getPageSize())
                    mAdapter.openLoadMore(false);
            }

            @Override
            public void onError(Throwable e) {
                Snackbar.make(mRepoListView,e.getMessage(),Snackbar.LENGTH_LONG).show();
                mAdapter.openLoadMore(false);
            }
        };

    private void createCustomAnimation() {
        AnimatorSet set = new AnimatorSet();

        ObjectAnimator scaleOutX = ObjectAnimator.ofFloat(mFloatMenu.getMenuIconView(), "scaleX", 1.0f, 0.2f);
        ObjectAnimator scaleOutY = ObjectAnimator.ofFloat(mFloatMenu.getMenuIconView(), "scaleY", 1.0f, 0.2f);

        ObjectAnimator scaleInX = ObjectAnimator.ofFloat(mFloatMenu.getMenuIconView(), "scaleX", 0.2f, 1.0f);
        ObjectAnimator scaleInY = ObjectAnimator.ofFloat(mFloatMenu.getMenuIconView(), "scaleY", 0.2f, 1.0f);

        scaleOutX.setDuration(50);
        scaleOutY.setDuration(50);

        scaleInX.setDuration(150);
        scaleInY.setDuration(150);

        scaleInX.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                mFloatMenu.getMenuIconView().setImageResource(mFloatMenu.isOpened()
                        ? R.drawable.ic_fab_menu_white : R.drawable.ic_fab_close);
            }
        });

        set.play(scaleOutX).with(scaleOutY);
        set.play(scaleInX).with(scaleInY).after(scaleOutX);
        set.setInterpolator(new OvershootInterpolator(2));

        mFloatMenu.setIconToggleAnimatorSet(set);
    }

    private void SetActivityTitle(){
        String label;
        switch(mCurrentType){
            case RepoApi.TYPE_ANDROID:
                label = "Android";
                break;
            case RepoApi.TYPE_IOS:
                label = "IOS";
                break;
            case RepoApi.TYPE_WEB:
                label = "Web";
                break;
            case RepoApi.TYPE_PYTHON:
                label = "Python";
                break;
            case RepoApi.TYPE_PHP:
                label = "PHP";
                break;
            default:
                label = "unknown";
        }
        getActivity().setTitle(getResources().getString(R.string.tabs_most_stars,label));
    }
}
