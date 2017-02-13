package com.bmj.greader.data.net;

import android.app.Application;

import javax.inject.Inject;

import com.bmj.greader.common.config.GithubConfig;
import com.bmj.greader.data.api.AccountApi;
import com.bmj.greader.data.model.User;
import com.bmj.greader.data.net.retrofit.GithubAuthRetrofit;
import com.bmj.greader.data.net.request.CreateAuthorization;
import com.bmj.greader.data.net.response.AuthorizationResp;
import com.bmj.greader.data.net.services.AccountService;
import com.bmj.greader.data.pref.AccountPref;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by Administrator on 2016/11/12 0012.
 */
public class AccountDataSource implements AccountApi{
    @Inject
    GithubAuthRetrofit mRetrofit;

    @Inject
    Application mContext;

    @Inject
    public AccountDataSource(){}

    @Override
    public Observable<User> login(String userName, String userPassword) {
        mRetrofit.setAuthInfo(userName,userPassword);
        final AccountService accountService = mRetrofit.get().create(AccountService.class);

        CreateAuthorization createAuthorization = new CreateAuthorization();
        createAuthorization.client_id = GithubConfig.CLIENT_ID;
        createAuthorization.client_secret = GithubConfig.CLIENT_SECRET;
        createAuthorization.note = GithubConfig.NOTE;
        createAuthorization.scopes = GithubConfig.SCOPES;

        return accountService.createAuthorization(createAuthorization)
                .flatMap(new Func1<AuthorizationResp, Observable<User>>() {
                    @Override
                    public Observable<User> call(AuthorizationResp authorizationResp) {
                        String token = authorizationResp.getToken();
                        AccountPref.saveLoginToken(mContext,token);
                        return accountService.getUserInfo(token);
                    }
                });
    }
}
