package com.bmj.greader.presenter.repo;

import android.util.Log;

import java.util.ArrayList;

import javax.inject.Inject;

import com.bmj.greader.data.api.RepoApi;
import com.bmj.greader.data.net.pagination.PaginationLinks;
import com.bmj.greader.data.net.response.Content;
import com.bmj.greader.data.rx.ResponseObserver;
import com.bmj.greader.presenter.base.RxMvpPresenter;
import com.bmj.greader.ui.module.repo.view.RepoTreeView;
import com.bmj.mvp.lce.LceView;

import retrofit2.Response;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/11/20 0020.
 */
public class RepoTreePresenter extends RxMvpPresenter<RepoTreeView> {

    private RepoApi mRepoApi;

    @Inject
    public RepoTreePresenter(RepoApi api){
        mRepoApi = api;
    }

    public void getRepoContents(final String owner, final String repo, final String path){
        mCompositeSubscription.add(
                mRepoApi.getRepoContents(owner,repo,path)
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
                .subscribe(new ResponseObserver<ArrayList<Content>>(){
                    @Override
                    public void onSuccess(ArrayList<Content> contents) {
                        getMvpView().showContent(contents);
                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().showError(e);
                    }
                })
        );
    }
}
