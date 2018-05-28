package com.sang.peoplan;

import android.content.Context;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.Scroller;
import android.widget.TextView;

import com.nex3z.notificationbadge.NotificationBadge;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


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
    NotificationBadge notificationBadge;

    Button calendarSelectButton;

    boolean isExistToday;
    Button todayButton;

    Button previousButton;
    Button nextButton;

    LocalDate date;

    HashMap<String, ArrayList<Event>> hashMap;

    private DayScheduleDialog mDayScheduleDialog;

    PlannerFragment plannerFragment;

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

    public void initView(final LocalDate localDate, final PlannerFragment plannerFragment) {
        this.date = localDate;
        removeAllViews();
        this.isExistToday = false;

        this.plannerFragment = plannerFragment;

        String infService = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(infService);

        final View view = inflater.inflate(R.layout.planner_view, this, false);
        addView(view);

        hashMap = new HashMap<>();
        for(Map.Entry<String, Event> entry : SplashActivity.EVENT_LIST.entrySet()) {
            Event event = entry.getValue();

            DateTime start = new DateTime(event.getStart().getTime());
            DateTime end = new DateTime(event.getEnd().getTime());

            SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");

            int period = (int) ((end.toLocalDate().toDate().getTime() - start.toLocalDate().toDate().getTime()) / (24 * 60 * 60 * 1000)); // 날짜 사이 구간
            if(period == 0) {
                if(!hashMap.containsKey(format.format(start.toDate()))) {
                    hashMap.put(format.format(start.toDate()), new ArrayList<Event>());
                }
                hashMap.get(format.format(start.toDate())).add(event);
            } else {
                for(int i = 0; i <= period; i++) {
                    DateTime dateTime = start.plusDays(i);
                    if(!hashMap.containsKey(format.format(dateTime.toDate()))) {
                        hashMap.put(format.format(dateTime.toDate()), new ArrayList<Event>());
                    }
                    hashMap.get(format.format(dateTime.toDate())).add(event);
                }
            }
        }

        todayButton = view.findViewById(R.id.calendar_today_bt);
        todayButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                date = new LocalDate();
                viewPager.setCurrentItem(findIndex());
                setCalendarSelectLayout();
            }
        });


        if(this.date != null) {
            yearText = view.findViewById(R.id.calendar_year_text);
            monthText = view.findViewById(R.id.calendar_month_text);

            viewPager = view.findViewById(R.id.calendar_viewpager);
            viewPager.setAdapter(new CalendarPagerAdapter(this.date));
            viewPager.setCurrentItem(findIndex());
            setCalendarSelectLayout();

            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    if(position < findIndex()) {
                        date = date.minusMonths(1);
                    } else if(position > findIndex()) {
                        date = date.plusMonths(1);
                    }
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                    if(state == ViewPager.SCROLL_STATE_IDLE) {
                        viewPager.setCurrentItem(findIndex());
                        setCalendarSelectLayout();
                    }
                }
            });

            calendarSelectView = view.findViewById(R.id.calendar_select_view); // number picker부분
            calendarSelectView.setOnClickListener(new OnClickListener() { // 검은배경 클릭시
                @Override
                public void onClick(View view) {

                }
            });


            notificationButton = view.findViewById(R.id.planner_notification_bt);
            notificationButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    plannerFragment.onNotificationButtonClicked();
                }
            });

            notificationBadge = view.findViewById(R.id.planner_notification_badge);
            notificationBadge.setNumber(0);

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
                            initView(new LocalDate(yearPicker.getValue(), monthPicker.getValue(), 1), plannerFragment);
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
                    date = date.minusMonths(1);
                    viewPager.setCurrentItem(findIndex());
                    setCalendarSelectLayout();
                }
            });
            nextButton = view.findViewById(R.id.calendar_next_bt);
            nextButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    date = date.plusMonths(1);
                    viewPager.setCurrentItem(findIndex());
                    setCalendarSelectLayout();
                }
            });
        }

    }


    public class CalendarPagerAdapter extends PagerAdapter {
        int size;
        int maxYear = 2099;
        int minYear = 2000;

        LocalDate minDate;
        LocalDate date;

        public CalendarPagerAdapter(LocalDate localDate) {
            minDate = new LocalDate(2000, 1, 1);
            this.size = (maxYear - minYear + 1) * 12;
            this.date = localDate.withDayOfMonth(1);
        }



        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = LayoutInflater.from(context).inflate(R.layout.planner_recyclerview, null);

            RecyclerView recyclerView = view.findViewById(R.id.calendar_recyclerview);
            recyclerView.setLayoutManager(new GridLayoutManager(context, 7, LinearLayoutManager.VERTICAL, false));
            recyclerView.setAdapter(new CalendarRecyclerViewAdapter(minDate.plusMonths(position)));
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

            View.OnClickListener listener = new OnClickListener() {
                @Override
                public void onClick(View view) {
                    DayScheduleDialog dialog = new DayScheduleDialog();
                    dialog.myEvent = viewHolder.adapter.events;
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
                    String date = dateFormat.format(viewHolder.getLocalDate().toDate());
                    dialog.day = date;
                    dialog.plannerFragment = plannerFragment;

                    FragmentManager fragmentManager = ((AppCompatActivity) mContext).getSupportFragmentManager();
                    dialog.show(fragmentManager, "DIALOG");
                }
            };

            view.setOnClickListener(listener); // item click listener
            viewHolder.dateListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    DayScheduleDialog dialog = new DayScheduleDialog();
                    dialog.myEvent = viewHolder.adapter.events;
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
                    String date = dateFormat.format(viewHolder.getLocalDate().toDate());
                    dialog.day = date;
                    dialog.plannerFragment = plannerFragment;

                    FragmentManager fragmentManager = ((AppCompatActivity) mContext).getSupportFragmentManager();
                    dialog.show(fragmentManager, "DIALOG");
                }
            });

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
            } else if (position >= startDay + this.calendarCount) { // 다음달
                this.nextCalendar = this.calendar.plusDays(plusCount++);
                viewHolder.setLocalDate(this.nextCalendar);
                viewHolder.dateText.setText(this.nextCalendar.dayOfMonth().getAsText());
                viewHolder.dateText.setTextColor(Color.parseColor("#A9A9A9"));
            } else { // 현재달
                viewHolder.setLocalDate(this.calendar);
                viewHolder.dateText.setText(this.calendar.dayOfMonth().getAsText());
                if(this.calendar.getDayOfWeek() == 6) { // 토요일
                    viewHolder.dateText.setTextColor(Color.BLUE);
                } else if(this.calendar.getDayOfWeek() == 7) { // 일요일
                    viewHolder.dateText.setTextColor(Color.RED);
                } else {
                    viewHolder.dateText.setTextColor(Color.parseColor("#000000"));

                }
                if (isExistToday(this.calendar)) {
                    viewHolder.dateText.setBackgroundResource(R.drawable.ic_highlight_24dp);
                    viewHolder.dateText.setTextColor(Color.parseColor("#ffffff"));
                    isExistToday = true;
                }

                for(Map.Entry<String, ArrayList<Event>> entry : hashMap.entrySet()) {
                    ArrayList<Event> events = entry.getValue();

                    for(int i = 0; i < events.size(); i++) {
                        Event event = events.get(i);
                        int repeat = event.getRepeat();

                        DateTime start = new DateTime(event.getStart().getTime());
                        DateTime end = new DateTime(event.getEnd().getTime());

                        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
                        if(entry.getKey().equals(format.format(this.calendar.toDate()))) {
                            viewHolder.adapter.events.add(event);
                        } else {
                            if(repeat != CreateScheduleActivity.REPEAT_NONE) {
                                LocalDate currentDate = this.calendar;
                                int year = Integer.parseInt(entry.getKey().substring(0, 4));
                                int month = Integer.parseInt(entry.getKey().substring(4, 6));
                                int day = Integer.parseInt(entry.getKey().substring(6, 8));
                                LocalDate eventDate = new LocalDate(year, month, day);
                                int period = (int) ((currentDate.toDate().getTime() - eventDate.toDate().getTime()) / (24 * 60 * 60 * 1000)); // 날짜 사이 구간

                                switch (repeat) {
                                    case CreateScheduleActivity.REPEAT_EVERYDAY:
                                        if(this.calendar.toDate().getTime() >= start.toDate().getTime() && this.calendar.toDate().getTime() <= end.toDate().getTime()) {
                                            viewHolder.adapter.events.add(event);
                                        }
                                        break;
                                    case CreateScheduleActivity.REPEAT_EVERYWEEK:
                                        if(period > 0 && period % 7 == 0) {
                                            viewHolder.adapter.events.add(event);
                                        }
                                        break;
                                    case CreateScheduleActivity.REPEAT_EVERYTWOWEEK:
                                        if(period > 0 && period % 14 == 0) {
                                            viewHolder.adapter.events.add(event);
                                        }
                                        break;
                                    case CreateScheduleActivity.REPEAT_EVERYMONTH:
                                        if(period > 0 && currentDate.getDayOfMonth() == eventDate.getDayOfMonth()) {
                                            viewHolder.adapter.events.add(event);
                                        }
                                        break;
                                    case CreateScheduleActivity.REPEAT_EVERYYEAR:
                                        if(period > 0 && currentDate.getMonthOfYear() == eventDate.getMonthOfYear() && currentDate.getDayOfMonth() == eventDate.getDayOfMonth()) {
                                            viewHolder.adapter.events.add(event);
                                        }
                                        break;
                                }
                            }
                        }
                    }
                }

                viewHolder.adapter.notifyDataSetChanged();


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
            ListView dateListView;
            DateListViewAdapter adapter;

            public ViewHolder(View itemView) {
                super(itemView);

                dateText = itemView.findViewById(R.id.planner_item_day);
                dateListView = itemView.findViewById(R.id.planner_item_listview);
                adapter = new DateListViewAdapter();
                dateListView.setAdapter(adapter);
            }

            public void setLocalDate(LocalDate localDate) {
                this.localDate = localDate;
            }

            public LocalDate getLocalDate() {
                return this.localDate;
            }
        }

        public class DateListViewAdapter extends BaseAdapter {
            ArrayList<Event> events;

            public DateListViewAdapter() {
                this.events = new ArrayList<>();
            }

            @Override
            public int getCount() {
                return this.events.size();
            }

            @Override
            public Object getItem(int i) {
                return this.events.get(i);
            }

            @Override
            public long getItemId(int i) {
                return i;
            }

            @Override
            public View getView(int i, View view, ViewGroup viewGroup) {
                Event event = this.events.get(i);

                View item = LayoutInflater.from(mContext).inflate(R.layout.plan_item, null, false);
                item.setBackgroundColor(Color.parseColor("#" + event.getColor()));
                TextView itemText = item.findViewById(R.id.plan_item_text);

                itemText.setText(event.getName());

                return item;
            }
        }
    }

    private boolean isExistToday(LocalDate date) {
        int[] todays = new LocalDate().getValues(); // size : 3 [0] = year, [1] = month, [2] = day
        int[] dates = date.getValues();

        return todays[0] == dates[0] && todays[1] == dates[1] && todays[2] == dates[2];
    }

    private int findIndex() {
        return ((this.date.getYear() - 2000) * 12) + this.date.getMonthOfYear() - 1;
    }

    private void setCalendarSelectLayout() {
        yearText.setText(date.year().getAsText());
        monthText.setText(String.valueOf(date.monthOfYear().get()));

        isExistToday = isExistToday(date);

        if(!isExistToday) {
            todayButton.setVisibility(View.VISIBLE);
        } else {
            todayButton.setVisibility(View.GONE);
        }
    }
}
