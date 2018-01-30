package com.sang.peoplan;

import android.content.Context;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import org.joda.time.LocalDate;

import java.text.SimpleDateFormat;


/**
 * Created by Sangjun on 2018-01-06.
 */

public class PlannerView extends ConstraintLayout {
    TextView yearText;
    TextView monthText;
    LinearLayout calendarSelectLayout;
    ConstraintLayout calendarSelectView;
    boolean isCalendarSelectClicked;

    ViewPager viewPager;

    Context context;

    Button notificationButton;
    Button calendarSelectButton;

    boolean isExistToday;
    Button todayButton;

    Button previousButton;
    Button nextButton;

    LocalDate date;

    private DayScheduleDialog mDayScheduleDialog;


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
        this.date = localDate;
        removeAllViews();
        this.isExistToday = false;

        String infService = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(infService);

        final View view = inflater.inflate(R.layout.planner_view, this, false);
        addView(view);

        if(this.date != null) {
            if(isExistToday(this.date)) {
                this.isExistToday = true;
            }
            yearText = view.findViewById(R.id.calendar_year_text);
            monthText = view.findViewById(R.id.calendar_month_text);

            viewPager = view.findViewById(R.id.calendar_viewpager);
            viewPager.setAdapter(new CalendarPagerAdapter(this.date));
            viewPager.setCurrentItem(1);
            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                CalendarPagerAdapter calendarPagerAdapter;

                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    if(position == 0) {
                        date = date.minusMonths(1);
                        calendarPagerAdapter = new CalendarPagerAdapter(date);
                    } else if(position == 2) {
                        date = date.plusMonths(1);
                        calendarPagerAdapter = new CalendarPagerAdapter(date);
                    }
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                    if(state == ViewPager.SCROLL_STATE_IDLE) {
                        isExistToday = false;

                        viewPager.setAdapter(calendarPagerAdapter);
                        viewPager.setCurrentItem(1);
                        yearText.setText(date.year().getAsText());
                        monthText.setText(String.valueOf(date.monthOfYear().get()));

                        if(isExistToday(date)) {
                            isExistToday = true;
                        }

                        if(!isExistToday) {
                            todayButton.setVisibility(View.VISIBLE);
                        } else {
                            todayButton.setVisibility(View.GONE);
                        }
                    }
                }
            });

            calendarSelectView = view.findViewById(R.id.calendar_select_view); // number picker부분
            calendarSelectView.setOnClickListener(new OnClickListener() { // 검은배경 클릭시
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

            if(!this.isExistToday) {
                todayButton.setVisibility(View.VISIBLE);
            }
        }

    }

    public class CalendarPagerAdapter extends PagerAdapter {
        LocalDate[] localDates;
        int size;

        public CalendarPagerAdapter(LocalDate localDate) {
            this.size = 3;
            LocalDate date = localDate.withDayOfMonth(1);
            localDates = new LocalDate[this.size];
            localDates[0] = date.minusMonths(1);
            localDates[1] = date;
            localDates[2] = date.plusMonths(1);
         }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = LayoutInflater.from(context).inflate(R.layout.planner_recyclerview, null);

            RecyclerView recyclerView = view.findViewById(R.id.calendar_recyclerview);
            recyclerView.setLayoutManager(new GridLayoutManager(context, 7, LinearLayoutManager.VERTICAL, false));
            recyclerView.setAdapter(new CalendarRecyclerViewAdapter(localDates[position]));
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((ConstraintLayout) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public int getCount() {
            return this.size;
        }
    }

    public class CalendarRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        public LocalDate calendar;
        private int startDay;
        private int lastDay;
        private int count;

        private int calendarCount;
        private int plusCount;

        LocalDate previousCalendar;
        LocalDate nextCalendar;

        Context mContext;

        public CalendarRecyclerViewAdapter(LocalDate localDate) {
            this.calendar = localDate;
            this.startDay = this.calendar.getDayOfWeek() == 7 ? 0 : this.calendar.getDayOfWeek(); // 1일의 요일
            this.lastDay = this.calendar.toDateTimeAtStartOfDay().dayOfMonth().withMaximumValue().getDayOfWeek() == 7 ? 0 : this.calendar.toDateTimeAtStartOfDay().dayOfMonth().withMaximumValue().getDayOfWeek(); // 마지막날 요일(일요일은 7)
            this.calendarCount = this.calendar.dayOfMonth().withMaximumValue().getDayOfMonth();
            this.count = startDay + (6 - lastDay) + this.calendarCount;

            this.plusCount = 0;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            mContext = parent.getContext();
            View view = LayoutInflater.from(context).inflate(R.layout.planner_item, parent, false);
            view.setLayoutParams(new LinearLayout.LayoutParams(parent.getWidth() / 7, parent.getHeight() / (this.count / 7)));
            final ViewHolder viewHolder = new ViewHolder(view);

            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    DayScheduleDialog dialog = new DayScheduleDialog(context);
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
                    String date = dateFormat.format(viewHolder.getLocalDate().toDate());
                    dialog.setDay(date);
                    dialog.show();
                }
            }); // item click listener

            return viewHolder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ViewHolder viewHolder = (ViewHolder) holder;

            if (position < startDay) {// 전달
                this.previousCalendar = this.calendar.minusDays(startDay - position);
                viewHolder.setLocalDate(this.previousCalendar);
                viewHolder.dateText.setText(this.previousCalendar.dayOfMonth().getAsText());
                viewHolder.dateText.setTextColor(Color.parseColor("#A9A9A9"));
                if (isExistToday(this.previousCalendar)) {
                    viewHolder.dateText.setBackgroundResource(R.drawable.ic_highlight_24dp);
                    isExistToday = true;
                }
            } else if (position >= startDay + this.calendarCount) { // 다음달
                this.nextCalendar = this.calendar.plusDays(plusCount++);
                viewHolder.setLocalDate(this.nextCalendar);
                viewHolder.dateText.setText(this.nextCalendar.dayOfMonth().getAsText());
                viewHolder.dateText.setTextColor(Color.parseColor("#A9A9A9"));
                if (isExistToday(this.nextCalendar)) {
                    viewHolder.dateText.setBackgroundResource(R.drawable.ic_highlight_24dp);
                    isExistToday = true;
                }
            } else { // 현재달
                viewHolder.setLocalDate(this.calendar);
                viewHolder.dateText.setText(this.calendar.dayOfMonth().getAsText());
                viewHolder.dateText.setTextColor(Color.parseColor("#000000"));
                if (isExistToday(this.calendar)) {
                    viewHolder.dateText.setBackgroundResource(R.drawable.ic_highlight_24dp);
                    viewHolder.dateText.setTextColor(Color.parseColor("#ffffff"));
                    isExistToday = true;
                }
                this.calendar = this.calendar.plusDays(1);
            }
        }

        @Override
        public int getItemCount() {
            return this.count;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView dateText;
            LocalDate localDate;

            public ViewHolder(View itemView) {
                super(itemView);

                dateText = itemView.findViewById(R.id.planner_item_day);
            }

            public void setLocalDate(LocalDate localDate) {
                this.localDate = localDate;
            }

            public LocalDate getLocalDate() {
                return this.localDate;
            }
        }
    }

    private boolean isExistToday(LocalDate date) {
        int[] todays = new LocalDate().getValues(); // size : 3 [0] = year, [1] = month, [2] = day
        int[] dates = date.getValues();

        return todays[0] == dates[0] && todays[1] == dates[1] && todays[2] == dates[2];
    }
}
