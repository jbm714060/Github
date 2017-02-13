package com.bmj.greader.presenter.repo;

import com.bmj.greader.data.net.pagination.PaginationLinks;
import com.bmj.greader.data.rx.ResponseObserver;

import retrofit2.Response;
import rx.Observable;

import java.util.ArrayList;

import javax.inject.Inject;

import com.bmj.greader.data.api.RepoApi;
import com.bmj.greader.data.model.User;
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
public class UserListPresenter extends RxMvpPresenter<LceView<ArrayList<User>>>{
    private RepoApi mRepoApi;
    private PaginationLinks mLinks;
    private OnLoadMoreListener<ArrayList<User>> loadMoreListener;
    private String mUsername;
    private boolean mIsSelf;
    private int mType;

    @Inject
    public UserListPresenter(RepoApi api){
        mRepoApi = api;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener<ArrayList<User>> listener){
        loadMoreListener = listener;
    }

    public void loadUsers(String userName, boolean isSelf, @RepoApi.UserType int type){
        mUsername = userName;
        mIsSelf = isSelf;
        mType = type;
        Observable<ArrayList<User>> observable = getObservable(userName,isSelf,type,1);
        if(observable == null)
            return;

        mCompositeSubscription.add(
                observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
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
                .subscribe(new ResponseObserver<ArrayList<User>>() {
                    @Override
                    public void onSuccess(ArrayList<User> users) {
                        getMvpView().showContent(users);
                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().showError(e);
                    }
                })
        );
    }

    private Observable<ArrayList<User>> getObservable(
            String userName, boolean isSelf, @RepoApi.UserType int type,int page) {
        Observable<ArrayList<User>> observable = null;
        switch (type) {
            case RepoApi.FOLLOWER:
                observable = (isSelf?mRepoApi.getMyFollowers(page):mRepoApi.getUserFollowers(userName, page))
                        .map(new Func1<Response<ArrayList<User>>, ArrayList<User>>() {
                            @Override
                            public ArrayList<User> call(Response<ArrayList<User>> arrayListResponse) {
                                mLinks = new PaginationLinks(arrayListResponse.headers().get("Link"));
                                return arrayListResponse.body();
                            }
                        });
                break;
            case RepoApi.FOLLOWING:
                observable = (isSelf?mRepoApi.getMyFollowing(page):mRepoApi.getUserFollowing(userName,page))
                        .map(new Func1<Response<ArrayList<User>>, ArrayList<User>>() {
                            @Override
                            public ArrayList<User> call(Response<ArrayList<User>> arrayListResponse) {
                                mLinks = new PaginationLinks(arrayListResponse.headers().get("Link"));
                                return arrayListResponse.body();
                            }
                        });
                break;
        }
        return observable;
    }

    public void loadMoreUsers(){
        if(mLinks.getNextPage() !=0 && mLinks.getNextPage() <= mLinks.getLastPage()){
            Observable<ArrayList<User>> observable = getObservable(mUsername,mIsSelf,mType,mLinks.getNextPage());
            mCompositeSubscription.add(
                    observable.subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new ResponseObserver<ArrayList<User>>() {
                                @Override
                                public void onSuccess(ArrayList<User> repos) {
                                    loadMoreListener.onSuccess(repos);
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
}
