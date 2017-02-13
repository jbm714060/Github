package com.bmj.greader.ui.module.repo.view;

import com.bmj.greader.data.model.Repo;
import com.bmj.greader.data.model.User;
import com.bmj.mvp.lce.LceView;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/12/11 0011.
 */
public interface UserDetailView extends LceView<User> {
    void showUserReposContent(ArrayList<Repo> repos,int pageIndex);
    void showAdditinalLoading();
    void dismissAdditionalLoading();
}
