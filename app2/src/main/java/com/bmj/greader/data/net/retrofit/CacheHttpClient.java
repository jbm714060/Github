package com.bmj.greader.data.net.retrofit;

import android.app.Application;

import java.io.File;
import java.io.IOException;

import javax.inject.Inject;

import com.bmj.greader.common.util.NetworkUtil;
import com.bmj.greader.data.net.retrofit.core.BaseOkHttpClient;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/11/11 0011.
 */
public class CacheHttpClient extends BaseOkHttpClient{
    private final long CACHE_SIZE = 1024*1024*100;

    @Inject
    Application mContext;

    @Inject
    public CacheHttpClient(){}

    @Override
    public OkHttpClient.Builder customize(OkHttpClient.Builder builder) {
        File cacheFile = new File(mContext.getCacheDir(),"github_repo");
        Cache cache = new Cache(cacheFile,CACHE_SIZE);
        builder.cache(cache);
        builder.addNetworkInterceptor(mCacheControlInterceptor);
        return builder;
    }

    private final Interceptor mCacheControlInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            if(!NetworkUtil.isNetworkAvailable(mContext)){
                request = request.newBuilder().cacheControl(CacheControl.FORCE_CACHE).build(); //追加属性
            }

            Response originalResponse = chain.proceed(request);
            if(NetworkUtil.isNetworkAvailable(mContext)){
                String cacheControl = request.cacheControl().toString();
                // Add cache control header for response same as request's while network is available.
                return originalResponse.newBuilder().header("Cache-Control",cacheControl).build();
            }else{
                // Add cache control header for response to FORCE_CACHE while network is not available.
                return originalResponse.newBuilder().
                        header("Cache-Control",CacheControl.FORCE_CACHE.toString())
                        .build();
            }
        }
    };
}
