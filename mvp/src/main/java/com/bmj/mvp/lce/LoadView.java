package com.bmj.mvp.lce;

import android.support.annotation.UiThread;

import com.bmj.mvp.MvpView;

/**
 * Created by Administrator on 2016/11/9 0009.
 */
public interface LoadView extends MvpView{
    @UiThread
    void showLoading();

    @UiThread
    void dismissLoading();

    @UiThread
    void error(Throwable e);
}
