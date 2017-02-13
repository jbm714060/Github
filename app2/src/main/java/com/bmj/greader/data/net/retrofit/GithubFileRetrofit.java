package com.bmj.greader.data.net.retrofit;

import com.bmj.greader.data.net.retrofit.core.ApiEndpoint;
import com.bmj.greader.data.net.retrofit.core.BaseRetrofit;

import javax.inject.Inject;

import okhttp3.OkHttpClient;

/**
 * Created by Administrator on 2016/12/20 0020.
 */
public class GithubFileRetrofit extends BaseRetrofit {
    public static final String CONTENT_END_POINT = "https://gist.githubusercontent.com/";

    GithubHttpClient mHttpClient;

    @Inject
    public GithubFileRetrofit(GithubHttpClient client){
        mHttpClient = client;
    }

    @Override
    public ApiEndpoint getApiEndpoint() {
        return new ApiEndpoint() {
            @Override
            public String getEndpoint() {
                return CONTENT_END_POINT;
            }
        };
    }

    @Override
    public OkHttpClient getHttpClient() {
        return mHttpClient.get();
    }
}