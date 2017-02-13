package com.bmj.mvp;

import android.support.annotation.UiThread;

/**
 * Created by Administrator on 2016/11/9 0009.
 */
public interface MvpPresenter<V extends MvpView> {
    @UiThread
    void attachView(V view);

    @UiThread
    void detachView();
}
