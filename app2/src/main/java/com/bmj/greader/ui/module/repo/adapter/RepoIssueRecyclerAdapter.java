package com.bmj.greader.ui.module.repo.adapter;

import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;

import com.bmj.greader.R;
import com.bmj.greader.common.wrapper.ImageLoader;
import com.bmj.greader.data.model.issue.Issue;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2016/12/22 0022.
 */
public class RepoIssueRecyclerAdapter extends BaseQuickAdapter<Issue>{

    public RepoIssueRecyclerAdapter(List<Issue> data) {
        super(R.layout.item_gist,data);
    }

    @Override
    protected void convert(BaseViewHolder holder, Issue issue) {
        ImageLoader.loadWithCircle(issue.user.getAvatar_url(),(ImageView)holder.getView(R.id.ig_avatar));
        holder.setText(R.id.ig_gist_id,issue.user.getLogin());

        Pattern p = Pattern.compile("[A-Z]");
        Matcher m = p.matcher(issue.updated_at);
        String updateTime = m.replaceAll(" ").trim();
        holder.setText(R.id.ig_update_time,updateTime);
        holder.setText(R.id.ig_gist_desc,issue.title);
        holder.getView(R.id.ig_star).setVisibility(View.GONE);
        holder.getView(R.id.ig_file_layout).setVisibility(View.GONE);
        holder.getView(R.id.ig_comment_delete).setVisibility(View.GONE);

        holder.setText(R.id.ig_ic_reply,"{oct_comment_discussion}");
        holder.getView(R.id.ig_comment_count).setVisibility(View.INVISIBLE);
        //holder.setText(R.id.ig_comment_count,issue.comments+"");

        holder.setOnClickListener(R.id.ig_gist_id,new OnItemChildClickListener());
        holder.setOnClickListener(R.id.ig_avatar,new OnItemChildClickListener());
        holder.setOnClickListener(R.id.ig_comment_layout,new OnItemChildClickListener());
    }
}
