package com.bmj.greader.ui.base.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Administrator on 2016/11/18 0018.
 */
public class ArrayFragmentPagerAdapter<T> extends FragmentPagerAdapter {
    protected List<T> mList;

    public ArrayFragmentPagerAdapter(FragmentManager fm){
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return null;
    }

    @Override
    public int getCount() {
        return mList == null?0:mList.size();
    }

    public void setList(List<T> list){
        mList = list;
        notifyDataSetChanged();
    }

    public List<T> getList(){
        return mList;
    }

    public void setList(T[] array){
        setList(Arrays.asList(array));
    }
}
