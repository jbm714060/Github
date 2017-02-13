package com.bmj.greader.data.net.retrofit;

import android.app.Application;
import android.util.Log;

import java.io.IOException;

import javax.inject.Inject;

import com.bmj.greader.data.pref.AccountPref;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class GithubHttpClient extends CacheHttpClient{
    @Inject
    Application application;

    @Inject
    public GithubHttpClient(){

    }

    public String getAcceptHeader(){
        return "application/vnd.github.v3.json";
    }

    @Override
    public OkHttpClient.Builder customize(OkHttpClient.Builder builder) {
        builder =  super.customize(builder);
        builder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                Request.Builder requestBuilder= original.newBuilder()
                        .header("Accept",getAcceptHeader())
                        .header("User-Agent","RGithub");  //User-Agent访问者的身份标识 GithubApp
                if(AccountPref.isLogon(application)){
                    requestBuilder.header("Authorization","token "+AccountPref.getLogonToken(application));
                }

                String desc = original.header("Des-bmj");
                Log.i("GithubHttpClient ",original.url().toString());

                Request request = requestBuilder.build();
                Response response = chain.proceed(request);
                return response;
            }
        });
        return builder;
    }
}
