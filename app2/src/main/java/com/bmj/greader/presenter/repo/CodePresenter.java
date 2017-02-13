package com.bmj.greader.presenter.repo;

import javax.inject.Inject;

import com.bmj.greader.data.api.RepoApi;
import com.bmj.greader.data.net.response.Content;
import com.bmj.greader.data.rx.ResponseObserver;
import com.bmj.greader.presenter.base.RxMvpPresenter;
import com.bmj.greader.ui.module.repo.view.RepoTreeView;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/11/20 0020.
 */
public class CodePresenter extends RxMvpPresenter<RepoTreeView>{
    private RepoApi mRepoApi;

    @Inject
    public CodePresenter(RepoApi repoApi){
        mRepoApi = repoApi;
    }

    public void getContentDetail(String owner,String reponame,String path){
        mCompositeSubscription.add(
                mRepoApi.getContentDetail(owner,reponame,path)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                       // getMvpView().showLoading();
                    }
                })
                .doOnTerminate(new Action0() {
                    @Override
                    public void call() {
                       // getMvpView().dismissLoading();
                    }
                })
                .subscribe(new ResponseObserver<Content>() {
                    @Override
                    public void onSuccess(Content content) {
                        getMvpView().showCode(content);
                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().showError(e);
                    }
                })
        );
    }
}
