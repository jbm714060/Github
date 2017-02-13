package com.bmj.greader.ui.module.repo.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import com.bmj.greader.R;
import com.bmj.greader.common.wrapper.ImageLoader;
import com.bmj.greader.data.model.User;

/**
 * Created by Administrator on 2016/11/21 0021.
 */
public class UserListRecyclerAdapter extends BaseQuickAdapter<User>{

    public UserListRecyclerAdapter(List<User> data) {
        super(R.layout.item_user_with_name,data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, User user) {
        baseViewHolder.setText(R.id.username,user.getLogin());
        ImageLoader.loadWithCircle(user.getAvatar_url(),(ImageView)baseViewHolder.getView(R.id.user_icon));
    }
}
