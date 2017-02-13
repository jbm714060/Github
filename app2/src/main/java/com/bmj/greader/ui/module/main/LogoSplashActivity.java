package com.bmj.greader.ui.module.main;

import android.os.Bundle;

import com.bmj.greader.common.wrapper.AppLog;
import com.bmj.greader.data.pref.AppPref;
import com.bmj.greader.ui.base.BaseActivity;

/**
 * Created by Administrator on 2016/11/15 0015.
 */
public class LogoSplashActivity extends BaseActivity{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppLog.d("trace===LogoSplashActivity onCreate");
        MainActivity.launch(this);
        /*if(AppPref.isFirstRunning(this)){
            IntroduceActivity.launch(this);
        }else{
            MainActivity.launch(this);
        }*/
        finish();
    }
}
