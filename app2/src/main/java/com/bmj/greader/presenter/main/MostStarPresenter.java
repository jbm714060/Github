package com.bmj.greader.presenter.main;

import java.util.ArrayList;

import javax.inject.Inject;

import com.bmj.greader.data.api.RepoApi;
import com.bmj.greader.data.model.Repo;
import com.bmj.greader.data.net.pagination.PaginationLinks;
import com.bmj.greader.data.net.response.SearchRepoResp;
import com.bmj.greader.data.rx.ResponseObserver;
import com.bmj.greader.presenter.base.LoadMoreInterface;
import com.bmj.greader.presenter.base.OnLoadMoreListener;
import com.bmj.greader.presenter.base.RxMvpPresenter;
import com.bmj.mvp.lce.LceView;

import retrofit2.Response;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/11/18 0018.
 */
public class MostStarPresenter extends RxMvpPresenter<LceView<ArrayList<Repo>>>
        implements LoadMoreInterface<ArrayList<Repo>>
{
    private RepoApi mRepoApi;
    private PaginationLinks links;
    private @RepoApi.MostStarsType int searchType;

    @Inject
    public MostStarPresenter(RepoApi repoApi){
        mRepoApi = repoApi;
    }

    public void loadMostStars(@RepoApi.MostStarsType int type){
        searchType = type;
        mCompositeSubscription.add(
                mRepoApi.getTop30StarsRepo(type,1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<Response<SearchRepoResp>, ArrayList<Repo>>() {
                    @Override
                    public ArrayList<Repo> call(Response<SearchRepoResp> response) {
                        links = new PaginationLinks(response.headers().get("Link"));
                        return response.body().getItems();
                    }
                })
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
                .subscribe(new ResponseObserver<ArrayList<Repo>>() {
                    @Override
                    public void onSuccess(ArrayList<Repo> repos) {
                        getMvpView().showContent(repos);
                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().showError(e);
                    }
                })
        );
    }

    private OnLoadMoreListener<ArrayList<Repo>> loadMoreListener;
    @Override
    public void setOnLoadMoreListener(OnLoadMoreListener<ArrayList<Repo>> onLoadMoreListener) {
        loadMoreListener = onLoadMoreListener;
    }

    @Override
    public void loadMore(){
        if(loadMoreListener == null)
            return;
        if(getPage() > 0){
            mCompositeSubscription.add(
                mRepoApi.getTop30StarsRepo(searchType,getPage())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .map(new Func1<Response<SearchRepoResp>, ArrayList<Repo>>() {
                        @Override
                        public ArrayList<Repo> call(Response<SearchRepoResp> response) {
                            links = new PaginationLinks(response.headers().get("Link"));
                            return response.body().getItems();
                        }
                    })
                    .doOnSubscribe(new Action0() {
                        @Override
                        public void call() {
                            loadMoreListener.onStart();
                        }
                    })
                    .doOnTerminate(new Action0() {
                        @Override
                        public void call() {
                            loadMoreListener.onEnd();
                        }
                    })
                    .subscribe(new ResponseObserver<ArrayList<Repo>>() {
                        @Override
                        public void onSuccess(ArrayList<Repo> repos) {
                            loadMoreListener.onSuccess(repos);
                        }

                        @Override
                        public void onError(Throwable e) {
                            loadMoreListener.onError(e);
                        }
                    })
            );
        }
        else {
            loadMoreListener.onError(new Throwable("no more data"));
        }
    }

    private int getPage(){
        if(links.getNextPage() !=0 && links.getNextPage() <= links.getLastPage())
            return links.getNextPage();
        else
            return -1;
    }

    public int getPageSize(){
        return links.getPageSize();
    }
}
