package com.bmj.greader.presenter.gist;

import com.bmj.greader.common.wrapper.AppLog;
import com.bmj.greader.data.model.User;
import com.bmj.greader.data.model.gist.Gist;
import com.bmj.greader.data.model.Repo;
import com.bmj.greader.data.net.pagination.PaginationLinks;
import com.bmj.greader.data.net.services.GistService;
import com.bmj.greader.data.rx.ResponseObserver;
import com.bmj.greader.presenter.base.OnLoadMoreListener;
import com.bmj.greader.presenter.base.RxMvpPresenter;
import com.bmj.greader.ui.module.repo.view.GistView;
import com.bmj.greader.ui.module.repo.view.SearchView;

import java.util.ArrayList;

import javax.inject.Inject;

import okhttp3.ResponseBody;
import retrofit2.Response;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/12/19 0019.
 */
public class GistPresenter extends RxMvpPresenter<GistView>{
    private GistService mGistService;
    private PaginationLinks mLinks;
    private OnLoadMoreListener<ArrayList<Gist>> loadMoreListener;
    private String mUserLogin;
    private boolean mIsSelf;
    private int gistType = 0;

    @Inject
    public GistPresenter(GistService gistService){
        mGistService = gistService;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener<ArrayList<Gist>> listener){
        loadMoreListener = listener;
    }

    public void getUsersGists(String userLogin,boolean isSelf){
        mUserLogin = userLogin;
        mIsSelf = isSelf;
        gistType = 0;

        Observable<Response<ArrayList<Gist>>> observable;
        if(isSelf)
            observable = mGistService.getOwnGists(1);
        else
            observable = mGistService.getUserGists(userLogin,1);

        mCompositeSubscription.add(
                observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<Response<ArrayList<Gist>>, ArrayList<Gist>>() {
                    @Override
                    public ArrayList<Gist> call(Response<ArrayList<Gist>> arrayListResponse) {
                        mLinks = new PaginationLinks(arrayListResponse.headers().get("Link"));
                        return arrayListResponse.body();
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
                .subscribe(new ResponseObserver<ArrayList<Gist>>() {
                    @Override
                    public void onSuccess(ArrayList<Gist> arrayList) {
                        getMvpView().showGists(arrayList);
                    }

                    @Override
                    public void onError(Throwable e) {
                        AppLog.e(e);
                    }
                })
        );
    }

    public void getStarredGists(){
        gistType = 1;
        mCompositeSubscription.add(
                mGistService.getOwnStarredGists(1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<Response<ArrayList<Gist>>, ArrayList<Gist>>() {
                    @Override
                    public ArrayList<Gist> call(Response<ArrayList<Gist>> arrayListResponse) {
                        mLinks = new PaginationLinks(arrayListResponse.headers().get("Link"));
                        return arrayListResponse.body();
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
                .subscribe(new ResponseObserver<ArrayList<Gist>>() {
                    @Override
                    public void onSuccess(ArrayList<Gist> arrayList) {
                        getMvpView().showGists(arrayList);
                    }

                    @Override
                    public void onError(Throwable e) {
                        AppLog.e(e);
                    }
                })
        );
    }

    public void loadMoreUsers(){
        if(getPage() > 1){
            Observable<Response<ArrayList<Gist>>> observable;

            if(gistType == 1)
                observable = mGistService.getOwnStarredGists(getPage());
            else if(mIsSelf)
                observable = mGistService.getOwnGists(getPage());
            else
                observable = mGistService.getUserGists(mUserLogin,getPage());

            mCompositeSubscription.add(
                observable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                        .map(new Func1<Response<ArrayList<Gist>>, ArrayList<Gist>>() {
                            @Override
                            public ArrayList<Gist> call(Response<ArrayList<Gist>> arrayListResponse) {
                                mLinks = new PaginationLinks(arrayListResponse.headers().get("Link"));
                                return arrayListResponse.body();
                            }
                        })
                        .subscribe(new ResponseObserver<ArrayList<Gist>>() {
                            @Override
                            public void onSuccess(ArrayList<Gist> arrayList) {
                                loadMoreListener.onSuccess(arrayList);
                            }

                            @Override
                            public void onError(Throwable e) {
                                AppLog.e(e);
                                loadMoreListener.onError(e);
                            }
                        })
            );
        }else{
            loadMoreListener.onError(new Throwable("no more page"));
        }
    }

    public int getPage(){
        if(mLinks == null)
            return 1;
        else if (mLinks.getNextPage() !=0 && mLinks.getNextPage() <= mLinks.getLastPage()) {
            return mLinks.getNextPage();
        }
        else
            return -1;
    }

    public int getPageSize(){
        return mLinks.getPageSize();
    }

    public void starGist(String gistId, boolean isStar, final int position){
        if(!isStar) {
            mCompositeSubscription.add(
                mGistService.starGist(gistId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new ResponseObserver<Response<ResponseBody>>() {
                        @Override
                        public void onSuccess(Response<ResponseBody> response) {
                            getMvpView().isStarSuccessfully(response != null && response.code() == 204, position);
                        }

                        @Override
                        public void onError(Throwable e) {
                            getMvpView().error(e);
                        }
                    })
            );
        }else{
            mCompositeSubscription.add(
                mGistService.unstarGist(gistId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new ResponseObserver<Response<ResponseBody>>() {
                        @Override
                        public void onSuccess(Response<ResponseBody> response) {
                            getMvpView().isUnstarSuccessfully(response != null && response.code() == 204, position);
                        }

                        @Override
                        public void onError(Throwable e) {
                            getMvpView().error(e);
                        }
                    })
            );
        }
    }
}
