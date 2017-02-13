package com.bmj.greader.ui.module.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.github.paolorotolo.appintro.AppIntro2;
import com.github.paolorotolo.appintro.AppIntroFragment;

import com.bmj.greader.R;
import com.bmj.greader.data.pref.AppPref;
import com.bmj.greader.ui.CustomSlide;


/**
 * Created by Administrator on 2016/11/15 0015.
 */
public class IntroduceActivity extends AppIntro2{

    public static void launch(Context context){
        context.startActivity(new Intent(context,IntroduceActivity.class));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addSlide(AppIntroFragment.newInstance(
                getString(R.string.appintro_slide1_title),
                getString(R.string.appintro_slide1_titlefont),
                getString(R.string.appintro_slide1_desc),
                getString(R.string.appintro_slide1_descfont),
                R.drawable.ic_slide1,
                getResources().getColor(R.color.md_indigo_400)
        ));

        addSlide(AppIntroFragment.newInstance(
                getString(R.string.appintro_slide2_title),
                getString(R.string.appintro_slide1_titlefont),
                getString(R.string.appintro_slide2_desc),
                getString(R.string.appintro_slide1_descfont),
                R.drawable.ic_slide2,
                getResources().getColor(R.color.md_amber_500)
        ));

        addSlide(AppIntroFragment.newInstance(
                getString(R.string.appintro_slide3_title),
                getString(R.string.appintro_slide1_titlefont),
                getString(R.string.appintro_slide3_desc),
                getString(R.string.appintro_slide1_descfont),
                R.drawable.slide3,
                getResources().getColor(R.color.md_green_500)
        ));

        addSlide(CustomSlide.newInstance(R.layout.slide_one_image));
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        doMain();
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        doMain();
    }

    private void doMain(){
        AppPref.setAlreadyRun(this);
        MainActivity.launch(this);
        finish();
    }
}
