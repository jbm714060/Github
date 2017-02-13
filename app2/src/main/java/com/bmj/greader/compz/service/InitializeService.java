package com.bmj.greader.compz.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.mikepenz.materialdrawer.util.AbstractDrawerImageLoader;
import com.mikepenz.materialdrawer.util.DrawerImageLoader;

import com.bmj.greader.common.wrapper.AppLog;
import com.bmj.greader.common.wrapper.CrashHelper;
import com.bmj.greader.common.wrapper.FeedbackPlatform;
import com.bmj.greader.common.wrapper.ImageLoader;
import com.bmj.greader.common.wrapper.PushPlatform;
import com.bmj.greader.common.wrapper.SharePlatform;

import java.io.File;

/**
 * Created by Administrator on 2016/11/10 0010.
 */
public class InitializeService extends IntentService{
    public static final String ACTION = "nine.november.com.InitializeService";

    public static void start(Context context){
        Intent intent = new Intent(context,InitializeService.class);
        context.startService(intent);
    }

    public InitializeService(){
        super("InitializeService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if(intent != null){
            String action = intent.getAction();
            if(ACTION.equals(action)){
                performInit();
            }
        }
    }

    private void performInit(){
        AppLog.d("performInit begin:"+ System.currentTimeMillis());
        DrawerImageLoader.init(new AbstractDrawerImageLoader() {
            @Override
            public void set(ImageView imageView, Uri uri, Drawable placeholder) {
                ImageLoader.loadWithCircle(getApplicationContext(),uri,imageView);
            }

            @Override
            public void cancel(ImageView imageView) {
                Glide.clear(imageView);
            }
        });
        // init crash helper
        CrashHelper.init(this.getApplicationContext());

        // init Push
        PushPlatform.init(this.getApplicationContext());

        // init Feedback
        FeedbackPlatform.init(this.getApplication());

        // init Share
        SharePlatform.init(this.getApplicationContext());

        GlideBuilder builder = new GlideBuilder(this.getApplicationContext());
        //File cacheDir = this.getApplicationContext().getExternalCacheDir();
        int diskCacheSize = 1024*1024*30;
        builder.setDiskCache(new InternalCacheDiskCacheFactory(this.getApplicationContext(),
                "glide",diskCacheSize));

        AppLog.d("performInit end:" + System.currentTimeMillis());
    }
}
