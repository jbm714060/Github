package com.bmj.greader.ui.module.user;

import android.graphics.Rect;
import android.text.Layout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bmj.greader.R;
import com.bmj.greader.common.wrapper.ImageLoader;
import com.bmj.greader.data.model.GithubComment;
import com.bmj.greader.data.pref.AccountPref;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ms.square.android.expandabletextview.ExpandableTextView;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2016/12/21 0021.
 */
public class GistCommentsRecyclerAdapter extends BaseQuickAdapter<GithubComment>{


    public GistCommentsRecyclerAdapter(List<GithubComment> data) {
        super(R.layout.item_gist,data);
    }

    @Override
    protected void convert(BaseViewHolder holder, final GithubComment comment) {
        holder.setText(R.id.ig_gist_id,comment.getUser().getLogin());
        ImageLoader.loadWithCircle(comment.getUser().getAvatar_url(),
                ((ImageView)holder.getView(R.id.ig_avatar)));
        Pattern p = Pattern.compile("[A-Z]");
        Matcher m = p.matcher(comment.getUpdated_at());
        String updateTime = m.replaceAll(" ").trim();
        holder.setText(R.id.ig_update_time,updateTime);

        holder.getView(R.id.ig_file_layout).setVisibility(View.GONE);
        holder.getView(R.id.ig_comment_layout).setVisibility(View.GONE);
        holder.getView(R.id.ig_star).setVisibility(View.GONE);
        holder.setText(R.id.ig_gist_desc, comment.getBody());

        if(AccountPref.isSelf(mContext,comment.getUser().getLogin())) {
            holder.getView(R.id.ig_comment_delete).setVisibility(View.VISIBLE);
            holder.setOnClickListener(R.id.ig_comment_delete,new OnItemChildClickListener());
        }
        holder.setOnClickListener(R.id.ig_gist_id,new OnItemChildClickListener());
        holder.setOnClickListener(R.id.ig_avatar,new OnItemChildClickListener());
    }
}
