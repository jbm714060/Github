package com.bmj.greader.presenter.repo;

import com.bmj.greader.data.model.GithubComment;
import com.bmj.greader.data.model.gist.CommentRequest;
import com.bmj.greader.data.net.pagination.PaginationLinks;
import com.bmj.greader.data.net.services.IssueService;
import com.bmj.greader.data.rx.ResponseObserver;
import com.bmj.greader.presenter.base.RxMvpPresenter;
import com.bmj.greader.ui.module.user.view.CommentView;

import java.util.ArrayList;

import javax.inject.Inject;

import okhttp3.ResponseBody;
import retrofit2.Response;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/12/23 0023.
 */
public class IssueCommentPresenter extends RxMvpPresenter<CommentView>{

    private IssueService mIssueService;
    private PaginationLinks mLink;
    private String mUserLogin;
    private String mRepoName;
    private int mIssueNumber;

    @Inject
    public IssueCommentPresenter(IssueService issueService){
        mIssueService = issueService;
    }

    public void getComments(String userLogin,String repoName,int issueNumber){
        mUserLogin = userLogin;
        mRepoName = repoName;
        mIssueNumber = issueNumber;
        mLink = null;
        loadMoreComments();
    }

    public void loadMoreComments(){
        final int page = getPage();
        if(page < 1) {
            getMvpView().showError(new Throwable("no more page"));
            return;
        }

        mCompositeSubscription.add(
                mIssueService.comments(mUserLogin,mRepoName,mIssueNumber,page)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .map(new Func1<Response<ArrayList<GithubComment>>, ArrayList<GithubComment>>() {
                            @Override
                            public ArrayList<GithubComment> call(Response<ArrayList<GithubComment>> arrayListResponse) {
                                mLink = new PaginationLinks(arrayListResponse.headers().get("Link"));
                                return arrayListResponse.body();
                            }
                        })
                        .doOnSubscribe(new Action0() {
                            @Override
                            public void call() {
                                if(page == 1)
                                    getMvpView().showLoading();
                            }
                        })
                        .doOnTerminate(new Action0() {
                            @Override
                            public void call() {
                                if(page == 1)
                                    getMvpView().dismissLoading();
                            }
                        })
                        .subscribe(new ResponseObserver<ArrayList<GithubComment>>() {
                            @Override
                            public void onSuccess(ArrayList<GithubComment> githubComments) {
                                if(page == 1)
                                    getMvpView().showContent(githubComments);
                                else
                                    getMvpView().loadMore(githubComments);
                            }

                            @Override
                            public void onError(Throwable e) {
                                getMvpView().showError(e);
                            }
                        })
        );
    }

    public void deleteComment(String userlogin,String reponame,int commentId,final int position){
        mCompositeSubscription.add(
                mIssueService.deleteComment(userlogin,reponame,commentId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResponseObserver<Response<ResponseBody>>() {
                    @Override
                    public void onSuccess(Response<ResponseBody> response) {
                        getMvpView().deleteCommentResult(response!=null && response.code()==204,position);
                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().showError(e);
                        getMvpView().deleteCommentResult(false,position);
                    }
                })
        );
    }

    public void addComment(String owner,String repo,int number,String body){
        mCompositeSubscription.add(
                mIssueService.addComment(owner,repo,number,new CommentRequest(body))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new ResponseObserver<Response<GithubComment>>() {
                            @Override
                            public void onSuccess(Response<GithubComment> response) {
                                getMvpView().addCommentResult(response!=null&&response.code()==201,response.body());
                            }

                            @Override
                            public void onError(Throwable e) {
                                getMvpView().showError(e);
                                getMvpView().addCommentResult(false,null);
                            }
                        })
        );
    }

    public int getPage(){
        if(mLink == null)
            return 1;
        else if (mLink.getNextPage() !=0 && mLink.getNextPage() <= mLink.getLastPage()) {
            return mLink.getNextPage();
        }
        else
            return -1;
    }

    public int getPageSize(){
        return mLink.getPageSize();
    }
}
