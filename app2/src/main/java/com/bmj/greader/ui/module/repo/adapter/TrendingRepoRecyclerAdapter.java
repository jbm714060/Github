package com.bmj.greader.ui.module.repo.adapter;

import android.content.res.Resources;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.StyleSpan;
import android.text.style.TypefaceSpan;
import android.util.Log;
import android.view.View;

import com.bmj.greader.MyApplication;
import com.bmj.greader.data.model.Languages;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.HashMap;
import java.util.List;

import com.bmj.greader.R;
import com.bmj.greader.common.util.StringUtil;
import com.bmj.greader.data.model.TrendingRepo;
import com.mikepenz.devicon_typeface_library.DevIcon;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.octicons_typeface_library.Octicons;

/**
 * Created by Administrator on 2016/11/18 0018.
 */
public class TrendingRepoRecyclerAdapter extends BaseQuickAdapter<TrendingRepo>{

    public TrendingRepoRecyclerAdapter(List<TrendingRepo> data){
        super(R.layout.item_trending_repo,data);

    }

    @Override
    protected void convert(BaseViewHolder holder, TrendingRepo repo) {
        int colorValue = ContextCompat.getColor(this.mContext, Languages.getLangColor(repo.getLanguage()));
        holder.setTextColor(R.id.itr_langcolor,colorValue);

        SpannableStringBuilder spannableBuilder = new SpannableStringBuilder();
        spannableBuilder.append(repo.getOwnUser()).append(" / ");
        int userloginIndex = (repo.getOwnUser()+" / ").length();
        spannableBuilder.append(repo.getRepoName());
        spannableBuilder.setSpan(new StyleSpan(Typeface.BOLD),userloginIndex,
                spannableBuilder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        holder.setText(R.id.itr_reponame,spannableBuilder);
        holder.setText(R.id.itr_desc,repo.getDescription());
        holder.setText(R.id.itr_language,repo.getLanguage());
        holder.setText(R.id.itr_stargazerscount,repo.getRepoStarCount());
        holder.setText(R.id.itr_forkscount,repo.getRepoForksCount());

        if(TextUtils.isEmpty(repo.getNewStars()) || !repo.getNewStars().contains("tar")){
            holder.getView(R.id.newstars_layout).setVisibility(View.INVISIBLE);
        }
        else {
            holder.getView(R.id.newstars_layout).setVisibility(View.VISIBLE);
            holder.setText(R.id.itr_newstars, repo.getNewStars());
        }
    }
}
