package com.bmj.greader.data.net.retrofit;

import android.text.TextUtils;
import android.util.Base64;

import java.io.IOException;

import javax.inject.Inject;

import com.bmj.greader.data.net.retrofit.core.ApiEndpoint;
import com.bmj.greader.data.net.retrofit.core.BaseOkHttpClient;
import com.bmj.greader.data.net.retrofit.core.BaseRetrofit;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/11/11 0011.
 */
public class GithubAuthRetrofit extends BaseRetrofit{
    private final String END_POINT="https://api.github.com/";
    private String username;
    private String password;

    @Inject
    public GithubAuthRetrofit(){

    }

    public void setAuthInfo(String username,String password){
        this.username = username;
        this.password = password;
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
        return new AuthHttpClient(username,password).get();
    }

    private class AuthHttpClient extends BaseOkHttpClient{
        private String username;
        private String password;

        public AuthHttpClient(String username,String password)
        {
            this.username = username;
            this.password = password;
        }

        @Override
        public OkHttpClient.Builder customize(OkHttpClient.Builder builder) {
            if(!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)){
                builder.addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        //The "basic" authentication scheme is based on the model that the
                        //client must authenticate itself with a user-ID and a password for
                        //each realm.
                        String userCredentials = username+":"+password;
                        String basicAuth = "Basic "+new String(Base64.encode(userCredentials.getBytes(),Base64.DEFAULT));
                        Request original = chain.request();
                        Request request = original.newBuilder().header("Authorization",basicAuth.trim()).build();
                        return chain.proceed(request);
                    }
                });
            }
            return builder;
        }
    }
}
