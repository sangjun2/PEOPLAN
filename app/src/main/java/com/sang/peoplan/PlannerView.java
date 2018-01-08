package com.sang.peoplan;

import android.content.Context;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import org.joda.time.LocalDate;


/**
 * Created by Sangjun on 2018-01-06.
 */

public class PlannerView extends ConstraintLayout {
    TextView yearText;
    TextView monthText;
    LinearLayout calendarSelectLayout;
    ConstraintLayout calendarSelectView;
    boolean isCalendarSelectClicked;
    GridView calendarView;
    Context context;

    Button notificationButton;
    Button calendarSelectButton;

    boolean isExistToday;
    Button todayButton;

    Button previousButton;
    Button nextButton;

    public PlannerView(Context context) {
        super(context);
        this.context = context;
        this.isCalendarSelectClicked = false;
    }

    public PlannerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        this.isCalendarSelectClicked = false;
    }

    public void initView(final LocalDate localDate) {
        removeAllViews();
        this.isExistToday = false;

        String infService = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(infService);

        final View view = inflater.inflate(R.layout.planner_view, this, false);
        addView(view);

        if(localDate != null) {
            if(getToday(new LocalDate(), localDate)) {
                this.isExistToday = true;
            }
            yearText = view.findViewById(R.id.calendar_year_text);
            monthText = view.findViewById(R.id.calendar_month_text);
            calendarView = view.findViewById(R.id.calendar_view);
            calendarSelectView = view.findViewById(R.id.calendar_select_view);
            calendarSelectView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });

            notificationButton = view.findViewById(R.id.calendar_notification_bt);

            calendarSelectLayout = view.findViewById(R.id.calendar_select_layout);
            calendarSelectLayout.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    final NumberPicker yearPicker = view.findViewById(R.id.year_picker);
                    final NumberPicker monthPicker = view.findViewById(R.id.month_picker);

                    calendarSelectButton = view.findViewById(R.id.calendar_select_bt);
                    calendarSelectButton.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            isCalendarSelectClicked = false;
                            calendarSelectView.setVisibility(View.GONE);
                            initView(new LocalDate(yearPicker.getValue(), monthPicker.getValue(), 1));
                        }
                    });

                    if(!isCalendarSelectClicked) {
                        isCalendarSelectClicked = true;

                        notificationButton.setVisibility(View.GONE);
                        calendarSelectButton.setVisibility(View.VISIBLE);

                        calendarSelectView.setVisibility(View.VISIBLE);
                        calendarSelectView.setAlpha(0.0f);
                        calendarSelectView.animate().alpha(1.0f);

                        monthPicker.setWrapSelectorWheel(false);
                        monthPicker.setMinValue(1);
                        monthPicker.setMaxValue(12);
                        monthPicker.setValue(localDate.monthOfYear().get());

                        yearPicker.setWrapSelectorWheel(false);
                        yearPicker.setMinValue(2000);
                        yearPicker.setMaxValue(2099);
                        yearPicker.setValue(localDate.year().get());
                    } else {
                        calendarSelectView.animate().alpha(0.0f);
                        calendarSelectView.setVisibility(View.GONE);
                        isCalendarSelectClicked = false;

                        notificationButton.setVisibility(View.VISIBLE);
                        calendarSelectButton.setVisibility(View.GONE);
                    }
                }
            });

            previousButton = view.findViewById(R.id.calendar_previous_bt);
            previousButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    initView(localDate.minusMonths(1));
                }
            });
            nextButton = view.findViewById(R.id.calendar_next_bt);
            nextButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    initView(localDate.plusMonths(1));
                }
            });

            todayButton = view.findViewById(R.id.calendar_today_bt);
            todayButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    initView(new LocalDate());
                }
            });

            yearText.setText(localDate.year().getAsText());
            monthText.setText(String.valueOf(localDate.monthOfYear().get()));
            calendarView.setAdapter(new CalendarAdapter(localDate));

            if(!this.isExistToday) {
                todayButton.setVisibility(View.VISIBLE);
            }
        }

    }


    public class CalendarAdapter extends BaseAdapter {
        public LocalDate calendar;
        private int startDay;
        private int lastDay;
        private int count;

        private int currentCount;
        private int plusCount;

        LocalDate previousCalendar;
        LocalDate nextCalendar;


        public CalendarAdapter(LocalDate localDate) {
            this.calendar = new LocalDate(localDate.year().get(), localDate.monthOfYear().get(), 1);
            this.startDay = this.calendar.getDayOfWeek() == 7 ? 0 : this.calendar.getDayOfWeek(); // 1일의 요일
            this.lastDay = this.calendar.toDateTimeAtStartOfDay().dayOfMonth().withMaximumValue().getDayOfWeek() == 7 ? 0 : this.calendar.toDateTimeAtStartOfDay().dayOfMonth().withMaximumValue().getDayOfWeek(); // 마지막날 요일(일요일은 7)
            this.count = startDay + (6 - lastDay) + this.calendar.dayOfMonth().withMaximumValue().getDayOfMonth();

            this.currentCount = 0;
            this.plusCount = 0;
        }

        @Override
        public int getCount() {
            return this.count;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View v = LayoutInflater.from(context).inflate(R.layout.planner_item, null);
            TextView dateText = v.findViewById(R.id.planner_item_day);
            LinearLayout dateLayout = v.findViewById(R.id.planner_item_layout);
            v.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, viewGroup.getHeight() / (this.count / 7)));

            LocalDate today = new LocalDate();

            if(i < startDay) {// 전달
                this.previousCalendar = this.calendar.minusDays(startDay - i);
                dateText.setText(this.previousCalendar.dayOfMonth().getAsText());
                if(getToday(today, this.previousCalendar)) {
                    dateText.setBackgroundResource(R.drawable.ic_highlight_24dp);
                    isExistToday = true;
                }
            } else if(i >= startDay + this.calendar.toDateTimeAtStartOfDay().dayOfMonth().withMaximumValue().getDayOfMonth()) { // 다음달
                this.nextCalendar = this.calendar.plusMonths(1).plusDays(plusCount++);
                dateText.setText(this.nextCalendar.dayOfMonth().getAsText());
                if(getToday(today, this.nextCalendar)) {
                    dateText.setBackgroundResource(R.drawable.ic_highlight_24dp);
                    isExistToday = true;
                }
            } else { // 현재달
                dateText.setText(String.valueOf(this.calendar.dayOfMonth().get()));
                dateText.setTextColor(Color.parseColor("#000000"));
                if(getToday(today, this.calendar)) {
                    dateText.setBackgroundResource(R.drawable.ic_highlight_24dp);
                    dateText.setTextColor(Color.parseColor("#ffffff"));
                    isExistToday = true;
                }

                this.calendar = this.calendar.plusDays(1);
            }

            return v;
        }
    }

    private boolean getToday(LocalDate today, LocalDate date) {
        int[] todays = today.getValues(); // size : 3 [0] = year, [1] = month, [2] = day
        int[] dates = date.getValues();

        return todays[0] == dates[0] && todays[1] == dates[1] && todays[2] == dates[2];
    }
}