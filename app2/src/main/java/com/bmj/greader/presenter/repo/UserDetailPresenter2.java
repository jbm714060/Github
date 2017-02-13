package com.bmj.greader.presenter.repo;

import com.bmj.greader.common.wrapper.AppLog;
import com.bmj.greader.data.api.RepoApi;
import com.bmj.greader.data.model.Repo;
import com.bmj.greader.data.model.User;
import com.bmj.greader.data.net.RepoDataSource;
import com.bmj.greader.data.net.pagination.PaginationLinks;
import com.bmj.greader.data.rx.ResponseObserver;
import com.bmj.greader.presenter.base.RxMvpPresenter;
import com.bmj.greader.ui.module.repo.view.UserDetailView;
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
 * Created by Administrator on 2016/12/11 0011.
 */
public class UserDetailPresenter2 extends RxMvpPresenter<LceView<User>> {

    private RepoApi mRepoApi;

    @Inject
    public UserDetailPresenter2(RepoDataSource dataSource){
        mRepoApi = dataSource;
    }

    public void loadUser(String userlogin,boolean isSelf){
        mCompositeSubscription.add(
        mRepoApi.getSingleUser(userlogin)
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
