package com.bmj.greader.common.util;

import android.content.Context;
import android.content.pm.PackageManager;

/**
 * Created by Administrator on 2016/11/9 0009.
 */
public class AppUtil {
    public static String getVersionName(Context context){
        String versionName = "1.0";
        PackageManager pm = context.getPackageManager();
        try {
            versionName = pm.getPackageInfo(context.getPackageName(), 0).versionName;
        }catch(PackageManager.NameNotFoundException e){
            e.printStackTrace();
        }
        return versionName;
    }
}
