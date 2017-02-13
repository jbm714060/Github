package com.bmj.greader.ui.module.repo.adapter;

import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;

import com.bmj.greader.R;
import com.bmj.greader.data.model.Languages;
import com.bmj.greader.data.model.Repo;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by Administrator on 2016/12/15 0015.
 */
public class RepoListRecyclerAdapter2 extends BaseQuickAdapter<Repo>{

    public RepoListRecyclerAdapter2(List<Repo> data) {
        super(R.layout.item_repo2,data);
    }

    @Override
    protected void convert(BaseViewHolder holder, Repo repo) {
        holder.setText(R.id.ir2_repo_name,repo.getName());

        if(!TextUtils.isEmpty(repo.getDescription())) {
            holder.setText(R.id.ir2_repodesc, repo.getDescription());
            holder.getView(R.id.ir2_repodesc).setVisibility(View.VISIBLE);
        }else{
            holder.getView(R.id.ir2_repodesc).setVisibility(View.GONE);
        }

        holder.setText(R.id.ir2_forkscount,repo.getForks_count()+"");
        holder.setText(R.id.ir2_starscount,repo.getStargazers_count()+"");

        if(TextUtils.isEmpty(repo.getLanguage()))
            holder.setText(R.id.ir2_language,"unknown");
        else
            holder.setText(R.id.ir2_language,repo.getLanguage());

        holder.setTextColor(R.id.ir2_langcolor,ContextCompat.getColor(this.mContext,
                Languages.getLangColor(repo.getLanguage())));

        if(repo.isFork())
            holder.setText(R.id.ir2_repo_nameicon,"{oct_repo_forked} ");
        else
            holder.setText(R.id.ir2_repo_nameicon,"{oct_repo} ");
    }
}
