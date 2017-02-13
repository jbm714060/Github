package com.bmj.greader.presenter.repo;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.bmj.greader.common.wrapper.AppLog;
import com.bmj.greader.data.api.RepoApi;
import com.bmj.greader.data.model.Repo;
import com.bmj.greader.data.model.User;
import com.bmj.greader.data.model.UserDetail;
import com.bmj.greader.data.net.pagination.PaginationLinks;
import com.bmj.greader.data.rx.ResponseObserver;
import com.bmj.greader.presenter.base.RxMvpPresenter;
import com.bmj.mvp.lce.LceView;

import java.lang.reflect.Array;
import java.util.ArrayList;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Func1;
import rx.functions.Func4;
import rx.functions.Func5;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/11/29 0029.
 */
public class UserDetailPresenter extends RxMvpPresenter<LceView<UserDetail>>{
    private RepoApi mRepoApi;

    @Inject
    public UserDetailPresenter(RepoApi repoApi){
        mRepoApi = repoApi;
    }

    private int getPage(PaginationLinks links){
        if(links == null)
            return 1;
        return links.getNextPage();
    }

    public void getUserDetail(String userlogin,boolean isSelf){
        Observable<UserDetail> observable = createDetailObservable(userlogin,isSelf);
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
                .subscribe(new ResponseObserver<UserDetail>() {
                    @Override
                    public void onSuccess(UserDetail userDetail) {
                        getMvpView().showContent(userDetail);
                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().showError(e);
                    }
                })
        );
    }

    private Observable<UserDetail> createDetailObservable(String username,boolean isOwn){
        return Observable.zip(
                mRepoApi.getSingleUser(username),
                (isOwn?mRepoApi.getMyFollowers(1):mRepoApi.getUserFollowers(username,1))
                    .map(new Func1<Response<ArrayList<User>>, ArrayList<User>>() {
                    @Override
                    public ArrayList<User> call(Response<ArrayList<User>> users) {
                        return users.body();
                    }
                }),
                (isOwn?mRepoApi.getMyFollowing(1):mRepoApi.getUserFollowing(username,1))
                    .map(new Func1<Response<ArrayList<User>>, ArrayList<User>>() {
                    @Override
                    public ArrayList<User> call(Response<ArrayList<User>> users) {
                        return users.body();
                    }
                }),
                (isOwn?mRepoApi.getMyRepos(1):mRepoApi.getUserRepos(username,1))
                    .map(new Func1<Response<ArrayList<Repo>>, ArrayList<Repo>>() {
                    @Override
                    public ArrayList<Repo> call(Response<ArrayList<Repo>> repos) {
                        return repos.body();
                    }
                }),
                (isOwn?mRepoApi.getMyStarredRepos(1):mRepoApi.getUserStarredRepos(username,1))
                    .map(new Func1<Response<ArrayList<Repo>>, ArrayList<Repo>>() {
                    @Override
                    public ArrayList<Repo> call(Response<ArrayList<Repo>> repos) {
                        return repos.body();
                    }
                }),
                new Func5<User,ArrayList<User>, ArrayList<User>, ArrayList<Repo>, ArrayList<Repo>, UserDetail>() {
                    @Override
                    public UserDetail call(User user,ArrayList<User> followers, ArrayList<User> following, ArrayList<Repo> repos, ArrayList<Repo> starredRepos) {
                        UserDetail userdatail = new UserDetail();
                        userdatail.setBaseUser(user);
                        userdatail.setFollowerList(followers);
                        userdatail.setFollowingList(following);
                        userdatail.setSelfRepoList(repos);
                        userdatail.setStarredRepoList(starredRepos);
                        return userdatail;
                    }
                }
        );
    }
}
