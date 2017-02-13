package com.bmj.greader.dagger2.module;

import dagger.Module;
import dagger.Provides;
import com.bmj.greader.data.api.AccountApi;
import com.bmj.greader.data.net.AccountDataSource;

/**
 * Created by Administrator on 2016/11/13 0013.
 */
@Module
public class AccountModule {
    @Provides
    AccountApi provideAccountApi(AccountDataSource dataSource) {
        return dataSource;
    }
}
