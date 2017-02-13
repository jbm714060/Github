package com.bmj.greader.ui.module.repo.view;

import com.bmj.greader.data.net.response.Content;
import com.bmj.mvp.lce.LceView;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/12/6 0006.
 */
public interface RepoTreeView extends LceView<ArrayList<Content>> {
    void showCode(Content data);
}
