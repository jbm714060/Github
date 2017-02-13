package com.bmj.greader.ui.base.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Administrator on 2016/11/18 0018.
 */
public abstract class ArrayListAdapter<T> extends BaseAdapter{
    protected List<T> mList;
    protected Activity mContext;
    protected ListView mListView;

    public ArrayListAdapter(Activity context){
        mContext = context;
    }

    @Override
    public int getCount() {
        return mList == null?0:mList.size();
    }

    @Override
    public Object getItem(int i) {
        return mList!=null?mList.get(i):null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public abstract View getView(int i, View view, ViewGroup viewGroup);

    public void setList(List<T> list){
        mList = list;
        notifyDataSetChanged();
    }

    public List<T> getList()
    {
        return mList;
    }

    public void setList(T[] list){
        setList(Arrays.asList(list));
    }

    public ListView getListView(){
        return mListView;
    }

    public void setListView(ListView lv){
        mListView = lv;
    }
}
