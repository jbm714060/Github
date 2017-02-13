package com.bmj.greader.ui.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.bmj.greader.R;
import com.bmj.mvp.lce.LceView;
/**
 * Created by Administrator on 2016/11/12 0012.
 */
public abstract class BaseLceFragment<M> extends BaseFragment implements LceView<M> {
    private LinearLayout mContentView;
    private RelativeLayout mEmptyView;
    private RelativeLayout mErrorView;
    private RelativeLayout mLoadingView;

   /* @BindView(R.id.content_frame)
    LinearLayout mContentView;

    @BindView(R.id.empty_root_layout)
    RelativeLayout mEmptyView;

    @BindView(R.id.error_root_layout)
    RelativeLayout mErrorView;

    @BindView(R.id.loading_layout)
    RelativeLayout mLoadingView;

    @OnClick(R.id.retry_btn)
    void retryBtnClicked(){
        onRetryBtnClicked();
    }*/

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View baseView = inflater.inflate(R.layout.layout_lce_container,null);
        //ButterKnife.bind(this,baseView);
        mContentView = (LinearLayout) baseView.findViewById(R.id.content_frame);
        mContentView.removeAllViews();
        mContentView.addView(inflater.inflate(getContentView(),null),new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));

        mEmptyView = (RelativeLayout) baseView.findViewById(R.id.empty_root_layout);
        mErrorView = (RelativeLayout)baseView.findViewById(R.id.error_root_layout);
        mErrorView.findViewById(R.id.retry_btn).setOnClickListener(getRetryListener());
        mLoadingView = (RelativeLayout) baseView.findViewById(R.id.loading_layout);
        return baseView;
    }

    public abstract int getContentView();
    public abstract View.OnClickListener getRetryListener();
    //public abstract void onRetryBtnClicked();

    @Override
    public void showLoading() {
        mContentView.setVisibility(View.GONE);
        mEmptyView.setVisibility(View.GONE);
        mErrorView.setVisibility(View.GONE);
        mLoadingView.setVisibility(View.VISIBLE);
    }

    @Override
    public void dismissLoading() {
        mLoadingView.setVisibility(View.GONE);
    }

    @Override
    public void showContent(M data) {
        dismissLoading();
        mEmptyView.setVisibility(View.GONE);
        mErrorView.setVisibility(View.GONE);
        mContentView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showError(Throwable e) {
        dismissLoading();
        mContentView.setVisibility(View.GONE);
        mEmptyView.setVisibility(View.GONE);
        mErrorView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showEmpty() {
        dismissLoading();
        mContentView.setVisibility(View.GONE);
        mErrorView.setVisibility(View.GONE);
        mEmptyView.setVisibility(View.VISIBLE);
    }
}
