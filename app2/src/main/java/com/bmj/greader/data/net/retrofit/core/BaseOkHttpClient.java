package com.bmj.greader.data.net.retrofit.core;

import java.util.concurrent.TimeUnit;

import com.bmj.greader.BuildConfig;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by Administrator on 2016/11/11 0011.
 */
public abstract class BaseOkHttpClient {
    private final long TIMEOUT_CONNECT = 30*1000;
    public OkHttpClient get(){
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(TIMEOUT_CONNECT, TimeUnit.MILLISECONDS)
                .addInterceptor(new HttpLoggingInterceptor()
                    .setLevel(BuildConfig.DEBUG?HttpLoggingInterceptor.Level.BODY:
                            HttpLoggingInterceptor.Level.NONE));
        builder = customize(builder);
        return builder.build();
    }

    public abstract OkHttpClient.Builder customize(OkHttpClient.Builder builder);
}
