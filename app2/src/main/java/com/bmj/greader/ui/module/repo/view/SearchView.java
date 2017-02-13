package com.bmj.greader.ui.module.repo.view;

import com.bmj.greader.data.model.Repo;
import com.bmj.greader.data.model.User;
import com.bmj.greader.data.net.response.SearchRepoResp;
import com.bmj.greader.data.net.response.SearchUserResp;
import com.bmj.mvp.lce.LoadView;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/11/21 0021.
 */
public interface SearchView extends LoadView{
    void showSearchRepo(SearchRepoResp repo,int page);
    void showSearchUser(SearchUserResp user,int page);
    void error(Throwable e);
}
