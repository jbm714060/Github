package com.bmj.greader.presenter.gist;

import android.net.Uri;
import android.util.Log;

import com.bmj.greader.data.model.gist.GistFile;
import com.bmj.greader.data.net.retrofit.GithubFileRetrofit;
import com.bmj.greader.data.net.services.FileDownloadService;
import com.bmj.greader.data.net.services.GistService;
import com.bmj.greader.data.rx.ResponseObserver;
import com.bmj.greader.presenter.base.RxMvpPresenter;
import com.bmj.mvp.lce.LceView;

import java.net.URI;

import javax.inject.Inject;

import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.http.Url;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/12/20 0020.
 */
public class GistFilePresenter extends RxMvpPresenter<LceView<String>>{

    private FileDownloadService mFileService;

    @Inject
    public GistFilePresenter(FileDownloadService gistService){
        mFileService = gistService;
    }

    public void downloadGistFile(String url){
        int index = url.indexOf(GithubFileRetrofit.CONTENT_END_POINT);
        url = url.replace(GithubFileRetrofit.CONTENT_END_POINT,"");
        URI uri = URI.create(url);

        mCompositeSubscription.add(
                mFileService.getGistFile(uri.getRawPath())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        if(getMvpView() != null)
                            getMvpView().showLoading();
                    }
                })
                .doOnTerminate(new Action0() {
                    @Override
                    public void call() {
                        if(getMvpView() != null)
                            getMvpView().dismissLoading();
                    }
                })
                .subscribe(new ResponseObserver<ResponseBody>() {
                    @Override
                    public void onSuccess(ResponseBody reponse) {
                        getMvpView().showContent(reponse.toString());
                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().showError(e);
                    }
                })
        );
    }
}
