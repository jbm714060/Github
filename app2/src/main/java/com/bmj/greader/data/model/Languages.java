package com.bmj.greader.data.model;

import android.support.annotation.IntDef;

import com.bmj.greader.R;

/**
 * Created by Administrator on 2016/12/4 0004.
 */
public class Languages {
    public static final int LANG_JAVA = 1;
    public static final int LANG_JS = 2;
    public static final int LANG_PHP=3;
    public static final int LANG_PYTHON = 4;
    public static final int LANG_OC = 5;
    public static final int LANG_HTML = 6;
    public static final int LANG_SWIFT = 7;
    public static final int LANG_UNKNOWN = 8;

    @IntDef({
            LANG_JAVA,
            LANG_JS,
            LANG_PHP,
            LANG_PYTHON,
            LANG_OC,
            LANG_HTML,
            LANG_SWIFT,
            LANG_UNKNOWN
    })
    public @interface LanguageType{}

    public static String langType2String(@LanguageType int langType){
        switch (langType){
            case LANG_JAVA:
                return "Java";
            case LANG_JS:
                return "JavaScript";
            case LANG_PHP:
                return "Php";
            case LANG_PYTHON:
                return "Python";
            case LANG_OC:
                return "Objective-c";
            case LANG_HTML:
                return "Html";
            case LANG_SWIFT:
                return "Swift";
            default:
                return "";
        }
    }

    public static @LanguageType int int2LangType(int value){
        switch (value){
            case 1:
                return LANG_JAVA;
            case 2:
                return LANG_JS;
            case 3:
                return LANG_PHP;
            case 4:
                return LANG_PYTHON;
            case 5:
                return LANG_OC;
            case 6:
                return LANG_HTML;
            case 7:
                return LANG_SWIFT;
            default:
                return LANG_UNKNOWN;
        }
    }

    /**
     *
     * @param langType
     * @return color resID
     */
    public static int getLangColor(@LanguageType int langType){
        switch (langType){
            case LANG_JAVA:
                return R.color.md_amber_500;
            case LANG_JS:
                return R.color.md_indigo_500;
            case LANG_PHP:
                return R.color.md_green_500;
            case LANG_PYTHON:
                return R.color.md_purple_500;
            case LANG_OC:
                return R.color.md_blue_grey_500;
            case LANG_HTML:
                return R.color.md_pink_500;
            case LANG_SWIFT:
                return R.color.md_teal_500;
            default:
                return R.color.md_amber_500;
        }
    }

    /**
     *
     * @param lang language string
     * @return color resID
     */
    public static int getLangColor(String lang)
    {
        if(lang == null)
            return R.color.md_black_1000;

        lang = lang.toLowerCase();
        if(lang.equals("java"))
            return R.color.md_amber_500;
        if(lang.equals("python"))
            return R.color.md_purple_500;
        if(lang.equals("objective-c"))
            return R.color.md_blue_grey_500;
        if(lang.equals("swift"))
            return R.color.md_teal_500;
        if(lang.equals("javascript"))
            return R.color.md_indigo_500;
        if(lang.equals("html"))
            return R.color.md_pink_500;
        if(lang.equals("php"))
            return R.color.md_green_500;
        if(lang.equals("c"))
            return R.color.md_red_500;
        if(lang.equals("c#"))
            return R.color.md_deep_purple_800;
        if(lang.equals("c++"))
            return R.color.md_indigo_400;
        if(lang.equals("objective-c++"))
            return R.color.md_brown_900;
        return R.color.md_brown_900;
    }
}
