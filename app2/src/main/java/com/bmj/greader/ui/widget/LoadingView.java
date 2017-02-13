package com.bmj.greader.ui.widget;

import android.content.Context;
import android.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.bmj.greader.R;

import dmax.dialog.SpotsDialog;


/**
 * Created by Administrator on 2016/11/12 0012.
 */
public class LoadingView {
    private AlertDialog mLoadingDialog;

    public LoadingView(Context context,String message){
        mLoadingDialog = new SpotsDialog(context,message);
    }

    public void show(){
        mLoadingDialog.show();
    }

    public void dismiss(){
        mLoadingDialog.dismiss();
    }
}
