package com.bmj.greader.presenter.account;

import android.app.Application;

import javax.inject.Inject;

import com.bmj.greader.common.wrapper.AppLog;
import com.bmj.greader.data.api.AccountApi;
import com.bmj.greader.data.model.User;
import com.bmj.greader.data.pref.AccountPref;
import com.bmj.greader.data.rx.ResponseObserver;
import com.bmj.greader.presenter.base.RxMvpPresenter;
import com.bmj.greader.ui.module.account.view.LoginView;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/11/14 0014.
 */
public class LoginPresenter extends RxMvpPresenter<LoginView>{
    private AccountApi mAccountApi;

    @Inject
    public LoginPresenter(AccountApi accountApi){
        mAccountApi = accountApi;
    }

    @Inject
    Application application;

    public void login(String userName,String password){
        mAccountApi.login(userName,password)
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
                        AccountPref.saveLogonUser(application,user);
                        AppLog.d("user:" + user.getLogin());
                        getMvpView().loginSuccess(user);
                    }

                    @Override
                    public void onError(Throwable e) {
                        AppLog.e(e);
                        getMvpView().error(e);
                    }
                });
    }
}
