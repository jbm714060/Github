package com.bmj.greader.common.util;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.typeface.IIcon;

/**
 * Created by Administrator on 2016/11/9 0009.
 */
public class IconicUtil {
    public static Drawable getSmallIcon(Context context,IIcon icon)
    {
        return new IconicsDrawable(context).icon(icon).sizeDp(24);
    }
}
