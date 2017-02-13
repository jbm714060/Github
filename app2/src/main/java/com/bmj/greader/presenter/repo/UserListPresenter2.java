package com.bmj.greader.presenter.repo;

import android.util.Log;

import com.bmj.greader.data.api.RepoApi;
import com.bmj.greader.data.model.User;
import com.bmj.greader.data.net.pagination.PaginationLinks;
import com.bmj.greader.data.rx.ResponseObserver;
import com.bmj.greader.presenter.base.OnLoadMoreListener;
import com.bmj.greader.presenter.base.RxMvpPresenter;
import com.bmj.mvp.lce.LceView;

import java.util.ArrayList;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/12/17 0017.
 */
public class UserListPresenter2 extends RxMvpPresenter<LceView<ArrayList<User>>> {
    private RepoApi mRepoApi;
    private PaginationLinks mLinks;
    private OnLoadMoreListener<ArrayList<User>> loadMoreListener;
    private String mUsername;
    private boolean mIsSelf;
    private int mType;

    @Inject
    public UserListPresenter2(RepoApi api){
        mRepoApi = api;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener<ArrayList<User>> listener){
        loadMoreListener = listener;
    }

    public void loadUsers(String userName, boolean isSelf, @RepoApi.UserType int type){
        mUsername = userName;
        mIsSelf = isSelf;
        mType = type;
        Observable<User> observable = getObservable(userName,isSelf,type,1);
        if(observable == null)
            return;
        final ArrayList<User> userArray = new ArrayList<>();
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
                        .subscribe(new ResponseObserver<User>() {
                            @Override
                            public void onSuccess(User user) {
                                userArray.add(user);
                            }

                            @Override
                            public void onCompleted() {
                                super.onCompleted();
                                getMvpView().showContent(userArray);
                            }

                            @Override
                            public void onError(Throwable e) {
                                getMvpView().showError(e);
                            }
                        })
        );
    }

    private Observable<User> getObservable(
            String userName, boolean isSelf, @RepoApi.UserType int type,int page) {
        Observable<User> observable = null;
        switch (type) {
            case RepoApi.FOLLOWER:
                observable = (isSelf?mRepoApi.getMyFollowers(page):mRepoApi.getUserFollowers(userName, page))
                        .map(new Func1<Response<ArrayList<User>>, ArrayList<User>>() {
                            @Override
                            public ArrayList<User> call(Response<ArrayList<User>> arrayListResponse) {
                                mLinks = new PaginationLinks(arrayListResponse.headers().get("Link"));
                                return arrayListResponse.body();
                            }
                        }) .flatMap(new Func1<ArrayList<User>, Observable<User>>() {
                            @Override
                            public Observable<User> call(ArrayList<User> users) {
                                if(users == null)
                                    return null;

                                User[] userArray = new User[users.size()];
                                users.toArray(userArray);
                                return Observable.from(userArray).flatMap(new Func1<User, Observable<User>>() {
                                    @Override
                                    public Observable<User> call(User user) {
                                        return mRepoApi.getSingleUser(user.getLogin());
                                    }
                                });
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
                        })
                        .flatMap(new Func1<ArrayList<User>, Observable<User>>() {
                            @Override
                            public Observable<User> call(ArrayList<User> users) {
                                if(users == null)
                                    return null;

                                User[] userArray = new User[users.size()];
                                users.toArray(userArray);
                                return Observable.from(userArray).flatMap(new Func1<User, Observable<User>>() {
                                    @Override
                                    public Observable<User> call(User user) {
                                        return mRepoApi.getSingleUser(user.getLogin());
                                    }
                                });
                            }
                        });
                break;
        }
        return observable;
    }

    public void loadMoreUsers(){
        if(mLinks.getNextPage() !=0 && mLinks.getNextPage() <= mLinks.getLastPage()){
            final ArrayList<User> userArray = new ArrayList<>();
            Observable<User> observable = getObservable(mUsername,mIsSelf,mType,mLinks.getNextPage());
            mCompositeSubscription.add(
                    observable.subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new ResponseObserver<User>() {
                                @Override
                                public void onSuccess(User user) {
                                    userArray.add(user);
                                }

                                @Override
                                public void onCompleted() {
                                    super.onCompleted();
                                    loadMoreListener.onSuccess(userArray);
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
