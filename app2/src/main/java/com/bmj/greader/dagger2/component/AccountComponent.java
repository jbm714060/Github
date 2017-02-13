package com.bmj.greader.dagger2.component;

import dagger.Component;
import com.bmj.greader.dagger2.PerActivity;
import com.bmj.greader.dagger2.module.AccountModule;
import com.bmj.greader.dagger2.module.ActivityModule;
import com.bmj.greader.ui.module.account.LoginActivity;

/**
 * Created by Administrator on 2016/11/13 0013.
 */
@PerActivity
@Component(dependencies=ApplicationComponent.class,modules = {ActivityModule.class,AccountModule.class})
public interface AccountComponent extends ActivityComponent{
    void inject(LoginActivity loginActivity);
}
