package com.bmj.greader.ui.module.user;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;

import com.blankj.utilcode.utils.ToastUtils;
import com.bmj.greader.R;
import com.bmj.greader.common.wrapper.AppLog;
import com.bmj.greader.common.wrapper.CustomSnackbar;
import com.bmj.greader.data.model.gist.Gist;
import com.bmj.greader.data.model.GithubComment;
import com.bmj.greader.presenter.base.OnLoadMoreListener;
import com.bmj.greader.presenter.gist.GistCommentPresenter;
import com.bmj.greader.ui.module.main.UserDetailActivity;
import com.bmj.greader.ui.module.user.view.CommentView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.github.clans.fab.FloatingActionButton;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;

import javax.inject.Inject;

import fr.castorflex.android.circularprogressbar.CircularProgressBar;
import razerdp.basepopup.BasePopupWindow;

/**
 * Created by Administrator on 2016/12/21 0021.
 */
public class GistCommentsPopup extends BasePopupWindow implements CommentView{

    private RecyclerView mCommentList;
    private GistCommentsRecyclerAdapter mAdapter;
    private Context mContext;
    private Activity mActivity;
    private FloatingActionButton mAddButton;
    private CircularProgressBar mProgressBar;
    private Gist mGist;

    @Inject
    protected GistCommentPresenter mPresenter;

    public GistCommentsPopup(Activity activity,Context context,Gist gist) {
        super(activity);
        mContext = context;
        mActivity = activity;
        mGist = gist;
        mCommentList = (RecyclerView) findViewById(R.id.pgc_comments_list);

        ((UserDetailActivity)activity).getComponent().inject(this);
        mPresenter.attachView(this);
        mPresenter.setOnLoadMoreListener(mLoadMoreListener);

        initViews();
        if(mGist.getComments() > 0)
            mPresenter.getComments(gist.getId());
    }

