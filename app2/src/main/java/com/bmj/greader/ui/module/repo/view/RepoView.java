package com.bmj.greader.ui.module.repo.view;

import com.bmj.mvp.lce.LoadView;

/**
 * Created by Administrator on 2016/11/19 0019.
 */
public interface RepoView extends LoadView{
    void starSuccess();
    void starFailed();
    void unstarSuccess();
    void unstarFailed();
}
