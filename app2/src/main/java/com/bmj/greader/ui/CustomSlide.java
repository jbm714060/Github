package com.bmj.greader.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bmj.greader.ui.base.BaseFragment;

/**
 * Created by Administrator on 2016/11/23 0023.
 */
public class CustomSlide extends BaseFragment{

    private static String EXTRA_SLIDE_ID = "layout_slide_id";
    public static CustomSlide newInstance(int resSlideId){
        CustomSlide customSlide = new CustomSlide();
        Bundle bundle = new Bundle();
        bundle.putInt(EXTRA_SLIDE_ID,resSlideId);
        customSlide.setArguments(bundle);
        return customSlide;
    }

    private int slideLayoutId;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments()!=null && getArguments().containsKey(EXTRA_SLIDE_ID))
            slideLayoutId = getArguments().getInt(EXTRA_SLIDE_ID);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(slideLayoutId,container,false);
    }
}
