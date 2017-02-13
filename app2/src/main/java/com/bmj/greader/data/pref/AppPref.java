package com.bmj.greader.data.pref;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Administrator on 2016/11/11 0011.
 */
public class AppPref {
    private static final String KEY_IS_FIRST_RUNNING = "is_first_running";
    private static final String KEY_TRENDING_LANGUAGE="trending_language";
    private static final String KEY_TRENDING_TIME="trending_time";
    private static final String KEY_SAVEFILE_DIR="last_save_dir";

    private static SharedPreferences getPreferences(Context context){
        return context.getApplicationContext().getSharedPreferences("appPreference.xml",Context.MODE_PRIVATE);
    }

    public static void setAlreadyRun(Context context){
        getPreferences(context).edit().putBoolean(KEY_IS_FIRST_RUNNING,false).apply();
    }

    public static boolean isFirstRunning(Context context){
        return getPreferences(context).getBoolean(KEY_IS_FIRST_RUNNING,true);
    }

    public static void setTrendingLanguage(Context context,int selectedIndex){
        getPreferences(context).edit().putInt(KEY_TRENDING_LANGUAGE,selectedIndex).apply();
    }

    public static int getTrendingLanguage(Context context){
        return getPreferences(context).getInt(KEY_TRENDING_LANGUAGE,0);
    }

    public static void setTrendingTime(Context context,int selectedIndex){
        getPreferences(context).edit().putInt(KEY_TRENDING_TIME,selectedIndex).apply();
    }

    public static int getTrendingTime(Context context){
        return getPreferences(context).getInt(KEY_TRENDING_TIME,0);
    }

    public static void setLastSaveFileDir(Context context,String path){
        getPreferences(context).edit().putString(KEY_SAVEFILE_DIR,path).apply();
    }

    public static String getLastSaveFileDir(Context context){
        return getPreferences(context).getString(KEY_SAVEFILE_DIR,"");
    }
}
