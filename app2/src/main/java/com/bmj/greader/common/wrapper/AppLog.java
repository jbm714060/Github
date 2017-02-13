package com.bmj.greader.common.wrapper;


import com.orhanobut.logger.Logger;

import com.bmj.greader.BuildConfig;

/**
 * Created by Administrator on 2016/11/10 0010.
 */
public class AppLog {
    private static final String TAG = "GithubApp";

    public static void Init(){
        Logger.init(TAG);
    }

    public static void i(String message){
        if(BuildConfig.DEBUG)
            Logger.i(message);
    }

    public static void d(String msg)
    {
        if(BuildConfig.DEBUG)
            Logger.d(msg);
    }

    public static void w(String msg){
        if(BuildConfig.DEBUG)
            Logger.w(msg);
    }

    public static void e(String msg){
        Logger.e(msg);
    }

    public static void e(Throwable e)
    {
        Logger.e(e,"");
    }
}
