package com.bmj.greader.ui.module.user;

import com.bmj.greader.R;
import com.bmj.greader.data.model.gist.GistFile;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by Administrator on 2016/12/20 0020.
 */
public class FileListRecyclerAdapter extends BaseQuickAdapter<GistFile>{

    public FileListRecyclerAdapter(List<GistFile> gistFileList){
        super(R.layout.item_file_list,gistFileList);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, GistFile gistFile) {
        baseViewHolder.setText(R.id.ifl_file_name,gistFile.getFilename());
    }
}
