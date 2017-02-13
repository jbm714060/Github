package com.bmj.greader.presenter.base;

/**
 * Created by Administrator on 2016/12/4 0004.
 */
public interface LoadMoreInterface<T> {
    void setOnLoadMoreListener(OnLoadMoreListener<T> onLoadMoreListener);
    void loadMore();
}
