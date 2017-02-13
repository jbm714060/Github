package com.bmj.greader.presenter.repo;

import com.bmj.greader.data.api.RepoApi;
import com.bmj.greader.data.net.RepoDataSource;
import com.bmj.greader.data.rx.ResponseObserver;
import com.bmj.greader.presenter.base.RxMvpPresenter;
import com.bmj.greader.ui.module.repo.view.FollowActionView;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/12/17 0017.
 */
public class FollowUserPresenter extends RxMvpPresenter<FollowActionView> {

    private RepoApi mRepoApi;

    @Inject
    public FollowUserPresenter(RepoDataSource dataSource){
        mRepoApi = dataSource;
    }

    public void getFollowUserState(String userLogin){
        mCompositeSubscription.add(
                mRepoApi.checkIfFollowUser(userLogin)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        //getMvpView().showLoading();
                    }
                })
                .doOnTerminate(new Action0() {
                    @Override
                    public void call() {
                        //getMvpView().dismissLoading();
                    }
                })
                .subscribe(new ResponseObserver<Boolean>() {
                    @Override
                    public void onSuccess(Boolean aBoolean) {
                        getMvpView().showFollowState(aBoolean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().error(e);
                        getMvpView().showFollowState(false);
                    }
                })
        );
    }

    public void followUser(String userLogin){
        mCompositeSubscription.add(
                mRepoApi.followUser(userLogin)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(new Action0() {
                            @Override
                            public void call() {
                                //getMvpView().showLoading();
                            }
                        })
                        .doOnTerminate(new Action0() {
                            @Override
                            public void call() {
                                //getMvpView().dismissLoading();
                            }
                        })
                        .subscribe(new ResponseObserver<Boolean>() {
                            @Override
                            public void onSuccess(Boolean aBoolean) {
                                getMvpView().showFollowIsSuccess(aBoolean);
                            }

                            @Override
                            public void onError(Throwable e) {
                                getMvpView().error(e);
                                getMvpView().showFollowIsSuccess(false);
                            }
                        })
        );
    }

    public void unfollowUser(String userLogin){
        mCompositeSubscription.add(
                mRepoApi.unfollowUser(userLogin)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(new Action0() {
                            @Override
                            public void call() {
                                //getMvpView().showLoading();
                            }
                        })
                        .doOnTerminate(new Action0() {
                            @Override
                            public void call() {
                                //getMvpView().dismissLoading();
                            }
                        })
                        .subscribe(new ResponseObserver<Boolean>() {
                            @Override
                            public void onSuccess(Boolean aBoolean) {
                                getMvpView().showUnfollowIsSuccess(aBoolean);
                            }

                            @Override
                            public void onError(Throwable e) {
                                getMvpView().error(e);
                                getMvpView().showUnfollowIsSuccess(false);
                            }
                        })
        );
    }
}