    private void initViews(){
        mAdapter = new GistCommentsRecyclerAdapter(null);
        mAdapter.setOnRecyclerViewItemChildClickListener(new BaseQuickAdapter.OnRecyclerViewItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int i) {
                GithubComment comment = (GithubComment) adapter.getItem(i);
                if(view.getId() == R.id.ig_gist_id || view.getId()==R.id.ig_avatar){
                    UserDetailActivity.launch(GistCommentsPopup.this.mContext,comment.getUser().getLogin());
                }else if(view.getId() == R.id.ig_comment_delete){
                    mPresenter.deleteComment(mGist.getId(),comment.getId(),i);
                }
            }
        });
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                mPresenter.loadMoreComments();
            }
        });

        mCommentList.setLayoutManager(new LinearLayoutManager(mContext));
        mCommentList.addItemDecoration(new HorizontalDividerItemDecoration.Builder(mContext)
                .size(1)
                .colorResId(R.color.md_grey_300)
                .build()
        );
        mCommentList.setAdapter(mAdapter);

        mProgressBar = (CircularProgressBar)findViewById(R.id.pgc_code_progress);

        setFabAnimation();
        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputPopup inputPopup = new InputPopup(mActivity, new InputPopup.OnTextCommitListener() {
                    @Override
                    public void OnTextInputCompleted(String text) {
                        mPresenter.addComment(mGist.getId(),text);
                    }
                });
                inputPopup.showPopupWindow();
            }
        });
    }

    private void setFabAnimation(){
        mAddButton = (FloatingActionButton)findViewById(R.id.pgc_addcomment);
        mAddButton.setHideAnimation(AnimationUtils.loadAnimation(mActivity,
                R.anim.hide_to_bottom));
        mAddButton.setShowAnimation(AnimationUtils.loadAnimation(mActivity,
                R.anim.show_from_bottom));
        mCommentList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(dy > 0){
                    mAddButton.hide(true);
                }
                else if(dy < 0) {
                    mAddButton.show(true);
                }
            }
        });

        if(mGist.getComments() == 0)
            mAddButton.setVisibility(View.VISIBLE);
    }

    @Override
    protected Animation initShowAnimation() {
        ScaleAnimation mScaleAnimation = new ScaleAnimation(0f, 1f, 0f, 1f, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        mScaleAnimation.setDuration(200);
        mScaleAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        mScaleAnimation.setFillAfter(false);

        AlphaAnimation mAlphaAnimation = new AlphaAnimation(0f, 1f);
        mAlphaAnimation.setDuration(400);
        mAlphaAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        mAlphaAnimation.setFillAfter(false);

        AnimationSet mAnimationSet = new AnimationSet(false);
        mAnimationSet.setDuration(400);
        mAnimationSet.addAnimation(mScaleAnimation);
        mAnimationSet.addAnimation(mAlphaAnimation);

        return mAnimationSet;
    }

    @Override
    protected Animation initExitAnimation() {
        ScaleAnimation mScaleAnimation = new ScaleAnimation(1f, 2f, 1f, 2f, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        mScaleAnimation.setDuration(200);
        mScaleAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        mScaleAnimation.setFillAfter(false);

        AlphaAnimation mAlphaAnimation = new AlphaAnimation(1, 0f);
        mAlphaAnimation.setDuration(400);
        mAlphaAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        mAlphaAnimation.setFillAfter(false);

        AnimationSet mAnimationSet = new AnimationSet(false);
        mAnimationSet.setDuration(400);
        mAnimationSet.addAnimation(mScaleAnimation);
        mAnimationSet.addAnimation(mAlphaAnimation);
        return mAnimationSet;
    }

    @Override
    public void dismiss(){
        super.dismiss();
        mPresenter.detachView();
    }

    @Override
    public View getClickToDismissView() {
        return getPopupWindowView();
    }

    @Override
    public View onCreatePopupView() {
        return createPopupById(R.layout.popup_gist_comment);
    }

    @Override
    public View initAnimaView() {
        return findViewById(R.id.pgc_cardview);
    }

    @Override
    public void showLoading() {
        mProgressBar.setIndeterminate(true);
    }

    @Override
    public void dismissLoading() {
        mProgressBar.setIndeterminate(false);
    }

    @Override
    public void showContent(ArrayList<GithubComment> data) {
        mAdapter.setNewData(data);
        if(mPresenter.getPage() > 1){
            mAdapter.openLoadMore(mPresenter.getPageSize(),true);
        }
        if(!mAddButton.isShown())
            mAddButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void showError(Throwable e) {
        AppLog.e(e);
        CustomSnackbar.make(mActivity.getWindow().getDecorView(), e.getMessage(), Snackbar.LENGTH_LONG)
                .setTextColor(R.color.md_white_1000).show();
    }

    @Override
    public void showEmpty() {

    }

    @Override
    public void loadMore(ArrayList<GithubComment> data) {

    }

    private OnLoadMoreListener<ArrayList<GithubComment>> mLoadMoreListener =
            new OnLoadMoreListener<ArrayList<GithubComment>>() {
        @Override
        public void onStart() {

        }

        @Override
        public void onEnd() {

        }

        @Override
        public void onSuccess(ArrayList<GithubComment> githubComments) {
            mAdapter.notifyDataChangedAfterLoadMore(githubComments, true);
            if(githubComments == null || githubComments.size() < mPresenter.getPageSize())
                mAdapter.openLoadMore(false);
        }

        @Override
        public void onError(Throwable e) {
            if(e.getMessage().equals("no more page"))
                mAdapter.openLoadMore(false);
            else {
                AppLog.e(e);
                CustomSnackbar.make(mActivity.getWindow().getDecorView(), e.getMessage(), Snackbar.LENGTH_LONG)
                        .setTextColor(R.color.md_white_1000).show();
            }
        }
    };

    @Override
    public void addCommentResult(boolean isSuccess,GithubComment comment){
        if(isSuccess){
            mAdapter.add(mAdapter.getItemCount(),comment);
            ToastUtils.showShortToast(getContext(),"Comment Successfully");
        }else
            ToastUtils.showShortToast(getContext(),"Comment Failed");
    }
    @Override
    public void deleteCommentResult(boolean isSuccess,int position){
        if(isSuccess){
            mAdapter.remove(position);
            ToastUtils.showShortToast(getContext(),"Delete Successfully");
        }else
            ToastUtils.showShortToast(getContext(),"Delete Failed");
    }
}
