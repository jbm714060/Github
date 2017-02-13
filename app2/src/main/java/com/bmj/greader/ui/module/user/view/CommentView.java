package com.bmj.greader.ui.module.user.view;

import com.bmj.greader.data.model.GithubComment;
import com.bmj.mvp.lce.LceView;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/12/21 0021.
 */
public interface CommentView extends LceView<ArrayList<GithubComment>> {
    void addCommentResult(boolean isSuccess,GithubComment comment);
    void deleteCommentResult(boolean isSuccess,int position);
    void loadMore(ArrayList<GithubComment> data);
}
