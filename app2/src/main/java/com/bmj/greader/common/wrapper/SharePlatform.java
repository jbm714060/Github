package com.bmj.greader.common.wrapper;

import android.content.Context;

//import cn.sharesdk.framework.ShareSDK;
//import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * Created by Administrator on 2016/11/10 0010.
 */
public class SharePlatform {
    public static void init(Context context){
        AppLog.d("SharePlatform init");
        //ShareSDK.initSDK(context.getApplicationContext());
    }

    public static void share(Context context){
        AppLog.d("SharePlatform share");

        /*OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        // 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle("Share GithubApp");
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl("https://github.com/mingjunli/GithubApp");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("Github Android Client: https://github.com/mingjunli/GithubApp");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("https://github.com/mingjunli/GithubApp");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("This is an Unofficial / OpenSource Github Client on Android.");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite("GithubApp");
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("https://github.com/mingjunli/GithubApp");

        // 启动分享GUI
        oks.show(context);*/
    }
}
