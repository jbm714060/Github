package com.bmj.greader.ui.module.repo.adapter;

import android.media.Image;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bmj.greader.R;
import com.bmj.greader.common.wrapper.ImageLoader;
import com.bmj.greader.data.model.MultiItem;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by Administrator on 2016/12/26 0026.
 */
public class SearchUserRecyclerAdapter extends BaseMultiItemQuickAdapter<MultiItem> {

    public SearchUserRecyclerAdapter(List<MultiItem> data){
        super(data);
        addItemType(MultiItem.SMALL,R.layout.item_user3);
        addItemType(MultiItem.LARGE,R.layout.item_user3);
    }

    @Override
    protected void convert(BaseViewHolder holder, MultiItem user) {
        switch (holder.getItemViewType()){
            case MultiItem.SMALL:
                holder.getView(R.id.iu3_user_avatar_large).setVisibility(View.GONE);
                ImageLoader.load(user.getContent().getAvatar_url(),(ImageView)holder.getView(R.id.iu3_user_avatar_small));
                break;
            case MultiItem.LARGE:
                holder.getView(R.id.iu3_user_avatar_small).setVisibility(View.GONE);
                ImageLoader.load(user.getContent().getAvatar_url(),(ImageView)holder.getView(R.id.iu3_user_avatar_large));
                break;
        }
        holder.setText(R.id.iu3_user_login,user.getContent().getLogin());
    }
}
