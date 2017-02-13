package com.bmj.greader.ui.module.compz;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.widget.TextView;

import com.bmj.greader.R;
import razerdp.basepopup.BasePopupWindow;

/**
 * Created by Administrator on 2016/12/26 0026.
 */
public class TextPopup extends BasePopupWindow{
    private TextView mAmiText;

    public TextPopup(Activity context,String content) {
        super(context);
        setDismissWhenTouchOuside(false);
        mAmiText = (TextView)findViewById(R.id.ami_text);
        mAmiText.setText(content);
    }

    @Override
    public View getClickToDismissView() {
        return null;
    }

    @Override
    protected Animation initShowAnimation() {
        return null;
    }

    @Override
    protected Animator initShowAnimator() {
        ObjectAnimator alphaAnimatorShow = ObjectAnimator.ofFloat(mAnimaView,"alpha",0,1f).setDuration(200);
        ObjectAnimator translationYAnimator = ObjectAnimator.ofFloat(mAnimaView,"translationY",50f,0).setDuration(200);
        ObjectAnimator alphaAnimatorHide = ObjectAnimator.ofFloat(mAnimaView,"alpha",1,1,0).setDuration(300);

        AnimatorSet set = new AnimatorSet();
        set.playTogether(alphaAnimatorShow,translationYAnimator);
        set.play(alphaAnimatorHide).after(alphaAnimatorShow);
        return set;
    }

    @Override
    public View initAnimaView() {
        return findViewById(R.id.ami_text);
    }

    @Override
    public View onCreatePopupView() {
        return LayoutInflater.from(getContext()).inflate(R.layout.text_popup,null);
    }
}
