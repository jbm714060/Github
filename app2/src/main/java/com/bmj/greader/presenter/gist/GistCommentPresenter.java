package com.bmj.greader.presenter.gist;

import com.bmj.greader.data.model.gist.CommentRequest;
import com.bmj.greader.data.model.GithubComment;
import com.bmj.greader.data.net.pagination.PaginationLinks;
import com.bmj.greader.data.net.services.GistService;
import com.bmj.greader.data.rx.ResponseObserver;
import com.bmj.greader.presenter.base.OnLoadMoreListener;
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
 * Created by Administrator on 2016/12/21 0021.
 */
public class GistCommentPresenter extends RxMvpPresenter<CommentView> {

    private GistService mGistService;
    private PaginationLinks mLink;
    private String mGistID;
    private OnLoadMoreListener<ArrayList<GithubComment>> loadMoreListener;

    @Inject
    public GistCommentPresenter(GistService gistService){
        mGistService = gistService;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener<ArrayList<GithubComment>> listener){
        loadMoreListener = listener;
    }

    public void getComments(String gistId){
        mGistID = gistId;

        mCompositeSubscription.add(
                mGistService.getGistComments(gistId,1)
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
                        getMvpView().showLoading();
                    }
                })
                .doOnTerminate(new Action0() {
                    @Override
                    public void call() {
                        getMvpView().dismissLoading();
                    }
                })
                .subscribe(new ResponseObserver<ArrayList<GithubComment>>() {
                    @Override
                    public void onSuccess(ArrayList<GithubComment> githubComments) {
                        getMvpView().showContent(githubComments);
                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().showError(e);
                    }
                })
        );
    }

    public void addComment(String gistId,String body){
        mCompositeSubscription.add(
                mGistService.createComment(gistId,new CommentRequest(body))
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

    public void deleteComment(String gistID,int commentId,final int position){
        mCompositeSubscription.add(
                mGistService.deleteComment(gistID,commentId)
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

    public void loadMoreComments(){
        if(getPage() > 1){
            mCompositeSubscription.add(
                    mGistService.getGistComments(mGistID,getPage())
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
                                    loadMoreListener.onStart();
                                }
                            })
                            .doOnTerminate(new Action0() {
                                @Override
                                public void call() {
                                    loadMoreListener.onEnd();
                                }
                            })
                            .subscribe(new ResponseObserver<ArrayList<GithubComment>>() {
                                @Override
                                public void onSuccess(ArrayList<GithubComment> githubComments) {
                                    loadMoreListener.onSuccess(githubComments);
                                }

                                @Override
                                public void onError(Throwable e) {
                                    loadMoreListener.onError(e);
                                }
                            })
            );
        }else{
            loadMoreListener.onError(new Throwable("no more page"));
        }
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
