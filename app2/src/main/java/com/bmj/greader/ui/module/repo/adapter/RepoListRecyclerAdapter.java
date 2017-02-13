package com.bmj.greader.ui.module.repo.adapter;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.util.Log;

import com.bmj.greader.data.RepoUpdatedTime;
import com.bmj.greader.data.model.Languages;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import com.bmj.greader.R;
import com.bmj.greader.common.util.StringUtil;
import com.bmj.greader.data.model.Repo;

/**
 * Created by Administrator on 2016/11/17 0017.
 */
public class RepoListRecyclerAdapter extends BaseQuickAdapter<Repo>{

    public RepoListRecyclerAdapter(List<Repo> data){
        super(R.layout.item_repo,data);
    }

    @Override
    protected void convert(BaseViewHolder holder, Repo repo) {
        SpannableStringBuilder spannableBuilder = new SpannableStringBuilder();
        spannableBuilder.append(repo.getOwner().getLogin()).append(" / ");
        int userloginIndex = spannableBuilder.length();
        spannableBuilder.append(repo.getName());
        spannableBuilder.setSpan(new StyleSpan(Typeface.BOLD),userloginIndex,
                spannableBuilder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        String desc = StringUtil.trimNewLine(repo.getDescription());
        holder.setText(R.id.item_repo_name, spannableBuilder)
                .setText(R.id.item_repo_desc, desc)
                .setText(R.id.item_repo_starscount,repo.getStargazers_count()+"")
                .setText(R.id.item_repo_forkscount,repo.getForks_count()+"")
                .setTextColor(R.id.ir_langcolor, ContextCompat.getColor(this.mContext,
                        Languages.getLangColor(repo.getLanguage())))
                .setText(R.id.ir_language,repo.getLanguage()==null?"unknown":repo.getLanguage())
                .setText(R.id.ir_updatetime, RepoUpdatedTime.getUpdatedTimeString(repo.getUpdated_at()));
    }
}
