package com.bmj.greader.ui.module.repo.adapter;

import android.support.v4.content.ContextCompat;
import android.view.View;

import com.bmj.greader.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mikepenz.iconics.view.IconicsImageView;
import com.mikepenz.octicons_typeface_library.Octicons;

import java.util.List;

/**
 * Created by Administrator on 2017/1/17 0017.
 */
public class DirectoryRecyclerAdapter extends BaseQuickAdapter<String>{

    public DirectoryRecyclerAdapter(List<String> data){
        super(R.layout.item_content,data);
    }

    @Override
    protected void convert(BaseViewHolder holder, String s) {
        IconicsImageView typeIndicator = holder.getView(R.id.type);
        typeIndicator.setIcon(Octicons.Icon.oct_file_directory);
        typeIndicator.setColorRes(R.color.md_yellow_900);

        holder.setText(R.id.file_name,s);
        holder.setTextColor(R.id.file_name, ContextCompat.getColor(mContext,R.color.md_grey_600));

        holder.getView(R.id.file_size).setVisibility(View.GONE);
    }
}
