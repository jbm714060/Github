package com.bmj.greader.ui.module.user;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.utils.ToastUtils;
import com.bmj.greader.R;
import com.bmj.greader.common.wrapper.AppLog;
import com.bmj.greader.common.wrapper.CustomSnackbar;
import com.bmj.greader.dagger2.component.RepoComponent;
import com.bmj.greader.data.model.gist.Gist;
import com.bmj.greader.data.model.gist.GistFile;
import com.bmj.greader.data.pref.AccountPref;
import com.bmj.greader.presenter.base.OnLoadMoreListener;
import com.bmj.greader.presenter.gist.GistPresenter;
import com.bmj.greader.ui.base.BaseFragment;
import com.bmj.greader.ui.module.main.UserDetailActivity;
import com.bmj.greader.ui.module.repo.view.GistView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/12/19 0019.
 */
public class UserGistFragment extends BaseFragment implements GistView{
    @BindView(R.id.fo_popular_repolist)
    RecyclerView mPopularRepoList;
    @BindView(R.id.fo_repo_size)
    TextView mRepoSize;
    @BindView(R.id.fo_repo_typetag)
    TextView mRepoTypeTag;
    @BindView(R.id.fo_starred_gists)
    TextView mStarredGist;
    @BindView(R.id.fo_first_range)
    LinearLayout mPublicGistLayout;

    @Inject
    protected GistPresenter mPresenter;

    private GistListRecyclerAdapter mAdapter;
    private UserDetailActivity mActivity;

    private String mUserLogin;

    private View rootView;
    private View mloadingView;
    private View mEmptyView;

    private static String EXTRA_USERLOGIN = "extra_user_login";
    public static UserGistFragment instance(String userlogin){
        UserGistFragment fragment = new UserGistFragment();
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_USERLOGIN,userlogin);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getComponent(RepoComponent.class).inject(this);
        mPresenter.attachView(this);

        mUserLogin = getArguments().getString(EXTRA_USERLOGIN);
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
        mRepoTypeTag.setText(R.string.fragment_gists);
        if(AccountPref.isSelf(this.getContext(),mUserLogin))
            mStarredGist.setVisibility(View.VISIBLE);

