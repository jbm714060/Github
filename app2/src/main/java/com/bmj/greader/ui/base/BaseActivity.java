package com.bmj.greader.ui.base;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v7.app.AppCompatActivity;

import com.mikepenz.iconics.context.IconicsContextWrapper;
import com.mikepenz.iconics.context.IconicsLayoutInflater;

import com.bmj.greader.MyApplication;
import com.bmj.greader.dagger2.component.ActivityComponent;
import com.bmj.greader.dagger2.component.DaggerActivityComponent;
import com.bmj.greader.dagger2.module.ActivityModule;

/**
 * Created by Administrator on 2016/11/12 0012.
 */
public class BaseActivity extends AppCompatActivity{
   ActivityComponent mActivityComponent;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        LayoutInflaterCompat.setFactory(getLayoutInflater(),new IconicsLayoutInflater(getDelegate()));
        super.onCreate(savedInstanceState);
    }

    public ActivityComponent getActivityComponent(){
        if(mActivityComponent == null) {
            mActivityComponent = DaggerActivityComponent.builder()
                    .applicationComponent(MyApplication.get(this).getComponent())
                    .activityModule(new ActivityModule(this))
                    .build();
        }
        return mActivityComponent;
    }
}
