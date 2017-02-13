package com.bmj.greader.presenter.repo;

import android.util.Log;

import javax.inject.Inject;

import com.bmj.greader.data.api.RepoApi;
import com.bmj.greader.data.model.Repo;
import com.bmj.greader.data.model.RepoDetail;
import com.bmj.greader.data.model.User;
import com.bmj.greader.data.net.response.Content;
import com.bmj.greader.data.rx.ResponseObserver;
import com.bmj.greader.presenter.base.RxMvpPresenter;
import com.bmj.greader.ui.module.repo.view.RepoDetailView;

import java.util.ArrayList;
import java.util.jar.Attributes;

import retrofit2.Response;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/11/19 0019.
 */
public class RepoDetailPresenter extends RxMvpPresenter<RepoDetailView>{
    private RepoApi mRepoApi;

    @Inject
    public RepoDetailPresenter(RepoApi repoApi){
        mRepoApi = repoApi;
    }

    public void loadRepo(final String owner, final String repo) {
        mCompositeSubscription.add(
                mRepoApi.getRepo(owner,repo)
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
                .subscribe(new ResponseObserver<Repo>() {
                    @Override
                    public void onSuccess(Repo repo) {
                        getMvpView().showRepo(repo);
                    }

                    @Override
                    public void onCompleted() {
                        super.onCompleted();
                        loadRepoContributors(owner, repo);
                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().error(e);
                    }
                })
        );
    }

    public void loadRepoContributors(final String userlogin,final String repoName){
        mCompositeSubscription.add(
                mRepoApi.getRepoContributors(userlogin, repoName,1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResponseObserver<Response<ArrayList<User>>>() {
                    @Override
                    public void onSuccess(Response<ArrayList<User>> arrayListResponse) {
                        getMvpView().showRepoContributors(arrayListResponse.body());
                    }

                    @Override
                    public void onCompleted() {
                        super.onCompleted();
                        loadRepoForks(userlogin,repoName);
                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().error(e);
                    }
                })
        );
    }

    public void loadRepoForks(final String userlogin,final String repoName){
        mCompositeSubscription.add(
                mRepoApi.getRepoForks(userlogin,repoName,"newest")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResponseObserver<ArrayList<Repo>>() {
                    @Override
                    public void onSuccess(ArrayList<Repo> repos) {
                        getMvpView().showRepoForks(repos);
                    }

                    @Override
                    public void onCompleted() {
                        super.onCompleted();
                        loadRepoReadme(userlogin,repoName);
                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().error(e);
                    }
                })
        );
    }

    public void loadRepoReadme(final String userlogin,final String repoName){
        mCompositeSubscription.add(
                mRepoApi.getRepoReadme(userlogin,repoName)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new ResponseObserver<Content>() {
                            @Override
                            public void onSuccess(Content content) {
                                getMvpView().showReadMe(content);
                            }

                            @Override
                            public void onCompleted() {
                                super.onCompleted();
                                checkStarred(userlogin,repoName);
                            }

                            @Override
                            public void onError(Throwable e) {
                                getMvpView().error(e);
                            }
                        })
        );
    }

    public void checkStarred(String userlogin,String repoName){
        mCompositeSubscription.add(
                mRepoApi.isStarred(userlogin,repoName)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new ResponseObserver<Boolean>() {
                            @Override
                            public void onSuccess(Boolean isStarred) {
                                getMvpView().showIsStarred(isStarred);
                            }

                            @Override
                            public void onCompleted() {
                                super.onCompleted();
                            }

                            @Override
                            public void onError(Throwable e) {
                                getMvpView().error(e);
                            }
                        })
        );
    }
}
