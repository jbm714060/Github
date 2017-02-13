package com.bmj.greader.ui.module.repo.adapter;

import android.content.Context;
import android.util.ArrayMap;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bmj.greader.R;
import com.bmj.greader.common.wrapper.ImageLoader;
import com.bmj.greader.data.model.User;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2016/12/27 0027.
 */
public class SearchUserRecyclerAdapter2 extends BaseQuickAdapter<User>{
    int min;
    int step;

    public SearchUserRecyclerAdapter2(List<User> datas,Context context){
        super(R.layout.item_search_user,datas);
        step = context.getResources().getDimensionPixelSize(R.dimen.dimen_10);
        min = context.getResources().getDimensionPixelSize(R.dimen.dimen_124);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, User user) {
        ImageView view = baseViewHolder.getView(R.id.iu3_user_avatar);
        int height = min;
        int id = baseViewHolder.getLayoutPosition();
        if(id%2 ==0)
            height += step;
        if(id%3 ==0)
            height += step;
        if(id%5 == 0)
            height += step;

        view.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,height));
        ImageLoader.load(user.getAvatar_url(),view);

        baseViewHolder.setText(R.id.iu3_user_login,user.getLogin());
    }
}
