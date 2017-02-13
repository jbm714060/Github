package com.bmj.greader.ui.module.repo;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.bmj.greader.data.model.GithubComment;
import com.bmj.greader.data.model.gist.Gist;
import com.bmj.greader.data.model.issue.Issue;
import com.bmj.greader.presenter.base.OnLoadMoreListener;
import com.bmj.greader.presenter.gist.GistCommentPresenter;
import com.bmj.greader.presenter.repo.IssueCommentPresenter;
import com.bmj.greader.ui.module.main.UserDetailActivity;
import com.bmj.greader.ui.module.user.GistCommentsRecyclerAdapter;
import com.bmj.greader.ui.module.user.InputPopup;
import com.bmj.greader.ui.module.user.view.CommentView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.github.clans.fab.FloatingActionButton;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;

import javax.inject.Inject;

import fr.castorflex.android.circularprogressbar.CircularProgressBar;
import razerdp.basepopup.BasePopupWindow;

/**
 * Created by Administrator on 2016/12/23 0023.
 */
public class IssueCommentsPopup extends BasePopupWindow implements CommentView {

    private RecyclerView mCommentList;
    private GistCommentsRecyclerAdapter mAdapter;
    private Context mContext;
    private Activity mActivity;
    private FloatingActionButton mAddButton;
    private CircularProgressBar mProgressBar;
    private Issue mIssue;
    private String mUserLogin;
    private String mRepoName;

    @Inject
    protected IssueCommentPresenter mPresenter;

    public IssueCommentsPopup(Activity activity,Context context,String userlogin,String repoName,Issue issue) {
        super(activity);
        mContext = context;
        mActivity = activity;
        mIssue = issue;
        mUserLogin = userlogin;
        mRepoName = repoName;
        mCommentList = (RecyclerView) findViewById(R.id.pgc_comments_list);

        ((RepoIssueActivity)activity).getComponent().inject(this);
        mPresenter.attachView(this);

        initViews();
        if(mIssue.comments > 0)
            mPresenter.getComments(mUserLogin,mRepoName,mIssue.number);
    }

    private void initViews(){
        GithubComment comment = new GithubComment();
        comment.setUser(mIssue.user);
        comment.setBody(mIssue.body);
        comment.setUpdated_at(mIssue.updated_at);
        ArrayList<GithubComment> comments = new ArrayList<>();
        comments.add(comment);

        mAdapter = new GistCommentsRecyclerAdapter(comments);
        mAdapter.setOnRecyclerViewItemChildClickListener(new BaseQuickAdapter.OnRecyclerViewItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int i) {
                GithubComment comment = (GithubComment) adapter.getItem(i);
                if(view.getId() == R.id.ig_gist_id || view.getId()==R.id.ig_avatar){
                    UserDetailActivity.launch(IssueCommentsPopup.this.mActivity,comment.getUser().getLogin());
                }else if(view.getId() == R.id.ig_comment_delete){
                    mPresenter.deleteComment(mUserLogin,mRepoName,comment.getId(),i);
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
                        mPresenter.addComment(mUserLogin,mRepoName,mIssue.number,text);
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

        if(mIssue.comments == 0)
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
        mAdapter.addData(data);
        if(mPresenter.getPage() > 1){
            mAdapter.openLoadMore(mPresenter.getPageSize(),true);
        }
        if(!mAddButton.isShown())
            mAddButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void showError(Throwable e) {
        if(e.getMessage().equals("no more page"))
            mAdapter.openLoadMore(false);
        else {
            AppLog.e(e);
            CustomSnackbar.make(mActivity.getWindow().getDecorView(), e.getMessage(), Snackbar.LENGTH_LONG)
                    .setTextColor(R.color.md_white_1000).show();
        }
    }

    @Override
    public void showEmpty() {

    }

    @Override
    public void loadMore(ArrayList<GithubComment> data) {
        mAdapter.notifyDataChangedAfterLoadMore(data, true);
        if(data == null || data.size() < mPresenter.getPageSize() || mPresenter.getPage() <= 1)
            mAdapter.openLoadMore(false);
    }

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
