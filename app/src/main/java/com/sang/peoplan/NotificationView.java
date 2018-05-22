package com.sang.peoplan;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by Sangjun on 2018-05-11.
 */

public class NotificationView extends LinearLayout {
    public NotificationView(Context context) {
        super(context);
    }

    public NotificationView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public NotificationView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void initView() {
        String infService = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(infService);

        removeAllViews();

        final View view = inflater.inflate(R.layout.notification_view, this, false);
        addView(view);
    }
}
