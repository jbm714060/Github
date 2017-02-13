package com.bmj.greader.dagger2.component;

import android.app.Activity;

import dagger.Component;
import com.bmj.greader.dagger2.PerActivity;
import com.bmj.greader.dagger2.module.ActivityModule;

/**
 * Created by Administrator on 2016/11/12 0012.
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class,modules = ActivityModule.class)
public interface ActivityComponent {
    Activity activity();
}
