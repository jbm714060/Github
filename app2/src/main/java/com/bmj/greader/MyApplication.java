package com.bmj.greader;

import android.Manifest;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.bmj.greader.common.wrapper.AppLog;
import com.bmj.greader.compz.service.InitializeService;
import com.bmj.greader.dagger2.component.ApplicationComponent;
import com.bmj.greader.dagger2.component.DaggerApplicationComponent;
import com.bmj.greader.dagger2.module.ApplicationModule;

/**
 * Created by Administrator on 2016/11/10 0010.
 */
public class MyApplication extends Application{
    ApplicationComponent mApplicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        AppLog.Init();
        InitializeService.start(this);
    }

    public static MyApplication get(Context context){
        return (MyApplication) context.getApplicationContext();
    }

    public ApplicationComponent getComponent(){
        if(mApplicationComponent == null){
            mApplicationComponent = DaggerApplicationComponent.builder()
                    .applicationModule(new ApplicationModule(this))
                    .build();
        }
        return mApplicationComponent;
    }

    public void setComponent(ApplicationComponent component){
        mApplicationComponent = component;
    }
}
