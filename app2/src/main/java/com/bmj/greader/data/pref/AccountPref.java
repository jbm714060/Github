package com.bmj.greader.data.pref;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.gson.Gson;

import com.bmj.greader.data.model.User;
import com.bmj.greader.ui.module.account.LoginActivity;

/**
 * Created by Administrator on 2016/11/11 0011.
 */
public class AccountPref {
    private static final String KEY_LOGIN_TOKEN = "login_token";
    private static final String KEY_LOGON_USER = "logon_user";

    private static SharedPreferences getPreference(Context context){
        return context.getApplicationContext().getSharedPreferences("accountPreference.xml",Context.MODE_PRIVATE);
    }

    public static void saveLoginToken(Context context,String loginToken){
        getPreference(context).edit().putString(KEY_LOGIN_TOKEN,loginToken).apply();
    }

    public static String getLogonToken(Context context){
        return getPreference(context).getString(KEY_LOGIN_TOKEN,"");
    }

    public static void saveLogonUser(Context context,User user ){
        String userJson = new Gson().toJson(user);
        getPreference(context).edit().putString(KEY_LOGON_USER,userJson).apply();
    }

    public static void removeLogonUser(Context context ){
        getPreference(context).edit().remove(KEY_LOGON_USER).apply();
    }

    public static User getLogonUser(Context context){
        String userJson = getPreference(context).getString(KEY_LOGON_USER,"");
        if(!TextUtils.isEmpty(userJson)){
            return new Gson().fromJson(userJson,User.class);
        }
        return null;
    }

    public static boolean isLogon(Context context){
        return !TextUtils.isEmpty(getLogonToken(context)) && getLogonUser(context) != null;
    }

    public static boolean checkLogon(Context context) {
        if (!isLogon(context)) {
            LoginActivity.launch(context);
            return false;
        }

        return true;
    }

    public static boolean isSelf(Context context, String username) {
        User user = getLogonUser(context);
        return user != null
                && !TextUtils.isEmpty(username)
                && username.equals(user.getLogin());
    }
}
