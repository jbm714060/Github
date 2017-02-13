package com.bmj.greader.ui.module.repo.view;

import com.bmj.greader.data.model.issue.Issue;
import com.bmj.mvp.lce.LceView;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/12/22 0022.
 */
public interface IssueView extends LceView<ArrayList<Issue>>{
    void showMore(ArrayList<Issue> data);
}
