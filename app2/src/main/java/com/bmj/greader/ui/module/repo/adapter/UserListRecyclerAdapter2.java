package com.bmj.greader.ui.module.repo.adapter;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.bmj.greader.R;
import com.bmj.greader.common.wrapper.ImageLoader;
import com.bmj.greader.data.model.User;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by Administrator on 2016/12/15 0015.
 */
public class UserListRecyclerAdapter2 extends BaseQuickAdapter<User>{

    public UserListRecyclerAdapter2(List<User> data) {
        super(R.layout.item_user2,data);
    }

    @Override
    protected void convert(BaseViewHolder holder, User user) {
        ImageLoader.load(user.getAvatar_url(),(ImageView)holder.getView(R.id.iu2_avatar));

        holder.setText(R.id.ir2_userlogin,user.getLogin());

        if(!TextUtils.isEmpty(user.getName()))
            holder.setText(R.id.ir2_username,mContext.getResources().getString(R.string.username,user.getName()));
        else
            holder.getView(R.id.ir2_username).setVisibility(View.GONE);

        if(!TextUtils.isEmpty(user.getBio()))
            holder.setText(R.id.ir2_bio,user.getBio());
        else
            holder.getView(R.id.ir2_bio).setVisibility(View.GONE);

        if(!TextUtils.isEmpty(user.getCompany()))
            holder.setText(R.id.fo_info_company,user.getCompany());
        else {
            holder.getView(R.id.fo_info_company).setVisibility(View.GONE);
            holder.getView(R.id.fo_info_company_ic).setVisibility(View.GONE);
        }

        if(!TextUtils.isEmpty(user.getLocation()))
            holder.setText(R.id.fo_info_location,user.getLocation());
        else {
            holder.getView(R.id.fo_info_location).setVisibility(View.GONE);
            holder.getView(R.id.fo_info_location_ic).setVisibility(View.GONE);
        }
    }
}
