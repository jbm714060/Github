package com.bmj.greader.ui.module.repo.adapter;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.util.Log;

import com.bmj.greader.common.wrapper.AppLog;
import com.bmj.greader.data.api.TrendingApi;

import com.bmj.greader.data.model.Languages;
import com.bmj.greader.ui.base.adapter.ArrayFragmentPagerAdapter;
import com.bmj.greader.ui.module.repo.TrendingFragment;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.material_design_iconic_typeface_library.MaterialDesignIconic;
import com.mikepenz.octicons_typeface_library.Octicons;

/**
 * Created by Administrator on 2016/11/18 0018.
 */
public class TrendingFragmentAdapter extends ArrayFragmentPagerAdapter<Integer>{
    public TrendingFragmentAdapter(FragmentManager fm,Integer[] data){
        super(fm);
        setList(data);
    }

    @Override
    public Fragment getItem(int position) {
        int lang = mList.get(position);
        //Fragment fragment = TrendingFragment.newInstance(lang);
        //return fragment;
        return null;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        int lang = mList.get(position);
        String sLang = Languages.langType2String(Languages.int2LangType(lang));
        if(TextUtils.isEmpty(sLang))
            AppLog.e("unknown language");
        return sLang;
    }
}
