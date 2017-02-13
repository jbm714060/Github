package com.bmj.greader.common.util;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by Administrator on 2016/11/10 0010.
 */
public class InputMethodUtil {
    public static void toggleSoftInput(Context context){
        InputMethodManager im = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        im.toggleSoftInput(0,InputMethodManager.SHOW_FORCED);
    }

    public static boolean showSoftInput(View view)
    {
        InputMethodManager im = (InputMethodManager)view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        return im.showSoftInput(view,InputMethodManager.SHOW_FORCED);
    }

    public static boolean showSoftInput(Activity activity)
    {
        View view = activity.getCurrentFocus();
        if(view != null)
        {
            InputMethodManager im = (InputMethodManager)view.getContext().
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            return im.showSoftInput(view,InputMethodManager.SHOW_FORCED);
        }else{
            return false;
        }
    }

    public static boolean hideSoftInput(View view)
    {
        InputMethodManager im = (InputMethodManager)view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        return im.hideSoftInputFromWindow(view.getWindowToken(),0);
    }

    public static boolean hideSoftInput(Activity activity){
        View view = activity.getCurrentFocus();
        if(view != null) {
            InputMethodManager im = (InputMethodManager) view.getContext().
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            return im.hideSoftInputFromWindow(view.getWindowToken(),0);
        }else{
            return false;
        }
    }

    public static boolean isActive(Context context)
    {
        InputMethodManager im = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        return im.isActive();
    }
}
