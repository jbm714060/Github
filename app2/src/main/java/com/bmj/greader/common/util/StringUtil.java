package com.bmj.greader.common.util;

import android.text.TextUtils;
import android.util.Base64;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2016/11/10 0010.
 */
public class StringUtil {
    public static String replaceAllBlank(String primary){
        if(TextUtils.isEmpty(primary))
            return "";
        Pattern p = Pattern.compile("\\s*|\t|\r|\n");
        Matcher m = p.matcher(primary);
        return m.replaceAll("");
    }

    public static String trimNewLine(String primary)
    {
        if(TextUtils.isEmpty(primary))
            return "";
        String str = primary.trim();
        Pattern p = Pattern.compile("\t|\r|\n");
        Matcher m = p.matcher(str);
        return m.replaceAll("");
    }

    public static String base64Decode(String primary)
    {
        if(TextUtils.isEmpty(primary))
            return "";
        return new String(Base64.decode(primary,0));
    }
}
