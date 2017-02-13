package com.bmj.greader.ui.module.repo;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.widget.TextView;

import com.bmj.greader.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import razerdp.basepopup.BasePopupWindow;

/**
 * Created by Administrator on 2016/12/28 0028.
 */
public class RepoUrlPopup extends BasePopupWindow{

    @BindView(R.id.rup_label_homepage)
    TextView mLabelHomepage;
    @BindView(R.id.rup_homepage)
    TextView mHomepage;

    @BindView(R.id.rup_label_repo)
    TextView mLabelRepo;
    @BindView(R.id.rup_repo)
    TextView mRepo;

    @BindView(R.id.rup_label_svn)
    TextView mLabelSvn;
    @BindView(R.id.rup_svn)
    TextView mSvn;

    @BindView(R.id.rup_label_clone)
    TextView mLabelClone;
    @BindView(R.id.rup_clone)
    TextView mClone;

    public RepoUrlPopup(Activity context,String homepage,String repourl,String clone,String svn){
        super(context);
        if(TextUtils.isEmpty(homepage)){
            mHomepage.setVisibility(View.GONE);
            mLabelHomepage.setVisibility(View.GONE);
        }else{
            mHomepage.setText(homepage);
        }

        if(TextUtils.isEmpty(repourl)){
            mRepo.setVisibility(View.GONE);
            mLabelRepo.setVisibility(View.GONE);
        }else{
            mRepo.setText(repourl);
        }

        if(TextUtils.isEmpty(clone)){
            mClone.setVisibility(View.GONE);
            mLabelClone.setVisibility(View.GONE);
        }else{
            mClone.setText(clone);
        }

        if(TextUtils.isEmpty(svn)){
            mSvn.setVisibility(View.GONE);
            mLabelSvn.setVisibility(View.GONE);
        }else{
            mSvn.setText(svn);
        }
    }

    @Override
    public View getClickToDismissView() {
        return getPopupWindowView();
    }

    @Override
    protected Animation initShowAnimation() {
        return null;
    }

    @Override
    protected Animator initShowAnimator() {
        ObjectAnimator alpha = ObjectAnimator.ofFloat(mAnimaView,"alpha",0,1f).setDuration(300);
        //ObjectAnimator scaleX = ObjectAnimator.ofFloat(mAnimaView,"scaleX",0,1f).setDuration(300);
        //ObjectAnimator scaleY = ObjectAnimator.ofFloat(mAnimaView,"scaleY",0,1f).setDuration(300);
        //ObjectAnimator rotationX1 = ObjectAnimator.ofFloat(mAnimaView,"rotationX",-45f,0f).setDuration(1000);
        //ObjectAnimator rotationX2 = ObjectAnimator.ofFloat(mAnimaView,"rotationX",180f,270f).setDuration(1000);
        //ObjectAnimator rotationX3 = ObjectAnimator.ofFloat(mAnimaView,"rotationX",270f,0f).setDuration(1000);
        AnimatorSet set = new AnimatorSet();
        set.playTogether(alpha);
        return set;
    }

    @Override
    protected Animator initExitAnimator() {
        ObjectAnimator alpha = ObjectAnimator.ofFloat(mAnimaView,"alpha",1f,0f).setDuration(300);
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(mAnimaView,"scaleX",1f,0f).setDuration(300);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(mAnimaView,"scaleY",1,0f).setDuration(300);

        AnimatorSet set = new AnimatorSet();
        set.playTogether(alpha,scaleX,scaleY);
        return set;
    }

    @Override
    public View onCreatePopupView() {
        View root = LayoutInflater.from(getContext()).inflate(R.layout.repo_urlinfo_popup,null);
        ButterKnife.bind(this,root);
        return root;
    }

    @Override
    public View initAnimaView() {
        return findViewById(R.id.rup_card);
    }
}
