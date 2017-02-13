package com.bmj.greader.ui.module.repo.view;

import com.bmj.greader.data.model.Repo;
import com.bmj.greader.data.model.RepoDetail;
import com.bmj.greader.data.model.User;
import com.bmj.greader.data.net.response.Content;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/11/19 0019.
 */
public interface RepoDetailView extends RepoView{
    void showRepo(Repo repo);
    void showRepoContributors(ArrayList<User> contributors);
    void showRepoForks(ArrayList<Repo> forks);
    void showReadMe(Content content);
    void showIsStarred(boolean isStarred);
}
