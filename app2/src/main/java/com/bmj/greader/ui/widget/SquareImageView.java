package com.bmj.greader.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.AbsListView;
import android.widget.ImageView;

/**
 * Created by Administrator on 2016/12/26 0026.
 */
public class SquareImageView extends ImageView {

    AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(0,0);

    public SquareImageView(Context context) {
        super(context);
    }

    public SquareImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        layoutParams.width = width;
        layoutParams.height = width;
        this.setLayoutParams(layoutParams);
        //super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }
}
