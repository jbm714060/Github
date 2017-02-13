package com.bmj.greader.ui.module.user;

import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bmj.greader.R;
import com.bmj.greader.common.wrapper.ImageLoader;
import com.bmj.greader.data.model.gist.Gist;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2016/12/19 0019.
 */
public class GistListRecyclerAdapter extends BaseQuickAdapter<Gist>{

    public GistListRecyclerAdapter(List<Gist> data) {
        super(R.layout.item_gist,data);
    }

    @Override
    protected void convert(BaseViewHolder holder, Gist gist) {
        holder.setText(R.id.ig_gist_id,mContext.getResources().getString(R.string.gist_id,gist.getId()));
        ImageLoader.loadWithCircle(gist.getOwner().getAvatar_url(),((ImageView)holder.getView(R.id.ig_avatar)));

        Pattern p = Pattern.compile("[A-Z]");
        Matcher m = p.matcher(gist.getUpdated_at());
        String updateTime = m.replaceAll(" ").trim();
        holder.setText(R.id.ig_update_time,updateTime);

        if(gist.getFiles() != null)
            holder.setText(R.id.ig_file_count,gist.getFiles().size()+"");
        else
            holder.setText(R.id.ig_file_count,"0");

        holder.setText(R.id.ig_comment_count,gist.getComments()+"");

        if(!TextUtils.isEmpty(gist.getDescription()))
            holder.setText(R.id.ig_gist_desc, gist.getDescription());
        else
            holder.setText(R.id.ig_gist_desc,"no description yet.");

        if(gist.isStarred)
            holder.setTextColor(R.id.ig_star, ContextCompat.getColor(mContext,R.color.red));
        else
            holder.setTextColor(R.id.ig_star, ContextCompat.getColor(mContext,R.color.md_grey_600));

        holder.setOnClickListener(R.id.ig_file_layout,new OnItemChildClickListener());
        holder.setOnClickListener(R.id.ig_comment_layout,new OnItemChildClickListener());
        holder.setOnClickListener(R.id.ig_star,new OnItemChildClickListener());
    }
}
