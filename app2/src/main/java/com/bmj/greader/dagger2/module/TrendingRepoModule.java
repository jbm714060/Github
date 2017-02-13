package com.bmj.greader.dagger2.module;

import dagger.Module;
import dagger.Provides;
import com.bmj.greader.data.api.TrendingApi;
import com.bmj.greader.data.net.TrendingRepoDataSource;

/**
 * Created by Administrator on 2016/11/15 0015.
 */
@Module
public class TrendingRepoModule {
    @Provides
    TrendingApi provideTrendingApi(TrendingRepoDataSource dataSource){
        return dataSource;
    }
}
