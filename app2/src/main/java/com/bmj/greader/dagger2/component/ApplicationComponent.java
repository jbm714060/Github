package com.bmj.greader.dagger2.component;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Component;

import com.bmj.greader.dagger2.ApplicationContext;
import com.bmj.greader.dagger2.module.ApplicationModule;
import com.bmj.greader.data.net.services.FileDownloadService;
import com.bmj.greader.data.net.services.GistService;
import com.bmj.greader.data.net.services.IssueService;
import com.bmj.greader.data.net.services.RepoService;
import com.bmj.greader.data.net.services.TrendingService;

/**
 * Created by Administrator on 2016/11/10 0010.
 */
@Singleton
@Component(modules= ApplicationModule.class)
public interface ApplicationComponent {
    @ApplicationContext
    Context context();

    Application application();

    RepoService repoService();

    TrendingService trendingService();

    GistService gistService();

    FileDownloadService fileDownloadService();

    IssueService issueService();
}
