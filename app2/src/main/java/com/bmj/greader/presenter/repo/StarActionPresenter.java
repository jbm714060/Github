package com.bmj.greader.presenter.repo;

import javax.inject.Inject;

import com.bmj.greader.common.wrapper.AppLog;
import com.bmj.greader.data.api.RepoApi;
import com.bmj.greader.data.rx.ResponseObserver;
import com.bmj.greader.presenter.base.RxMvpPresenter;
import com.bmj.greader.ui.module.repo.view.RepoView;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/11/19 0019.
 */
public class StarActionPresenter extends RxMvpPresenter<RepoView>{
    private RepoApi mRepoApi;

    @Inject
    public StarActionPresenter(RepoApi repoApi){
        mRepoApi = repoApi;
    }

    public void starRepo(String owner,String repo){
        mCompositeSubscription.add(
                mRepoApi.starRepo(owner,repo)
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
                        if(aBoolean)
                            getMvpView().starSuccess();
                        else
                            getMvpView().starFailed();
                    }

                    @Override
                    public void onError(Throwable e) {
                        AppLog.e(e);
                        getMvpView().starFailed();
                    }
                })
        );
    }

    public void unstarRepo(String owner,String repo){
        mCompositeSubscription.add(
                mRepoApi.unstarRepo(owner,repo)
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
                        if(aBoolean)
                            getMvpView().unstarSuccess();
                        else
                            getMvpView().unstarFailed();
                    }

                    @Override
                    public void onError(Throwable e) {
                        AppLog.e(e);
                        getMvpView().unstarFailed();
                    }
                })
        );
    }
}
