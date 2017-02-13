package com.bmj.greader.data.net.retrofit.core;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2016/11/11 0011.
 */
public abstract class BaseRetrofit {
    public Retrofit get(){
        Retrofit.Builder builder = new Retrofit.Builder();
        builder.baseUrl(getApiEndpoint().getEndpoint())
                .client(getHttpClient())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create());
        return builder.build();
    }

    public abstract ApiEndpoint getApiEndpoint();
    public abstract OkHttpClient getHttpClient();
}
