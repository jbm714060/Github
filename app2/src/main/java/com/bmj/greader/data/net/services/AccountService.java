package com.bmj.greader.data.net.services;

import rx.Observable;

import com.bmj.greader.data.model.User;
import com.bmj.greader.data.net.request.CreateAuthorization;
import com.bmj.greader.data.net.response.AuthorizationResp;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Administrator on 2016/11/12 0012.
 */
public interface AccountService {
    @POST("/authorizations")
    Observable<AuthorizationResp> createAuthorization(@Body CreateAuthorization createAuthorization);

    @GET("/user")
    Observable<User> getUserInfo(@Query("access_token") String access_token);
}
