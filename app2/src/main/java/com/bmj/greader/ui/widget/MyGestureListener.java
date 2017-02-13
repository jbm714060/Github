package com.bmj.greader.ui.widget;

/**
 * Created by Administrator on 2016/12/28 0028.
 */
import android.content.Context;
import android.util.Log;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
//创建手势侦听对象
public class MyGestureListener extends SimpleOnGestureListener {

    private Context mContext;
    MyGestureListener(Context context) {
          mContext = context;
    }

     @Override
     //按下触摸屏按下时立刻触发
     public boolean onDown(MotionEvent e) {
        return false;
     }

     // 短按，触摸屏按下片刻后抬起，会触发这个手势，如果迅速抬起则不会
     @Override
     public void onShowPress(MotionEvent e) {

     }

     //释放，手指离开触摸屏时触发(长按、滚动、滑动时，不会触发这个手势)
     @Override
     public boolean onSingleTapUp(MotionEvent e) {
        return false;
     }
    // 滑动，按下后滑动
    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    // 长按，触摸屏按下后既不抬起也不移动，过一段时间后触发
    @Override
    public void onLongPress(MotionEvent e) {
    }

    // 滑动，触摸屏按下后快速移动并抬起，会先触发滚动手势，跟着触发一个滑动手势
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,float velocityY) {
        return false;
    }

    // 双击，手指在触摸屏上迅速点击第二下时触发
    @Override
    public boolean onDoubleTap(MotionEvent e) {
        return false;
    }

    // 双击后按下跟抬起各触发一次
    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        return false;
    }

    // 单击
    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        return false;
    }

}
