package com.bmj.greader.common.wrapper;

import android.content.Context;

import com.tencent.bugly.crashreport.CrashReport;

import com.bmj.greader.BuildConfig;

/**
 * Created by Administrator on 2016/11/10 0010.
 */
public class CrashHelper {
    public static void init(Context context){
        AppLog.d("CrashHelper init");
        CrashReport.initCrashReport(context.getApplicationContext(),"12345678", BuildConfig.DEBUG);
    }

    public static void testCrash(){
        CrashReport.testJavaCrash();
    }

    public static void testANR(){
        CrashReport.testANRCrash();
    }
}
