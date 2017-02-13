package com.bmj.greader.data.rx;

import com.bmj.greader.common.wrapper.AppLog;
import rx.Subscriber;

/**
 * Created by Administrator on 2016/11/12 0012.
 */
public abstract class ResponseObserver<T> extends Subscriber<T> {

    @Override
    public void onCompleted() {
        AppLog.d("onCompleted");
    }

    @Override
    public void onNext(T t) {
        AppLog.d("onNext");
        onSuccess(t);
    }

    public abstract void onSuccess(T t);
}
