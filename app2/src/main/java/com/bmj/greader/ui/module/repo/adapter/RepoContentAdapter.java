package com.bmj.greader.ui.module.repo.adapter;

import android.app.Activity;
import android.renderscript.Float2;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.balysv.materialripple.MaterialRippleLayout;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mikepenz.iconics.view.IconicsImageView;
import com.mikepenz.material_design_iconic_typeface_library.MaterialDesignIconic;
import com.mikepenz.octicons_typeface_library.Octicons;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.Format;
import java.util.List;

import com.bmj.greader.R;
import com.bmj.greader.data.net.response.Content;

/**
 * Created by Administrator on 2016/11/20 0020.
 */
public class RepoContentAdapter extends BaseQuickAdapter<Content>{
    public RepoContentAdapter(Activity activity,List<Content> data){
        super(R.layout.item_content,data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, Content content) {
        IconicsImageView typeIndicator = baseViewHolder.getView(R.id.type);
        if(content.isDir()) {
            typeIndicator.setIcon(Octicons.Icon.oct_file_directory);
            typeIndicator.setColorRes(R.color.md_yellow_900);
        }
        else if(content.isFile()) {
            switch (content.getFileType()){
                case Content.FILE_CODE:
                    typeIndicator.setIcon(Octicons.Icon.oct_file_code);
                    typeIndicator.setColorRes(R.color.md_indigo_400);
                    break;
                case Content.FILE_TEXT:
                    typeIndicator.setIcon(Octicons.Icon.oct_file_text);
                    typeIndicator.setColorRes(R.color.md_brown_300);
                    break;
                case Content.FILE_ZIP:
                    typeIndicator.setIcon(Octicons.Icon.oct_file_zip);
                    break;
                case Content.FILE_PDF:
                    typeIndicator.setIcon(Octicons.Icon.oct_file_pdf);
                    break;
                case Content.FILE_MEDIA: {
                    typeIndicator.setIcon(Octicons.Icon.oct_file_media);
                    typeIndicator.setColorRes(R.color.md_green_400);
                    break;
                }
                default: {
                    typeIndicator.setIcon(MaterialDesignIconic.Icon.gmi_file);
                    typeIndicator.setColorRes(R.color.md_brown_300);
                    break;
                }
            }
        }
        else if(content.isSubmodule())
            typeIndicator.setIcon(Octicons.Icon.oct_file_submodule);
        else
            typeIndicator.setIcon(Octicons.Icon.oct_file_symlink_file);

        baseViewHolder.setText(R.id.file_name,content.name);
        baseViewHolder.setTextColor(R.id.file_name,ContextCompat.getColor(mContext,R.color.black));

        if(content.size > 0){
            baseViewHolder.setText(R.id.file_size, fetchFileSizeStr(content.size));
            baseViewHolder.getView(R.id.file_size).setVisibility(View.VISIBLE);
        }
        else
            baseViewHolder.getView(R.id.file_size).setVisibility(View.GONE);
    }

    private String fetchFileSizeStr(int size){
        StringBuilder sizeStr = new StringBuilder();
        if(size < 1024){
            sizeStr.append(size).append("B");
        }else if(size < 1024*1024){
            sizeStr.append(new DecimalFormat(".0").format(size*1f/1024)).append("KB");
        }else{
            sizeStr.append(String.format("%.2f",size*1f/(1024*1024))).append("MB");
        }
        return sizeStr.toString();
    }
}
