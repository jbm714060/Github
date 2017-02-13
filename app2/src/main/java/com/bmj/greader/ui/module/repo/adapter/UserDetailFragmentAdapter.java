package com.bmj.greader.ui.module.repo.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.bmj.greader.data.api.RepoApi;
import com.bmj.greader.ui.module.repo.TestFragment;
import com.bmj.greader.ui.module.user.UserGistFragment;
import com.bmj.greader.ui.module.user.UserListFragment;
import com.bmj.greader.ui.module.user.UserReposFragment;

/**
 * Created by Administrator on 2016/12/13 0013.
 */
public class UserDetailFragmentAdapter extends FragmentPagerAdapter {

    public static final int FRAGMENT_REPOS = 0;
    public static final int FRAGMENT_STARS = 1;
    public static final int FRAGMENT_FOLLOWERS = 2;
    public static final int FRAGMENT_FOLLOWING = 3;
    public static final int FRAGMENT_GISTS = 4;

    private String mUserLogin;

    public UserDetailFragmentAdapter(FragmentManager fm, String userlogin) {
        super(fm);
        mUserLogin = userlogin;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case FRAGMENT_REPOS:
                UserReposFragment reposFragment = UserReposFragment.instance(mUserLogin, RepoApi.OWNER_REPOS);
                return reposFragment;
            case FRAGMENT_STARS:
                UserReposFragment starredFragment = UserReposFragment.instance(mUserLogin, RepoApi.STARRED_REPOS);
                return starredFragment;
            case FRAGMENT_FOLLOWERS:
                UserListFragment follower = UserListFragment.instance(mUserLogin, RepoApi.FOLLOWER);
                return follower;
            case FRAGMENT_FOLLOWING:
                UserListFragment following = UserListFragment.instance(mUserLogin, RepoApi.FOLLOWING);
                return following;
            case FRAGMENT_GISTS:
                UserGistFragment gist = UserGistFragment.instance(mUserLogin);
                return gist;
        }
        TestFragment fragment1 = new TestFragment();
        return fragment1;
    }

    /**
     * overview,repositories,stars,followers,following,gists
     * @return
     */
    @Override
    public int getCount() {
        return 5;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case FRAGMENT_REPOS:
                return "owner repos";
            case FRAGMENT_STARS:
                return "stars";
            case FRAGMENT_FOLLOWERS:
                return "followers";
            case FRAGMENT_FOLLOWING:
                return "following";
            case FRAGMENT_GISTS:
                return "gists";
            default:
                return "unknown";
        }
    }
}
