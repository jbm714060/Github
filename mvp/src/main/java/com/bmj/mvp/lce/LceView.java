package com.bmj.mvp.lce;

import android.support.annotation.UiThread;

import com.bmj.mvp.MvpView;

/**
 * Created by Administrator on 2016/11/9 0009.
 */
public interface LceView<M> extends MvpView{
    @UiThread
    void showLoading();

    @UiThread
    void dismissLoading();

    @UiThread
    void showContent(M data);

    @UiThread
    void showError(Throwable e);

    @UiThread
    void showEmpty();
}
