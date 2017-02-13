package com.bmj.greader.presenter.base;

/**
 * Created by Administrator on 2016/12/4 0004.
 */
public interface OnLoadMoreListener<T> {
    void onStart();
    void onEnd();
    void onSuccess(T t);
    void onError(Throwable e);
}