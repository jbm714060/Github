package com.bmj.greader.ui.module.account.view;

import com.bmj.greader.data.model.User;
import com.bmj.mvp.lce.LoadView;

/**
 * Created by Administrator on 2016/11/13 0013.
 */
public interface LoginView extends LoadView{
    void loginSuccess(User user);
}
