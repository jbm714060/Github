package com.bmj.greader.data.model;

/**
 * Created by Administrator on 2016/12/26 0026.
 */
import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.ArrayList;

/**
 * https://github.com/CymChad/BaseRecyclerViewAdapterHelper
 */
public class MultiItem extends MultiItemEntity {
    public static final int SMALL = 1;
    public static final int LARGE = 2;

    private int itemType;
    private int spanSize;
    private User content;

    public MultiItem(int itemType, int spanSize, User content) {
        this.itemType = itemType;
        this.spanSize = spanSize;
        this.content = content;
    }

    public MultiItem(int itemType, int spanSize) {
        this.itemType = itemType;
        this.spanSize = spanSize;
    }

    public int getSpanSize() {
        return spanSize;
    }

    public void setSpanSize(int spanSize) {
        this.spanSize = spanSize;
    }

    public User getContent() {
        return content;
    }

    public void setContent(User content) {
        this.content = content;
    }

    @Override
    public int getItemType() {
        return itemType;
    }

    public static ArrayList<MultiItem> parse(ArrayList<User> datas){
        int i = 0;
        ArrayList<MultiItem> result = new ArrayList<>();
        while(i<datas.size()){
            int type = Math.random()*10 > 5?SMALL:LARGE;
            int spansize = type==SMALL?1:2;
            result.add(new MultiItem(type,spansize,datas.get(i)));
            i++;
        }
        return result;
    }
}
