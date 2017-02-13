package com.bmj.greader.ui.module.repo.view;

import com.bmj.mvp.lce.LoadView;

/**
 * Created by Administrator on 2016/12/17 0017.
 */
public interface FollowActionView extends LoadView{
    void showFollowState(boolean isFollowing);
    void showFollowIsSuccess(boolean isSuccess);
    void showUnfollowIsSuccess(boolean isSuccess);
}
