package com.bmj.greader.ui.module.main;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;

import com.bmj.greader.R;
import com.bmj.greader.data.pref.AppPref;

import razerdp.basepopup.BasePopupWindow;

/**
 * Created by Administrator on 2016/12/30 0030.
 */
public class IntroducePopup extends BasePopupWindow{

    public IntroducePopup(Activity context){
        super(context);
    }

    @Override
    public View getClickToDismissView() {
        return getPopupWindowView();
    }

    @Override
    protected Animation initShowAnimation() {
        return AnimationUtils.loadAnimation(getContext(),R.anim.show_from_left);
    }

    @Override
    protected Animation initExitAnimation() {
        return AnimationUtils.loadAnimation(getContext(),R.anim.hide_to_right);
    }

    @Override
    public View onCreatePopupView() {
        return createPopupById(R.layout.layout_intro_popup);
    }

    @Override
    public View initAnimaView() {
        return findViewById(R.id.lip_introimage);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        AppPref.setAlreadyRun(getContext());
    }
}
