package com.bmj.greader.presenter.main;

import java.util.ArrayList;

import javax.inject.Inject;

import com.bmj.greader.data.api.TrendingApi;
import com.bmj.greader.data.model.Languages;
import com.bmj.greader.data.model.TrendingRepo;
import com.bmj.greader.data.net.TrendingRepoDataSource;
import com.bmj.greader.data.rx.ResponseObserver;
import com.bmj.greader.presenter.base.RxMvpPresenter;
import com.bmj.mvp.lce.LceView;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/11/18 0018.
 */
public class TrendingRepoPresenter extends RxMvpPresenter<LceView<ArrayList<TrendingRepo>>>{

    private TrendingApi mTrendingApi;

    @Inject
    public TrendingRepoPresenter(TrendingRepoDataSource dataSource){
        mTrendingApi = dataSource;
    }

    public void loadTrendingRepo(final String language,final String time){
        mCompositeSubscription.add(
                mTrendingApi.getTrendingRepo(language,time)
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
                .subscribe(new ResponseObserver<ArrayList<TrendingRepo>>() {
                    @Override
                    public void onSuccess(ArrayList<TrendingRepo> trendingRepos) {
                        getMvpView().showContent(trendingRepos);
                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().showError(e);
                    }
                })
        );
    }
}
