package com.bmj.greader.dagger2.module;

import dagger.Module;
import dagger.Provides;
import com.bmj.greader.data.api.RepoApi;
import com.bmj.greader.data.net.RepoDataSource;

/**
 * Created by Administrator on 2016/11/15 0015.
 */
@Module
public class RepoModule {
    @Provides
    RepoApi provideRepoApi(RepoDataSource dataSource){
        return dataSource;
    }
}
