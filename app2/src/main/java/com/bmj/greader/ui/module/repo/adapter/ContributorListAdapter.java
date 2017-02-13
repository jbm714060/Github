package com.bmj.greader.ui.module.repo.adapter;

import android.app.Activity;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import com.bmj.greader.R;
import com.bmj.greader.common.wrapper.ImageLoader;
import com.bmj.greader.data.model.User;

/**
 * Created by Administrator on 2016/11/20 0020.
 */
public class ContributorListAdapter extends BaseQuickAdapter<User>{

    private Activity activity;

    public ContributorListAdapter(Activity activity,List<User> data){
        super(R.layout.item_user,data);
        this.activity = activity;
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, User user) {
        ImageLoader.load(activity,user.getAvatar_url(),
                (ImageView)baseViewHolder.getView(R.id.user_icon));
    }
}
