package com.sang.peoplan;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

/**
 * Created by Sangjun on 2018-03-26.
 */

public class WidthSquareImageView extends AppCompatImageView {
    public WidthSquareImageView(Context context) {
        super(context);
    }

    public WidthSquareImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public WidthSquareImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = getMeasuredWidth();
        setMeasuredDimension(width, width);
    }
}
