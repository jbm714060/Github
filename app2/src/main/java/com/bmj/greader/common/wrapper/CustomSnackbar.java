package com.bmj.greader.common.wrapper;

import android.support.annotation.ColorRes;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bmj.greader.R;

/**
 * Created by Administrator on 2016/12/5 0005.
 */
public class CustomSnackbar{
    private Snackbar snackbar;
    private CustomSnackbar(){}

    public static CustomSnackbar make(@NonNull View view,@NonNull CharSequence text,int duration){
        CustomSnackbar customSnackbar = new CustomSnackbar();
        customSnackbar.snackbar = Snackbar.make(view,text,duration);
        return customSnackbar;
    }

    public Snackbar setTextColor(@ColorRes int colorResID){
        View snackbarLayout = this.snackbar.getView();
        TextView text = (TextView)snackbarLayout.findViewById(R.id.snackbar_text);
        text.setTextColor(ContextCompat.getColor(text.getContext(),colorResID));
        return this.snackbar;
    }
}
