package com.bmj.greader.dagger2.component;

import dagger.Component;
import com.bmj.greader.dagger2.PerActivity;
import com.bmj.greader.dagger2.module.ActivityModule;
import com.bmj.greader.dagger2.module.RepoModule;
import com.bmj.greader.dagger2.module.TrendingRepoModule;
import com.bmj.greader.ui.module.main.MainActivity;
import com.bmj.greader.ui.module.repo.MostStarFragment;
import com.bmj.greader.ui.module.repo.TrendingFragment;

/**
 * Created by Administrator on 2016/11/15 0015.
 */
@PerActivity
@Component(
        dependencies = ApplicationComponent.class,
        modules = {ActivityModule.class, RepoModule.class, TrendingRepoModule.class}
)
public interface MainComponent extends ActivityComponent{
    void inject(MostStarFragment mostStarFragment);
    void inject(TrendingFragment trendingFragment);
    void inject(MainActivity mainActivity);
}
