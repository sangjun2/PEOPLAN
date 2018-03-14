package com.sang.peoplan;

import android.app.Activity;
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
import org.joda.time.LocalTime;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by Sangjun on 2018-02-05.
 */

public class DayPicker extends LinearLayout {
    CreateScheduleActivity createScheduleActivity;

    LocalDate localDate;

    NumberPicker dayPicker;
    NumberPicker typePicker;
    NumberPicker hourPicker;
    NumberPicker minutePicker;

    LocalDate pickerDate;

    public DayPicker(Context context) {
        super(context);
        localDate = null;
        createScheduleActivity = ((CreateScheduleActivity) context);
    }

    public DayPicker(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        localDate = null;
        createScheduleActivity = ((CreateScheduleActivity) context);
    }

    public void initView(String day, int hour) {
        initView(day);
        if(hour < 12) {
            typePicker.setValue(0);
            hourPicker.setValue(hour);
        } else if(hour >= 12 && hour < 24){
            typePicker.setValue(1);
            hourPicker.setValue(hour - 12);
        } else {
            dayPicker.setValue(dayPicker.getValue() + 1);
            typePicker.setValue(0);
            hourPicker.setValue(12);
        }

        if(getId() == R.id.event_start_daypicker) {
            createScheduleActivity.setEventStartText();
        } else if(getId() == R.id.event_end_daypicker) {
            createScheduleActivity.setEventEndText();
        }

        typePicker.setEnabled(true);
        hourPicker.setEnabled(true);
        minutePicker.setEnabled(true);
    }

    public void initView(String day) {
        String[] days = day.split("\\.");
        localDate = new LocalDate(Integer.parseInt(days[0]), Integer.parseInt(days[1]), Integer.parseInt(days[2]));
        pickerDate = localDate;

        String infService = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(infService);

        final View view = inflater.inflate(R.layout.day_picker, this, false);

        dayPicker = view.findViewById(R.id.day_picker_day);
        typePicker = view.findViewById(R.id.day_picker_type);
        hourPicker = view.findViewById(R.id.day_picker_hour);
        minutePicker = view.findViewById(R.id.day_picker_minute);

        dayPicker.setWrapSelectorWheel(false);
        dayPicker.setMinValue(0);
        dayPicker.setMaxValue(100);
        setDayPicker(pickerDate);

        dayPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                if(getId() == R.id.event_start_daypicker) {
                    createScheduleActivity.setEventStartText();
                    createScheduleActivity.syncEventEndText();
                } else if(getId() == R.id.event_end_daypicker) {
                    createScheduleActivity.setEventEndText();
                }
            }
        });
        dayPicker.setOnScrollListener(new NumberPicker.OnScrollListener() {
            boolean isChanged;
            @Override
            public void onScrollStateChange(NumberPicker numberPicker, int i) {
                if(i == 2) {
                    isChanged = true;
                } else if(i == 0) {
                    if(isChanged) {
                        if(numberPicker.getValue() < 50) {
                            pickerDate = pickerDate.minusDays(50 - numberPicker.getValue());
                        } else if(numberPicker.getValue() == 50) {

                        } else {
                            pickerDate = pickerDate.plusDays(numberPicker.getValue() - 50);
                        }

                        setDayPicker(pickerDate);
                        isChanged = false;
                    }
                }
            }
        });

        typePicker.setWrapSelectorWheel(false);
        typePicker.setMinValue(0);
        typePicker.setMaxValue(1);
        String[] typeDisplayStrings = {"오전", "오후"};
        typePicker.setDisplayedValues(typeDisplayStrings);
        typePicker.setValue(0);
        typePicker.setEnabled(false);
        typePicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                if(getId() == R.id.event_start_daypicker) {
                    createScheduleActivity.setEventStartText();
                    createScheduleActivity.syncEventEndText();
                } else if(getId() == R.id.event_end_daypicker) {
                    createScheduleActivity.setEventEndText();
                }
            }
        });

        hourPicker.setMinValue(1);
        hourPicker.setMaxValue(12);
        hourPicker.setValue(12);
        hourPicker.setEnabled(false);
        hourPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) { // i : old val, i1 : new val
                if(i == 12 && i1 == 1) {
                    if(typePicker.getValue() == 0) {
                        typePicker.setValue(1);
                    } else if(typePicker.getValue() == 1) {
                        typePicker.setValue(0);
                    }
                }

                if(getId() == R.id.event_start_daypicker) {
                    createScheduleActivity.setEventStartText();
                    createScheduleActivity.syncEventEndText();
                } else if(getId() == R.id.event_end_daypicker) {
                    createScheduleActivity.setEventEndText();
                }
            }
        });

        minutePicker.setMinValue(0);
        minutePicker.setMaxValue(5);
        String[] minuteDisplayStrings = new String[6];
        for(int i = 0; i < minuteDisplayStrings.length; i++) {
            minuteDisplayStrings[i] = String.valueOf(i) + "0";
        }
        minutePicker.setDisplayedValues(minuteDisplayStrings);
        minutePicker.setValue(0);
        minutePicker.setEnabled(false);
        minutePicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                if(getId() == R.id.event_start_daypicker) {
                    createScheduleActivity.setEventStartText();
                    createScheduleActivity.syncEventEndText();
                } else if(getId() == R.id.event_end_daypicker) {
                    createScheduleActivity.setEventEndText();
                }
            }
        });

        addView(view);
        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.anim_alpha);
        startAnimation(animation);
    }

    public LocalDate getPickerDate() {
        if(dayPicker.getValue() < 50) {
            return pickerDate.minusDays(50 - dayPicker.getValue());
        } else if(dayPicker.getValue() == 50) {
            return pickerDate;
        } else {
            return pickerDate.plusDays(dayPicker.getValue() - 50);
        }
    }

    private void setDayPicker(LocalDate input) {
        String[] dayStrings = new String[101];
        for(int i = 0; i < dayStrings.length; i++) {
            LocalDate date = null;
            if(i < 50) {
                date = input.minusDays(50 - i);
                dayStrings[i] = String.valueOf(date.getMonthOfYear()) + "월 " + String.valueOf(date.getDayOfMonth()) + "일 " + getDayOfWeek(date.getDayOfWeek());
            } else if(i == 50) {
                date = input;
                dayStrings[i] = String.valueOf(input.getMonthOfYear()) + "월 " + String.valueOf(input.getDayOfMonth()) + "일 " + getDayOfWeek(input.getDayOfWeek());
            } else {
                date = input.plusDays(i - 50);
                dayStrings[i] = String.valueOf(date.getMonthOfYear()) + "월 " + String.valueOf(date.getDayOfMonth()) + "일 " + getDayOfWeek(date.getDayOfWeek());
            }

            if(date != null && isExistToday(date)) {
                dayStrings[i] = "오늘";
            }
        }

        dayPicker.setDisplayedValues(dayStrings);
        dayPicker.setValue(50);
    }

    private String getDayOfWeek(int dayOfWeek) {
        switch (dayOfWeek) {
            case 1:
                return "월";
            case 2:
                return "화";
            case 3:
                return "수";
            case 4:
                return "목";
            case 5:
                return "금";
            case 6:
                return "토";
            case 7:
                return "일";
            default:
                return "";
        }
    }

    private boolean isExistToday(LocalDate date) {
        int[] todays = new LocalDate().getValues(); // size : 3 [0] = year, [1] = month, [2] = day
        int[] dates = date.getValues();

        return todays[0] == dates[0] && todays[1] == dates[1] && todays[2] == dates[2];
    }
}
