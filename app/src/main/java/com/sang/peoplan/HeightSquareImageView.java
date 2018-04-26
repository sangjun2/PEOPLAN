package com.sang.peoplan;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

/**
 * Created by Sangjun on 2018-04-12.
 */

public class HeightSquareImageView extends AppCompatImageView {
    public HeightSquareImageView(Context context) {
        super(context);
    }

    public HeightSquareImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public HeightSquareImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int height = getMeasuredHeight();
        setMeasuredDimension(height, height);
    }
}