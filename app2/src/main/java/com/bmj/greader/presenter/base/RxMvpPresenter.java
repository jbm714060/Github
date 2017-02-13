package com.bmj.greader.presenter.base;

import com.bmj.mvp.BaseMvpPresenter;
import com.bmj.mvp.MvpView;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Administrator on 2016/11/14 0014.
 */
public class RxMvpPresenter<V extends MvpView> extends BaseMvpPresenter<V> {

    protected CompositeSubscription mCompositeSubscription;

    @Override
    public void attachView(V view) {
        super.attachView(view);
        mCompositeSubscription = new CompositeSubscription();
    }

    @Override
    public void detachView() {
        super.detachView();
        mCompositeSubscription.clear();
        mCompositeSubscription = null;
    }
}
