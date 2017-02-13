package com.bmj.greader.ui.module.user;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bmj.greader.R;
import com.bmj.greader.common.wrapper.AppLog;
import com.bmj.greader.common.wrapper.CustomSnackbar;
import com.bmj.greader.dagger2.component.RepoComponent;
import com.bmj.greader.data.api.RepoApi;
import com.bmj.greader.data.model.Repo;
import com.bmj.greader.data.pref.AccountPref;
import com.bmj.greader.presenter.base.OnLoadMoreListener;
import com.bmj.greader.presenter.repo.RepoListPresenter;
import com.bmj.greader.ui.base.BaseFragment;
import com.bmj.greader.ui.module.main.UserDetailActivity;
import com.bmj.greader.ui.module.repo.RepoDetailActivity;
import com.bmj.greader.ui.module.repo.adapter.RepoListRecyclerAdapter2;
import com.bmj.mvp.lce.LceView;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/12/13 0013.
 */
public class UserReposFragment extends BaseFragment implements LceView<ArrayList<Repo>>{
    @BindView(R.id.fo_popular_repolist)
    RecyclerView mPopularRepoList;
    @BindView(R.id.fo_repo_size)
    TextView mRepoSize;
    @BindView(R.id.fo_repo_typetag)
    TextView mRepoTypeTag;

    private View rootView;
    private View mloadingView;
    private View mEmptyView;
    private String mUserLogin;
    private int mRepoType;
    private RepoListRecyclerAdapter2 mRepoListAdapter2;
    private UserDetailActivity mActivity;

    @Inject
    RepoListPresenter mPresenter;

    private static String EXTRA_USER_LOGIN = "userLogin";
    private static String EXTRA_TYPE = "type";
    public static UserReposFragment instance(String userLogin,int repoType){
        UserReposFragment fragment = new UserReposFragment();
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_USER_LOGIN,userLogin);
        bundle.putInt(EXTRA_TYPE,repoType);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getComponent(RepoComponent.class).inject(this);

        mUserLogin = getArguments().getString(EXTRA_USER_LOGIN,"");
        mRepoType = getArguments().getInt(EXTRA_TYPE,RepoApi.OWNER_REPOS);

        mPresenter.attachView(this);
        mPresenter.setOnLoadMoreListener(mOnLoadMoreListener);
        mPresenter.setOnReposCountListener(new RepoListPresenter.OnReposCountListener() {
               @Override
               public void getReposCount(int reposCount) {
                   mRepoSize.setText(getResources().getString(R.string.act_user_repository,reposCount));
                   mActivity.setStarredReposCount(reposCount);
               }
           }
        );

        mActivity = (UserDetailActivity)getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_repos,container,false);
        mloadingView = LayoutInflater.from(getContext()).inflate(R.layout.view_load_more,container,false);
        mEmptyView = LayoutInflater.from(getContext()).inflate(R.layout.trending_empy_view,container,false);
        ButterKnife.bind(this,rootView);
        initViews();
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadData();
    }

    private void initViews(){
        if(mRepoType == RepoApi.OWNER_REPOS)
            mRepoTypeTag.setText(R.string.fragment_repositories);
        else if(mRepoType == RepoApi.STARRED_REPOS)
            mRepoTypeTag.setText(R.string.fragment_starredrepos);

        mRepoListAdapter2 = new RepoListRecyclerAdapter2(null);
        mRepoListAdapter2.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
                Repo repo = mRepoListAdapter2.getItem(i);
                RepoDetailActivity.launch(UserReposFragment.this.getContext(),
                        repo.getOwner().getLogin(),repo.getName(),view.findViewById(R.id.ir2_repo_name));
            }
        });
        mRepoListAdapter2.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                mPresenter.loadMoreRepos();
            }
        });
        mRepoListAdapter2.setLoadingView(mloadingView);
        ((TextView)mEmptyView.findViewById(R.id.empty_title)).setText(R.string.loading_repo);
        mRepoListAdapter2.setEmptyView(mEmptyView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mPopularRepoList.setLayoutManager(linearLayoutManager);
        mPopularRepoList.setAdapter(mRepoListAdapter2);
        /**
         * 解决NestedScrollView嵌套RecyclerView时，RecyclerView不自动滚动的问题
         * linearLayoutManager.setSmoothScrollbarEnabled(true);
         * linearLayoutManager.setAutoMeasureEnabled(true);
         * mPopularRepoList.setHasFixedSize(true);
         * mPopularRepoList.setNestedScrollingEnabled(false);
         */
    }

    private void loadData(){
        mPresenter.loadRepos(mUserLogin, AccountPref.isSelf(this.getContext(),mUserLogin), mRepoType);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    @Override
    public void showLoading() {
    }

    @Override
    public void dismissLoading() {
    }

    @Override
    public void showContent(ArrayList<Repo> repos) {
        if(repos != null)
            mRepoListAdapter2.setNewData(repos);

        if(mPresenter.getPage() > 1){
            mRepoListAdapter2.openLoadMore(mPresenter.getPageSize(),true);
        }

        if(mRepoType == RepoApi.OWNER_REPOS)
            mRepoSize.setText(getResources().getString(R.string.act_user_repository,mActivity.mReposCount));
        else if(mRepoType == RepoApi.STARRED_REPOS){
            mPresenter.getReposCount();
        }

        if(repos == null || repos.size() == 0) {
            if(mRepoType == RepoApi.STARRED_REPOS)
                ((TextView) mEmptyView.findViewById(R.id.empty_title)).setText(R.string.no_starrepo_loaded);
            else if(mRepoType == RepoApi.OWNER_REPOS)
                ((TextView)mEmptyView.findViewById(R.id.empty_title)).setText(R.string.no_repo_loaded);
        }
    }

    @Override
    public void showError(Throwable e) {
        AppLog.e(e);

        if(mRepoType == RepoApi.STARRED_REPOS)
            ((TextView) mEmptyView.findViewById(R.id.empty_title)).setText(R.string.no_starrepo_loaded);
        else if(mRepoType == RepoApi.OWNER_REPOS)
            ((TextView)mEmptyView.findViewById(R.id.empty_title)).setText(R.string.no_repo_loaded);

        CustomSnackbar.make(rootView, e.getMessage(), Snackbar.LENGTH_LONG)
                .setTextColor(R.color.md_white_1000).show();
    }

    @Override
    public void showEmpty() {

    }

    OnLoadMoreListener<ArrayList<Repo>> mOnLoadMoreListener = new OnLoadMoreListener<ArrayList<Repo>>() {
        @Override
        public void onStart() {
        }

        @Override
        public void onEnd() {
        }

        @Override
        public void onSuccess(ArrayList<Repo> repos) {
            mRepoListAdapter2.notifyDataChangedAfterLoadMore(repos, true);
            if(repos == null || repos.size() < mPresenter.getPageSize())
                mRepoListAdapter2.openLoadMore(false);
        }

        @Override
        public void onError(Throwable e) {
            if(e.getMessage().equals("no more page"))
                mRepoListAdapter2.openLoadMore(false);
            else {
                AppLog.e(e);
                CustomSnackbar.make(rootView, e.getMessage(), Snackbar.LENGTH_LONG)
                        .setTextColor(R.color.md_white_1000).show();
            }
        }
    };
}
