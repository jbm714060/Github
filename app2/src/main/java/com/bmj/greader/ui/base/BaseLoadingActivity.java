package com.bmj.greader.ui.base;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;

import com.blankj.utilcode.utils.ToastUtils;
import com.bmj.greader.R;
import com.bmj.greader.common.wrapper.AppLog;
import com.bmj.greader.common.wrapper.CustomSnackbar;
import com.bmj.greader.ui.widget.LoadingView;
import com.bmj.mvp.lce.LoadView;

import dmax.dialog.SpotsDialog;

/**
 * Created by Administrator on 2016/11/12 0012.
 */
public abstract class BaseLoadingActivity extends BaseActivity implements LoadView{
    private LoadingView mLoadingView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLoadingView = new LoadingView(this,getLoadingMessage());
    }

    @Override
    public void showLoading() {
        mLoadingView.show();
    }

    @Override
    public void dismissLoading() {
        mLoadingView.dismiss();
    }

    public abstract String getLoadingMessage();

    @Override
    public void error(Throwable e) {
        AppLog.e(e);
        ToastUtils.showLongToast(this,e.getMessage());
    }
}
