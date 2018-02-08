package com.sang.peoplan;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.NumberPicker;

import org.joda.time.LocalDate;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by Sangjun on 2018-02-05.
 */

public class DayPicker extends LinearLayout {
    LocalDate localDate;

    NumberPicker dayPicker;
    NumberPicker typePicker;
    NumberPicker hourPicker;
    NumberPicker minutePicker;

    public DayPicker(Context context) {
        super(context);
        localDate = null;
    }

    public DayPicker(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        localDate = null;
    }


    public void initView(String day) {
        String[] days = day.split("\\.");
        localDate = new LocalDate(Integer.parseInt(days[0]), Integer.parseInt(days[1]), Integer.parseInt(days[2]));

        String infService = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(infService);

        final View view = inflater.inflate(R.layout.day_picker, this, false);

        dayPicker = view.findViewById(R.id.day_picker_day);
        typePicker = view.findViewById(R.id.day_picker_type);
        hourPicker = view.findViewById(R.id.day_picker_hour);
        minutePicker = view.findViewById(R.id.day_picker_minute);

        dayPicker.setWrapSelectorWheel(false);

        typePicker.setWrapSelectorWheel(false);
        typePicker.setMinValue(0);
        typePicker.setMaxValue(1);
        String[] typeDisplayStrings = {"오전", "오후"};
        typePicker.setDisplayedValues(typeDisplayStrings);
        typePicker.setValue(0);

        hourPicker.setMinValue(1);
        hourPicker.setMaxValue(12);
        hourPicker.setValue(12);

        minutePicker.setMinValue(0);
        minutePicker.setMaxValue(5);
        String[] minuteDisplayStrings = new String[6];
        for(int i = 0; i < minuteDisplayStrings.length; i++) {
            minuteDisplayStrings[i] = String.valueOf(i) + "0";
        }
        minutePicker.setDisplayedValues(minuteDisplayStrings);
        minutePicker.setValue(0);

        if(getChildCount() == 0) {
            addView(view);
            Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.anim_alpha);
            startAnimation(animation);
        } else {
            removeAllViews();
        }
    }
}