        ((TextView)mEmptyView.findViewById(R.id.empty_title)).setText(R.string.loading_repo);
        mAdapter = new GistListRecyclerAdapter(null);
        mAdapter.setEmptyView(mEmptyView);
        mAdapter.setLoadingView(mloadingView);
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                mPresenter.loadMoreUsers();
            }
        });
        mAdapter.setOnRecyclerViewItemChildClickListener(new BaseQuickAdapter.OnRecyclerViewItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                Gist gist = (Gist)baseQuickAdapter.getItem(i);
                if(view.getId() == R.id.ig_file_layout){
                    ArrayList<GistFile> gistFiles = new ArrayList<>(gist.getFiles().values());
                    GistFileListPopup fileListPopup = new GistFileListPopup(getActivity(),getContext(),gistFiles);
                    fileListPopup.showPopupWindow();
                }else if(view.getId() == R.id.ig_comment_layout){
                    GistCommentsPopup CommentsPopup = new GistCommentsPopup(getActivity(),getContext(),gist);
                    CommentsPopup.showPopupWindow();
                }else if(view.getId() == R.id.ig_star){
                    if(AccountPref.checkLogon(getContext())){
                        mPresenter.starGist(gist.getId(),gist.isStarred,i);
                    }
                }
            }
        });

        mPopularRepoList.setLayoutManager(new LinearLayoutManager(this.getContext()));
        mPopularRepoList.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this.getContext())
                .colorResId(R.color.md_grey_500)
                .size(1)
                .build());
        mPopularRepoList.setAdapter(mAdapter);
    }

    private void loadData(){
        mPresenter.getUsersGists(mUserLogin, AccountPref.isSelf(this.getContext(),mUserLogin));
    }

    private int gistType = 0;
    @OnClick({R.id.fo_starred_gists,R.id.fo_first_range})
    protected void onClick(View view){
        if(view.getId() == R.id.fo_starred_gists && mStarredGist.getX() > mPublicGistLayout.getX()) {
            ObjectAnimator starAni = ObjectAnimator.ofFloat(mStarredGist,"translationX",mPublicGistLayout.getX()-mStarredGist.getX());
            ObjectAnimator publicAni = ObjectAnimator.ofFloat(mPublicGistLayout,"translationX", mStarredGist.getRight()-mPublicGistLayout.getRight());
            starAni.setDuration(300);
            publicAni.setDuration(300);
            starAni.start();
            publicAni.start();
            gistType = 1;
            mPresenter.getStarredGists();
            return;
        }
        if(view.getId() == R.id.fo_first_range && mStarredGist.getX() < mPublicGistLayout.getX()){
            ObjectAnimator publicAni = ObjectAnimator.ofFloat(mPublicGistLayout,"translationX",mPublicGistLayout.getTranslationX(),0);
            ObjectAnimator starAni = ObjectAnimator.ofFloat(mStarredGist,"translationX",mStarredGist.getTranslationX(),0);
            starAni.setDuration(300);
            publicAni.setDuration(300);
            starAni.start();
            publicAni.start();
            gistType = 0;
            mPresenter.getUsersGists(mUserLogin, AccountPref.isSelf(this.getContext(),mUserLogin));
            return;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    private OnLoadMoreListener<ArrayList<Gist>> mLoadMoreListener = new OnLoadMoreListener<ArrayList<Gist>>() {
        @Override
        public void onStart() {

        }

        @Override
        public void onEnd() {

        }

        @Override
        public void onSuccess(ArrayList<Gist> gists) {
            mAdapter.notifyDataChangedAfterLoadMore(gists, true);
            if(gists == null || gists.size() < mPresenter.getPageSize())
                mAdapter.openLoadMore(false);
        }

        @Override
        public void onError(Throwable e) {
            if(e.getMessage().equals("no more page"))
                mAdapter.openLoadMore(false);
            else {
                AppLog.e(e);
                CustomSnackbar.make(rootView, e.getMessage(), Snackbar.LENGTH_LONG)
                        .setTextColor(R.color.md_white_1000).show();
            }
        }
    };

    @Override
    public void showGists(ArrayList<Gist> gists) {
        if(gistType == 1){
            for(Gist gist:gists)
                gist.isStarred = true;
        }

        mAdapter.setNewData(gists);

        if(mPresenter.getPage() > 1){
            mAdapter.openLoadMore(mPresenter.getPageSize(),true);
        }

        mRepoSize.setText(getResources().getString(R.string.act_user_repository,mActivity.mGistCount));

        if(gists == null || gists.size() == 0)
            ((TextView)mEmptyView.findViewById(R.id.empty_title)).setText(R.string.no_gist_loaded);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void dismissLoading() {

    }

    @Override
    public void error(Throwable e) {
        AppLog.e(e);
        ((TextView)mEmptyView.findViewById(R.id.empty_title)).setText(R.string.no_gist_loaded);
        CustomSnackbar.make(rootView, e.getMessage(), Snackbar.LENGTH_LONG)
                .setTextColor(R.color.md_white_1000).show();
    }

    @Override
    public void isStarSuccessfully(boolean success,int position){
        if(success){
            mAdapter.getItem(position).isStarred = true;
            mAdapter.notifyItemChanged(position);
            ToastUtils.showShortToast(getContext(),"Star Successfully");
        }else{
            ToastUtils.showShortToast(getContext(),"Star Failed");
        }
    }

    @Override
    public void isUnstarSuccessfully(boolean success,int position){
        if(success){
            mAdapter.getItem(position).isStarred = false;
            mAdapter.notifyItemChanged(position);
            ToastUtils.showShortToast(getContext(),"Unstar Successfully");
        }else{
            ToastUtils.showShortToast(getContext(),"Unstar Failed");
        }
    }
}
