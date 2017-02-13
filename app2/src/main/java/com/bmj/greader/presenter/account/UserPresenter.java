package com.bmj.greader.presenter.account;

import javax.inject.Inject;

import com.bmj.greader.data.api.RepoApi;
import com.bmj.greader.data.model.User;
import com.bmj.greader.data.rx.ResponseObserver;
import com.bmj.greader.presenter.base.RxMvpPresenter;
import com.bmj.mvp.lce.LceView;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/11/15 0015.
 */
public class UserPresenter extends RxMvpPresenter<LceView<User>> {
    private RepoApi mRepoApi;

    @Inject
    public UserPresenter(RepoApi repoApi){
        mRepoApi = repoApi;
    }

    public void getSingleUser(String name){
        mCompositeSubscription.add(
                mRepoApi.getSingleUser(name)
                .subscribeOn(Schedulers.io())
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
                        getMvpView().showContent(user);
                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().showError(e);
                    }
                })
        );
    }
}
