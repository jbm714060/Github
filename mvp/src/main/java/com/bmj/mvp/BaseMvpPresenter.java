package com.bmj.mvp;

/**
 * Created by Administrator on 2016/11/9 0009.
 */
public class BaseMvpPresenter<V extends MvpView> implements MvpPresenter<V>{
    V mMvpView;

    @Override
    public void attachView(V view) {
        mMvpView = view;
    }

    @Override
    public void detachView() {
        mMvpView = null;
    }

    public boolean isViewAttached(){
        if(mMvpView != null)
            return true;
        else
            return false;
    }

    public V getMvpView(){
        return mMvpView;
    }

    public void checkViewAttached()throws MvpViewNoAttachedException
    {
        if(!isViewAttached())
            throw new MvpViewNoAttachedException();
    }

    public static class MvpViewNoAttachedException extends RuntimeException{
        public MvpViewNoAttachedException(){
            super("Please call Presenter.attachView(MvpView) before" +
                    " requesting data to the Presenter");
        }
    }
}
