package com.bmj.greader.ui.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bmj.greader.R;

/**
 * Created by Administrator on 2016/12/12 0012.
 */
public class DrawerUserHeader extends LinearLayout {
    private View rootView;

    public DrawerUserHeader(Context context) {
        super(context);
        init();
    }

    public DrawerUserHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DrawerUserHeader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        rootView = LayoutInflater.from(this.getContext()).inflate(R.layout.drawer_user_header,this,true);
    }

    public DrawerUserHeader withBackground(int resId){
        rootView.setBackgroundResource(resId);
        return this;
    }

    public DrawerUserHeader withName(String name){
        if(!TextUtils.isEmpty(name)) {
            TextView tview = ((TextView) rootView.findViewById(R.id.duh_name));
            tview.setText(name);
            tview.setVisibility(VISIBLE);
        }
        return this;
    }

    public DrawerUserHeader withBio(String bio){
        if(!TextUtils.isEmpty(bio)) {
            TextView tview = ((TextView) rootView.findViewById(R.id.duh_bio));
            tview.setText(bio);
            tview.setVisibility(VISIBLE);
        }
        return this;
    }

    public DrawerUserHeader withOnHeaderClickListener(OnClickListener listener){
        rootView.setOnClickListener(listener);
        return this;
    }

    public ImageView getAvatar(){
        return (ImageView)rootView.findViewById(R.id.duh_avatar);
    }
}
