package com.bmj.greader.dagger2.module;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

import com.bmj.greader.dagger2.ApplicationContext;
import com.bmj.greader.data.net.retrofit.GithubFileRetrofit;
import com.bmj.greader.data.net.retrofit.GithubRepoRetrofit;
import com.bmj.greader.data.net.retrofit.GithubTrendingRetrofit;
import com.bmj.greader.data.net.services.FileDownloadService;
import com.bmj.greader.data.net.services.GistService;
import com.bmj.greader.data.net.services.IssueService;
import com.bmj.greader.data.net.services.RepoService;
import com.bmj.greader.data.net.services.TrendingService;

/**
 * Created by Administrator on 2016/11/10 0010.
 */
@Module
public class ApplicationModule {
    protected  final Application application;

    public ApplicationModule(Application application){
        this.application = application;
    }

    @Provides
    Application provideApplication(){
        return application;
    }

    @Provides
    @ApplicationContext
    Context provideContext(){
        return application;
    }

    @Provides
    @Singleton
    RepoService provideRepoService(GithubRepoRetrofit retrofit) {
        return retrofit.get().create(RepoService.class);
    }

    @Provides
    @Singleton
    TrendingService provideTrendingService(GithubTrendingRetrofit retrofit) {
        return retrofit.get().create(TrendingService.class);
    }

    @Provides
    @Singleton
    GistService provideGistService(GithubRepoRetrofit retrofit){
        return retrofit.get().create(GistService.class);
    }

    @Provides
    @Singleton
    FileDownloadService provideFileDownloadService(GithubFileRetrofit retrofit){
        return retrofit.get().create(FileDownloadService.class);
    }

    @Provides
    @Singleton
    IssueService provideIssueService(GithubRepoRetrofit retrofit){
        return retrofit.get().create(IssueService.class);
    }
}
