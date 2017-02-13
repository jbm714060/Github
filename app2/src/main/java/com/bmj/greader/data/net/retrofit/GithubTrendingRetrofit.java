package com.bmj.greader.data.net.retrofit;

import javax.inject.Inject;

import com.bmj.greader.data.net.retrofit.core.ApiEndpoint;
import com.bmj.greader.data.net.retrofit.core.BaseRetrofit;
import okhttp3.OkHttpClient;

/**
 * Created by Administrator on 2016/11/12 0012.
 */
public class GithubTrendingRetrofit extends BaseRetrofit{
    private final String END_POINT = "https://api.github.com/";

    GithubHttpClient mHttpClient;

    @Inject
    public GithubTrendingRetrofit(GithubHttpClient client){
        mHttpClient = client;
    }

    @Override
    public ApiEndpoint getApiEndpoint() {
        return new ApiEndpoint() {
            @Override
            public String getEndpoint() {
                return END_POINT;
            }
        };
    }

    @Override
    public OkHttpClient getHttpClient() {
        return mHttpClient.get();
    }
}
