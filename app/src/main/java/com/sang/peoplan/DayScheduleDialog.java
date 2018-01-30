package com.sang.peoplan;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

/**
 * Created by sanginLee on 2018-01-10.
 */

public class DayScheduleDialog extends Dialog {
    public String day;
    public Context mContext;

    public DayScheduleDialog(@NonNull Context context) {
        super(context);
        mContext = context;
    }

    public DayScheduleDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        mContext = context;
    }

    protected DayScheduleDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        mContext = context;
    }

    public void setDay(String day) {
        this.day = day;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_day_schedule);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        TextView dayText = findViewById(R.id.dialog_day_text);
        FloatingActionButton fab = findViewById(R.id.dialog_floating_bt);

        dayText.setText(this.day);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                Activity activity = (Activity) mContext;

                Intent intent = new Intent(getContext(), CreateScheduleActivity.class);
                intent.putExtra("day", day);
                getContext().startActivity(intent);
                activity.overridePendingTransition(R.anim.anim_slide_in_bottom, R.anim.anim_noanim);
            }
        });
    }
}
