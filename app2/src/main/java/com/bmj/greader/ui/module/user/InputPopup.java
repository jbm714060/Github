package com.bmj.greader.ui.module.user;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import com.bmj.greader.R;
import com.bmj.greader.common.util.InputMethodUtil;

import razerdp.basepopup.BasePopupWindow;

/**
 * Created by Administrator on 2016/12/21 0021.
 */
public class InputPopup extends BasePopupWindow implements View.OnClickListener{
    private Button mCancelButton;
    private Button mCompeleteButton;
    private EditText mInputEdittext;
    private OnTextCommitListener mListener;

    public InputPopup(Activity context,OnTextCommitListener listener) {
        super(context);
        mListener = listener;
        mCancelButton= (Button) findViewById(R.id.btn_cancel);
        mCompeleteButton= (Button) findViewById(R.id.btn_Compelete);
        mInputEdittext= (EditText) findViewById(R.id.ed_input);

        setAutoShowInputMethod(true);
        bindEvent();
    }

    @Override
    protected Animation initShowAnimation() {
        return null;
    }

    private void bindEvent() {
        mCancelButton.setOnClickListener(this);
        mCompeleteButton.setOnClickListener(this);
    }

    //=============================================================super methods


    @Override
    public Animator initShowAnimator() {
        return getDefaultSlideFromBottomAnimationSet();
    }

    @Override
    public EditText getInputView() {
        return mInputEdittext;
    }

    @Override
    public View getClickToDismissView() {
        return getPopupWindowView();
    }

    @Override
    public View onCreatePopupView() {
        return LayoutInflater.from(getContext()).inflate(R.layout.popup_input,null);
    }

    @Override
    public View initAnimaView() {
        return findViewById(R.id.popup_anima);
    }

    @Override
    public Animator initExitAnimator() {
        AnimatorSet set = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            set = new AnimatorSet();
            if (initAnimaView() != null) {
                set.playTogether(
                        ObjectAnimator.ofFloat(initAnimaView(), "translationY", 0, 250).setDuration(400),
                        ObjectAnimator.ofFloat(initAnimaView(), "alpha", 1, 0.4f).setDuration(250 * 3 / 2));
            }
        }
        return set;
    }

    //=============================================================click event
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_cancel:
                dismiss();
                break;
            case R.id.btn_Compelete:
                mListener.OnTextInputCompleted(mInputEdittext.getText().toString());
                dismiss();
                break;
            default:
                break;
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
        InputMethodUtil.hideSoftInput(mCompeleteButton);
    }

    public interface OnTextCommitListener{
        void OnTextInputCompleted(String text);
    }
}
