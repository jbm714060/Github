package com.bmj.greader.ui.module.repo.view;

import com.bmj.greader.data.model.gist.Gist;
import com.bmj.mvp.lce.LoadView;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/12/19 0019.
 */
public interface GistView extends LoadView{
    void showGists(ArrayList<Gist> gists);
    void isStarSuccessfully(boolean success,int position);
    void isUnstarSuccessfully(boolean success,int position);
}
