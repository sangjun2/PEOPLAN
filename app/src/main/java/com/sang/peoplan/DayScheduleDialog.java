package com.sang.peoplan;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.WindowManager;

/**
 * Created by sanginLee on 2018-01-10.
 */

public class DayScheduleDialog extends Dialog {
    public DayScheduleDialog(@NonNull Context context) {
        super(context);
    }

    public DayScheduleDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected DayScheduleDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.8f;
        getWindow().setAttributes(lpWindow);

        setContentView(R.layout.dialog_day_schedule);


    }
}
