package com.bmj.greader.dagger2.module;

import android.app.Activity;

import android.content.Context;
import android.support.v4.app.Fragment;


import dagger.Module;
import dagger.Provides;
import com.bmj.greader.dagger2.ActivityContext;

/**
 * Created by Administrator on 2016/11/12 0012.
 */
@Module
public class ActivityModule {
    private Activity mActivity;
    public ActivityModule(Activity activity){
        mActivity = activity;
    }

    public ActivityModule(Fragment fragment){
        mActivity = fragment.getActivity();
    }

    @Provides
    Activity provideActivity(){
        return mActivity;
    }

    @Provides
    @ActivityContext
    Context provideContext(){
        return mActivity;
    }
}
