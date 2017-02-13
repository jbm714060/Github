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
import com.bmj.greader.data.model.User;
import com.bmj.greader.data.pref.AccountPref;
import com.bmj.greader.presenter.base.OnLoadMoreListener;
import com.bmj.greader.presenter.repo.UserListPresenter2;
import com.bmj.greader.ui.base.BaseFragment;
import com.bmj.greader.ui.module.main.UserDetailActivity;
import com.bmj.greader.ui.module.repo.adapter.UserListRecyclerAdapter2;
import com.bmj.mvp.lce.LceView;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/12/17 0017.
 */
public class UserListFragment extends BaseFragment implements LceView<ArrayList<User>>{

    @BindView(R.id.fo_popular_repolist)
    RecyclerView mPopularRepoList;
    @BindView(R.id.fo_repo_size)
    TextView mRepoSize;
    @BindView(R.id.fo_repo_typetag)
    TextView mRepoTypeTag;

    @Inject
    protected UserListPresenter2 mPresenter;

    private UserListRecyclerAdapter2 mAdapter2;
    private UserDetailActivity mActivity;

    private String mUserLogin;
    private int mUserType;

    private View rootView;
    private View mloadingView;
    private View mEmptyView;

    private static String EXTRA_USERLOGIN = "extra_user_login";
    private static String EXTRA_USERTYPE = "extra_user_type";
    public static UserListFragment instance(String userlogin, @RepoApi.UserType int usertype){
        UserListFragment fragment = new UserListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_USERLOGIN,userlogin);
        bundle.putInt(EXTRA_USERTYPE,usertype);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getComponent(RepoComponent.class).inject(this);
        mPresenter.attachView(this);

        mUserLogin = getArguments().getString(EXTRA_USERLOGIN);
        mUserType = getArguments().getInt(EXTRA_USERTYPE);
        mActivity = UserDetailActivity.class.cast(getActivity());

        mPresenter.setOnLoadMoreListener(mLoadMoreListener);
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
        if(mUserType == RepoApi.FOLLOWER)
            mRepoTypeTag.setText(R.string.fragment_followers);
        else if(mUserType == RepoApi.FOLLOWING)
            mRepoTypeTag.setText(R.string.fragment_following);

        ((TextView)mEmptyView.findViewById(R.id.empty_title)).setText(R.string.loading_repo);
        mAdapter2 = new UserListRecyclerAdapter2(null);
        mAdapter2.setEmptyView(mEmptyView);
        mAdapter2.setLoadingView(mloadingView);
        mAdapter2.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
                User user = mAdapter2.getItem(i);
                UserDetailActivity.launch(UserListFragment.this.getActivity(),user.getLogin());
            }
        });
        mAdapter2.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                mPresenter.loadMoreUsers();
            }
        });

        mPopularRepoList.setLayoutManager(new LinearLayoutManager(this.getContext()));
        mPopularRepoList.setAdapter(mAdapter2);
    }

    private void loadData(){
        mPresenter.loadUsers(mUserLogin, AccountPref.isSelf(this.getContext(),mUserLogin),mUserType);
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
    public void showContent(ArrayList<User> data) {
        mAdapter2.setNewData(data);
        if(mPresenter.getPage() > 1){
            mAdapter2.openLoadMore(mPresenter.getPageSize(),true);
        }

        if(mUserType == RepoApi.FOLLOWER)
            mRepoSize.setText(getResources().getString(R.string.act_user_repository,mActivity.mFollowersCount));
        else if(mUserType == RepoApi.FOLLOWING){
            mRepoSize.setText(getResources().getString(R.string.act_user_repository,mActivity.mFollowingCount));
        }

        if(data == null || data.size() == 0) {
            if(mUserType == RepoApi.FOLLOWING)
                ((TextView) mEmptyView.findViewById(R.id.empty_title)).setText(R.string.no_following_loaded);
            else if(mUserType == RepoApi.FOLLOWER)
                ((TextView) mEmptyView.findViewById(R.id.empty_title)).setText(R.string.no_follower_loaded);
        }
    }

    @Override
    public void showError(Throwable e) {
        AppLog.e(e);
        if(mUserType == RepoApi.FOLLOWING)
            ((TextView) mEmptyView.findViewById(R.id.empty_title)).setText(R.string.no_following_loaded);
        else if(mUserType == RepoApi.FOLLOWER)
            ((TextView) mEmptyView.findViewById(R.id.empty_title)).setText(R.string.no_follower_loaded);
        CustomSnackbar.make(rootView, e.getMessage(), Snackbar.LENGTH_LONG)
                .setTextColor(R.color.md_white_1000).show();
    }

    @Override
    public void showEmpty() {

    }

    private OnLoadMoreListener<ArrayList<User>> mLoadMoreListener = new OnLoadMoreListener<ArrayList<User>>() {
        @Override
        public void onStart() {

        }

        @Override
        public void onEnd() {

        }

        @Override
        public void onSuccess(ArrayList<User> users) {
            mAdapter2.notifyDataChangedAfterLoadMore(users, true);
            if(users == null || users.size() < mPresenter.getPageSize())
                mAdapter2.openLoadMore(false);
        }

        @Override
        public void onError(Throwable e) {
            if(e.getMessage().equals("no more page"))
                mAdapter2.openLoadMore(false);
            else {
                AppLog.e(e);
                CustomSnackbar.make(rootView, e.getMessage(), Snackbar.LENGTH_LONG)
                        .setTextColor(R.color.md_white_1000).show();
            }
        }
    };
}
