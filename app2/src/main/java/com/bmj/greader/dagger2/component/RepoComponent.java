package com.bmj.greader.dagger2.component;

import dagger.Component;
import com.bmj.greader.dagger2.PerActivity;
import com.bmj.greader.dagger2.module.ActivityModule;
import com.bmj.greader.dagger2.module.RepoModule;
import com.bmj.greader.ui.module.account.UserActivity;
import com.bmj.greader.ui.module.main.UserDetailActivity;
import com.bmj.greader.ui.module.repo.IssueCommentsPopup;
import com.bmj.greader.ui.module.repo.RepoIssueActivity;
import com.bmj.greader.ui.module.repo.RepoListActivity;
import com.bmj.greader.ui.module.repo.UserListActivity;
import com.bmj.greader.ui.module.repo.CodeActivity;
import com.bmj.greader.ui.module.repo.RepoDetailActivity;
import com.bmj.greader.ui.module.repo.RepoTreeActivity;
import com.bmj.greader.ui.module.repo.SearchActivity;
import com.bmj.greader.ui.module.user.FileReadPopup;
import com.bmj.greader.ui.module.user.GistCommentsPopup;
import com.bmj.greader.ui.module.user.UserGistFragment;
import com.bmj.greader.ui.module.user.UserListFragment;
import com.bmj.greader.ui.module.user.UserReposFragment;
import com.bmj.greader.ui.module.repo.RepoListFragment;

/**
 * Created by Administrator on 2016/11/15 0015.
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class,modules = {RepoModule.class, ActivityModule.class})
public interface RepoComponent extends ActivityComponent{
    void inject (UserActivity userActivity);
    void inject(SearchActivity searchActivity);

    void inject(RepoListActivity listActivity);
    void inject(RepoDetailActivity detailActivity);
    void inject(RepoTreeActivity treeActivity);
    void inject(CodeActivity codeActivity);
    void inject(UserListActivity userListActivity);

    void inject(RepoListFragment repoListFragment);

    void inject(RepoIssueActivity repoIssueActivity);
    void inject(UserDetailActivity userDetailActivity);
    void inject(UserReposFragment userOverViewFragment);
    void inject(UserListFragment userListFragment);
    void inject(UserGistFragment userGistFragment);
    void inject(FileReadPopup fileReadPopup);
    void inject(GistCommentsPopup gistCommentsPopup);
    void inject(IssueCommentsPopup issueCommentsPopup);
}
