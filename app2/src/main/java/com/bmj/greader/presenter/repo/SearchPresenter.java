package com.bmj.greader.presenter.repo;

import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.Map;

import javax.inject.Inject;

import com.bmj.greader.data.api.RepoApi;
import com.bmj.greader.data.model.Repo;
import com.bmj.greader.data.model.User;
import com.bmj.greader.data.net.pagination.PaginationLinks;
import com.bmj.greader.data.net.response.SearchRepoResp;
import com.bmj.greader.data.net.response.SearchUserResp;
import com.bmj.greader.data.rx.ResponseObserver;
import com.bmj.greader.presenter.base.RxMvpPresenter;
import com.bmj.greader.ui.module.repo.view.SearchView;

import retrofit2.Response;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/11/21 0021.
 */
public class SearchPresenter extends RxMvpPresenter<SearchView>{
    private RepoApi mRepoApi;
    private PaginationLinks pageLinks;
    private String mQueryString;

    @Inject
    public SearchPresenter(RepoApi repoApi){
        mRepoApi = repoApi;
    }

    //key Log.i("showgist",text);
    //type
    //users:  repos followers location
    //repos:   forks stars language created user
    public void search(final Map<String,String> options){
        if(TextUtils.isEmpty(options.get("key"))) {
            getMvpView().error(new Throwable("please input content to search"));
            return;
        }

        StringBuilder builder = new StringBuilder();
        builder.append(options.get("key"));
        if(options.get("type").equals("Users")){
            builder = appendQ(builder,"repos",options);
            builder = appendQ(builder,"followers",options);
            builder = appendQ(builder,"location",options);

            mQueryString = builder.toString();
            searchUser(mQueryString,1);
        }else{
            builder = appendQ(builder,"forks",options);
            builder = appendQ(builder,"stars",options);
            builder = appendQ(builder,"language",options);
            builder = appendQ(builder,"created",options);
            builder = appendQ(builder,"user",options);

            mQueryString = builder.toString();
            searchRepo(mQueryString,1);
        }
    }

    public StringBuilder appendQ(StringBuilder builder,String key,Map<String,String> options){

        if(key.equals("language") && options.get(key).equals("ALL")){
            return builder;
        }

        if(!TextUtils.isEmpty(options.get(key))){
            String [] keyValues = options.get(key).split(",");
            for(String value:keyValues){
                builder.append(" ").append(key).append(":");
                builder.append(value);
            }

            if(key.equals("user"))
                builder.append(" fork:true");
        }
        return builder;
    }

    public void searchRepo(String q,final int page){
        mCompositeSubscription.add(
            mRepoApi.searchRepo(q,page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                    .map(new Func1<Response<SearchRepoResp>, SearchRepoResp>() {
                        @Override
                        public SearchRepoResp call(Response<SearchRepoResp> searchResultRespResponse) {
                            pageLinks = new PaginationLinks(searchResultRespResponse.headers().get("Link"));
                            return searchResultRespResponse.body();
                        }
                    })
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        if(page == 1)
                            getMvpView().showLoading();
                    }
                })
                .doOnTerminate(new Action0() {
                    @Override
                    public void call() {
                        if(page == 1)
                            getMvpView().dismissLoading();
                    }
                })
                .subscribe(new ResponseObserver<SearchRepoResp>() {
                    @Override
                    public void onSuccess(SearchRepoResp resp) {
                        getMvpView().showSearchRepo(resp,page);
                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().error(e);
                    }
                })
        );
    }

    public void searchUser(String q,final int page) {
        mCompositeSubscription.add(
                mRepoApi.searchUser(q, page)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .map(new Func1<Response<SearchUserResp>,SearchUserResp>() {
                            @Override
                            public SearchUserResp call(Response<SearchUserResp> searchResultRespResponse) {
                                pageLinks = new PaginationLinks(searchResultRespResponse.headers().get("Link"));
                                return searchResultRespResponse.body();
                            }
                        })
                        .doOnSubscribe(new Action0() {
                            @Override
                            public void call() {
                                if(page == 1)
                                    getMvpView().showLoading();
                            }
                        })
                        .doOnTerminate(new Action0() {
                            @Override
                            public void call() {
                                if(page == 1)
                                    getMvpView().dismissLoading();
                            }
                        })
                        .subscribe(new ResponseObserver<SearchUserResp>() {
                            @Override
                            public void onSuccess(SearchUserResp resp) {
                                getMvpView().showSearchUser(resp,page);
                            }

                            @Override
                            public void onError(Throwable e) {
                                getMvpView().error(e);
                            }
                        })
        );
    }

    public void loadMoreUsers(){
        if(getPage() > 1)
            searchUser(mQueryString,getPage());
    }

    public void loadMoreRepos(){
        if(getPage() > 1)
            searchRepo(mQueryString,getPage());
    }

    public int getPage(){
        if(pageLinks == null)
            return 1;
        else if(pageLinks.getNextPage() > 1 && pageLinks.getNextPage() <= pageLinks.getLastPage())
            return pageLinks.getNextPage();
        else
            return -1;
    }

    public int getPageSize(){
        return pageLinks.getPageSize();
    }
}
