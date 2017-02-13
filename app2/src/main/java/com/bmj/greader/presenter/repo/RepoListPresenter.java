package com.bmj.greader.presenter.repo;

import java.util.ArrayList;

import com.bmj.greader.data.net.pagination.PaginationLinks;
import com.bmj.greader.data.rx.ResponseObserver;

import retrofit2.Response;
import rx.Observable;

import javax.inject.Inject;

import com.bmj.greader.data.api.RepoApi;
import com.bmj.greader.data.model.Repo;
import com.bmj.greader.presenter.base.OnLoadMoreListener;
import com.bmj.greader.presenter.base.RxMvpPresenter;
import com.bmj.mvp.lce.LceView;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/11/21 0021.
 */
public class RepoListPresenter extends RxMvpPresenter<LceView<ArrayList<Repo>>>{
    private RepoApi mRepoApi;
    private PaginationLinks mLinks;
    private String mUsername;
    private boolean mIsSelf;
    private int mRepoType;
    private OnLoadMoreListener<ArrayList<Repo>> mLoadMoreListener;
    private int mFirstPageSize = 0;
    private OnReposCountListener mReposCountListener;

    @Inject
    public RepoListPresenter(RepoApi api){
        mRepoApi = api;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener<ArrayList<Repo>> listener){
        mLoadMoreListener = listener;
    }

    public void setOnReposCountListener(OnReposCountListener listener){
        mReposCountListener = listener;
    }

    public void loadRepos(String username, boolean isSelf, @RepoApi.RepoType int repoType){
        mUsername = username;
        mIsSelf = isSelf;
        mRepoType = repoType;
        mFirstPageSize = 0;

        Observable<Response<ArrayList<Repo>>> observable = getObservable(username,isSelf,repoType,1);
        if(observable == null) {
            getMvpView().showError(new IllegalArgumentException("repoType is Illegal"));
            return;
        }

        mCompositeSubscription.add(
            observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                    .map(new Func1<Response<ArrayList<Repo>>, ArrayList<Repo>>() {
                        @Override
                        public ArrayList<Repo> call(Response<ArrayList<Repo>> arrayListResponse) {
                            mLinks = new PaginationLinks(arrayListResponse.headers().get("Link"));
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
                .subscribe(new ResponseObserver<ArrayList<Repo>>() {
                    @Override
                    public void onSuccess(ArrayList<Repo> repos) {
                        if(repos != null)
                            mFirstPageSize += repos.size();
                        getMvpView().showContent(repos);
                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().showError(e);
                    }
                })
        );
    }

    private Observable<Response<ArrayList<Repo>>> getObservable(
            String username, boolean isSelf, @RepoApi.RepoType int repoType,int page){
        Observable<Response<ArrayList<Repo>>> observable = null;
        switch(repoType){
            case RepoApi.OWNER_REPOS:
                observable = isSelf?mRepoApi.getMyRepos(page):mRepoApi.getUserRepos(username,page);
                break;
            case RepoApi.STARRED_REPOS:
                observable = isSelf?mRepoApi.getMyStarredRepos(page):mRepoApi.getUserStarredRepos(username,page);
                break;
            case RepoApi.ORG_REPOS:
                break;
        }
        return observable;
    }

    public void loadMoreRepos(){
        if(getPage() > 1){
            Observable<Response<ArrayList<Repo>>> observable =
                    getObservable(mUsername,mIsSelf,mRepoType,getPage());
            mCompositeSubscription.add(
                    observable.subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .map(new Func1<Response<ArrayList<Repo>>, ArrayList<Repo>>() {
                                @Override
                                public ArrayList<Repo> call(Response<ArrayList<Repo>> arrayListResponse) {
                                    mLinks = new PaginationLinks(arrayListResponse.headers().get("Link"));
                                    return arrayListResponse.body();
                                }
                            })
                            .subscribe(new ResponseObserver<ArrayList<Repo>>() {
                                @Override
                                public void onSuccess(ArrayList<Repo> repos) {
                                    mLoadMoreListener.onSuccess(repos);
                                }

                                @Override
                                public void onError(Throwable e) {
                                    mLoadMoreListener.onError(e);
                                }
                            })
            );
        }else{
            mLoadMoreListener.onError(new Throwable("no more page"));
        }
    }

    public void getReposCount(){
        if(mLinks.getLastPage() <= 1)
            mReposCountListener.getReposCount(mFirstPageSize);
        else{
            Observable<Response<ArrayList<Repo>>> observable =
                    getObservable(mUsername,mIsSelf,mRepoType,mLinks.getLastPage());
            mCompositeSubscription.add(
                    observable.subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new ResponseObserver<Response<ArrayList<Repo>>>() {
                                @Override
                                public void onSuccess(Response<ArrayList<Repo>> arrayListResponse) {
                                    mReposCountListener.getReposCount(
                                            mFirstPageSize*(mLinks.getLastPage()-1)+arrayListResponse.body().size());
                                }

                                @Override
                                public void onError(Throwable e) {
                                    mReposCountListener.getReposCount(mFirstPageSize*(mLinks.getLastPage()-1));
                                }
                            })
            );
        }
    }

    public int getPage(){
        if(mLinks == null)
            return 1;
        else if (mLinks.getNextPage() !=0 && mLinks.getNextPage() <= mLinks.getLastPage()) {
            return mLinks.getNextPage();
        }
        else
            return -1;
    }

    public int getPageSize(){
        return mLinks.getPageSize();
    }

    public interface OnReposCountListener{
        void getReposCount(int reposCount);
    }
}
