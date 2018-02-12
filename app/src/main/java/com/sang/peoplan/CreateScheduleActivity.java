package com.sang.peoplan;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import java.text.SimpleDateFormat;

public class CreateScheduleActivity extends AppCompatActivity {
    Switch isByDay;
    Switch isAllDay;
    EditText eventTitle;
    Button selectGroupButton;

    LinearLayout timeGroup;

    LinearLayout eventStart;
    LinearLayout eventEnd;
    LinearLayout repeatView;
    LinearLayout alarmSelect;
    EditText eventContent;
    Button confirm;
    TextView repeatConfirm;

    TextView eventStartText;
    TextView eventEndText;

    DayPicker eventStartNumberPicker;
    DayPicker eventEndNumberPicker;

    WeekdaysGroup weekdaysGroup;

    final int REQUESTCODE_GROUP = 100;
    final int REQUESTCODE_REPEAT = 200;
    final int REQUESTCODE_ALARM = 300;

    private String day = "";

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUESTCODE_REPEAT){
            if(resultCode == Activity.RESULT_OK){
                String repeat = data.getStringExtra("repeat");
                repeatConfirm.setText(repeat);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_schedule);

        Toolbar toolbar = findViewById(R.id.schedule_toolbar);
        TextView toolbarTitle = findViewById(R.id.confirm_toolbar_title);
        toolbarTitle.setText("일정 추가");
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        this.day = intent.getStringExtra("day");

        isByDay = findViewById(R.id.is_byday);
        isAllDay = findViewById(R.id.is_allday);
        eventTitle = findViewById(R.id.eventTitle);
        selectGroupButton = findViewById(R.id.selectGroupButton);
        eventStart = findViewById(R.id.eventStart);
        eventEnd = findViewById(R.id.eventEnd);
        repeatView = findViewById(R.id.repeatView);
        alarmSelect = findViewById(R.id.alarmSelect);
        eventContent = findViewById(R.id.eventContent);
        confirm = findViewById(R.id.confirm_toolbar_bt);
        repeatConfirm = findViewById(R.id.repeatConfirm);

        eventStartText = findViewById(R.id.eventStartConfirm);
        eventEndText = findViewById(R.id.eventEndConfirm);

        eventStartNumberPicker = findViewById(R.id.event_start_daypicker);
        eventEndNumberPicker = findViewById(R.id.event_end_daypicker);

        weekdaysGroup = findViewById(R.id.weekdays_group);
        weekdaysGroup.initView();

        timeGroup = findViewById(R.id.group_time);

        isByDay.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){ // 요일별
                    weekdaysGroup.setVisibility(View.VISIBLE);
                    timeGroup.setVisibility(View.GONE);
                }
                else{ // 날짜별
                    weekdaysGroup.setVisibility(View.GONE);
                    timeGroup.setVisibility(View.VISIBLE);
                }

                isAllDay.setChecked(true);
            }
        });

        isAllDay.setChecked(true);
        isAllDay.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                removeViews();

                eventStartText.setText(day);
                eventEndText.setText(day);

                /*
                if(b){ // 하루종일
                    eventStartText.setText(day);
                    eventEndText.setText(day);
                }
                else{ // not 하루종일
                    LocalTime localTime = new LocalTime();
                    int hour = localTime.getHourOfDay() + 1;
                    if(hour < 12) {
                        eventStartText.setText(day + " " + "오전 " + String.valueOf(hour) + ":00");
                        eventEndText.setText(day + " " + "오전 " + String.valueOf(hour + 1) + ":00");
                    } else {
                        eventStartText.setText(day + " " + "오후 " + String.valueOf(hour - 12) + ":00");
                        eventEndText.setText(day + " " + "오후 " + String.valueOf(hour + 1 - 12) + ":00");
                    }
                }
                */
            }
        });

        selectGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        eventStartText.setText(day);
        eventStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(eventStartNumberPicker.getChildCount() == 0) {
                    removePickerView(view);
                    if(isAllDay.isChecked()) {
                        eventStartNumberPicker.initView(day);
                    } else {
                        eventStartNumberPicker.initView(day, new LocalTime().getHourOfDay() + 1);
                    }
                } else {
                    eventStartNumberPicker.removeAllViews();
                }
            }
        });

        eventEndText.setText(day);
        eventEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(eventEndNumberPicker.getChildCount() == 0) {
                    removePickerView(view);
                    if(isAllDay.isChecked()) {
                        eventEndNumberPicker.initView(day);
                    } else {
                        eventEndNumberPicker.initView(day, new LocalTime().getHourOfDay() + 2);
                    }
                } else {
                    eventEndNumberPicker.removeAllViews();
                }
            }
        });

        repeatView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removePickerView(view);
                Intent intent = new Intent(getApplicationContext(), SetEventRepeatActivity.class);
                startActivityForResult(intent, REQUESTCODE_REPEAT);
                overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);


            }
        });

        alarmSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), TestAlarmActivity.class);
                startActivityForResult(intent, REQUESTCODE_ALARM);
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isExistAllData()) {
                    //db 등록
                }
            }
        });
    }

    private boolean isExistAllData() {
        if(eventTitle.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(), "제목을 입력해주세요.", Toast.LENGTH_SHORT).show();
            return false;
        }
        String[] eventStartTexts = eventStartText.getText().toString().split(" ");
        String[] eventEndTexts = eventEndText.getText().toString().split(" ");

        String[] startDates = eventStartTexts[0].split("\\.");
        LocalDate startDate = new LocalDate(Integer.parseInt(startDates[0]), Integer.parseInt(startDates[1]), Integer.parseInt(startDates[2]));

        String[] endDates = eventEndTexts[0].split("\\.");
        LocalDate endDate = new LocalDate(Integer.parseInt(endDates[0]), Integer.parseInt(endDates[1]), Integer.parseInt(endDates[2]));

        if(startDate.toDate().getTime() > endDate.toDate().getTime()) {
            Toast.makeText(getApplicationContext(), "종료시간이 시작시간보다 빠릅니다.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(!isAllDay.isChecked()) {
            String startType = eventStartTexts[1];
            String endType = eventEndTexts[1];

            int startHour = Integer.parseInt(eventStartTexts[2].split(":")[0]);
            int startMinute = Integer.parseInt(eventStartTexts[2].split(":")[1]);
            if(startType.equals("오전")) {
                if(startHour == 12) {
                    startHour = 0;
                }
            } else {
                startHour -= 12;
            }

            int endHour = Integer.parseInt(eventEndTexts[2].split(":")[0]);
            int endMinute = Integer.parseInt(eventEndTexts[2].split(":")[1]);
            if(endType.equals("오전")) {
                if(endHour == 12) {
                    endHour = 0;
                }
            } else {
                endHour -= 12;
            }

            DateTime startTime = startDate.toDateTime(new LocalTime(startHour, startMinute));
            DateTime endTime = endDate.toDateTime(new LocalTime(endHour, endMinute));

            if(startTime.getMillis() > endTime.getMillis()) {
                Toast.makeText(getApplicationContext(), "종료시간이 시작시간보다 빠릅니다.", Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        return true;
    }

    private void removePickerView(View view) {
        if(view.getId() == R.id.event_start_daypicker) {
            eventEndNumberPicker.removeAllViews();
        } else if(view.getId() == R.id.event_end_daypicker) {
            eventStartNumberPicker.removeAllViews();
        } else {
            eventStartNumberPicker.removeAllViews();
            eventEndNumberPicker.removeAllViews();
        }
    }

    private void removeViews() {
        eventStartNumberPicker.removeAllViews();
        eventEndNumberPicker.removeAllViews();
    }

    public void setEventStartText() {
        StringBuffer buf = new StringBuffer();
        LocalDate date = eventStartNumberPicker.getPickerDate();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd");
        buf.append(simpleDateFormat.format(date.toDate()));

        if(!isAllDay.isChecked()) {
            int type = eventStartNumberPicker.typePicker.getValue();
            int hour = eventStartNumberPicker.hourPicker.getValue();
            int minute = eventStartNumberPicker.minutePicker.getValue();

            buf.append(" " + eventStartNumberPicker.typePicker.getDisplayedValues()[type] + " " + hour + ":" + eventStartNumberPicker.minutePicker.getDisplayedValues()[minute]);
        }

        eventStartText.setText(buf + "");
    }

    public void setEventEndText() {
        StringBuffer buf = new StringBuffer();
        LocalDate date = eventEndNumberPicker.getPickerDate();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd");
        buf.append(simpleDateFormat.format(date.toDate()));

        if(!isAllDay.isChecked()) {
            int type = eventEndNumberPicker.typePicker.getValue();
            int hour = eventEndNumberPicker.hourPicker.getValue();
            int minute = eventEndNumberPicker.minutePicker.getValue();

            buf.append(" " + eventEndNumberPicker.typePicker.getDisplayedValues()[type] + " " + hour + ":" + eventEndNumberPicker.minutePicker.getDisplayedValues()[minute]);
        }

        eventEndText.setText(buf + "");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.anim_noanim, R.anim.anim_slide_out_bottom);
    }
}
