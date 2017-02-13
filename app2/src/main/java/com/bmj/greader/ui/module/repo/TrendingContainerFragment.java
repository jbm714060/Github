package com.bmj.greader.ui.module.repo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.bmj.greader.R;
import com.bmj.greader.data.api.TrendingApi;
import com.bmj.greader.data.model.Languages;
import com.bmj.greader.ui.base.BaseFragment;
import com.bmj.greader.ui.module.repo.adapter.TrendingFragmentAdapter;

/**
 * Created by Administrator on 2016/11/16 0016.
 */
public class TrendingContainerFragment extends BaseFragment{
    @BindView(R.id.tabs)
    PagerSlidingTabStrip mtabStrip;

    @BindView(R.id.view_pager)
    ViewPager mViewPager;

    private TrendingFragmentAdapter mAdapter;

    private final Integer[] TRENDING_CATEGORY = {
            Languages.LANG_JAVA,
            Languages.LANG_JS,
            Languages.LANG_PHP,
            Languages.LANG_PYTHON,
            Languages.LANG_OC,
            Languages.LANG_HTML,
            Languages.LANG_SWIFT
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trending_container,null);
        ButterKnife.bind(this,view);
        initViews();
        return view;
    }

    private void initViews(){
        getActivity().setTitle(R.string.tabs_trending);
        mAdapter = new TrendingFragmentAdapter(getChildFragmentManager(),TRENDING_CATEGORY);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOffscreenPageLimit(TRENDING_CATEGORY.length);
        mtabStrip.setViewPager(mViewPager);
    }
}
