package com.bmj.greader.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.FloatMath;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.blankj.utilcode.utils.ScreenUtils;

/**
 * Created by Administrator on 2016/12/28 0028.
 */
public class LayoutContainImage extends FrameLayout{

    private ImageView mImageView;

    public LayoutContainImage(Context context) {
        super(context);
        init();
    }

    public LayoutContainImage(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LayoutContainImage(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        mImageView = (ImageView)getChildAt(0);
    }

    private float beforeDistance;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        /** 处理单点、多点触摸 **/
        switch (ev.getAction() & MotionEvent.ACTION_MASK) {
            // 多点触摸
            case MotionEvent.ACTION_POINTER_DOWN:
                if (ev.getPointerCount() == 2) {
                    beforeDistance = getDistance(ev);
                }
                break;

            case MotionEvent.ACTION_MOVE:
                float afterDistance = getDistance(ev);// 获取两点的距离

                float gapLenght = afterDistance - beforeDistance;// 变化的长度

                if (Math.abs(gapLenght) > 5f) {
                    float scale_temp = afterDistance / beforeDistance;// 求的缩放的比例
                    float primaryScale = mImageView.getScrollX();
                    //mImageView.setScaleX(primaryScale*scale_temp);
                    //mImageView.setScaleY(primaryScale*scale_temp);
                    mImageView.setScaleX(3);
                    mImageView.setScaleY(3);
                    beforeDistance = afterDistance;
                }
                break;
        }

        return super.dispatchTouchEvent(ev);
    }

    /** 获取两点的距离 **/
    float getDistance(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);

        return (float)Math.sqrt(x * x + y * y);
    }
}
