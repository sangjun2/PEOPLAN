package com.sang.peoplan;

import android.app.Activity;
import android.content.Intent;
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
import org.joda.time.LocalTime;

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
            }
        });

        isAllDay.setChecked(true);
        isAllDay.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){ // 하루종일
                    eventStartText.setText(day);
                    eventEndText.setText(day);
                }
                else{ // not 하루종일
                    LocalTime localTime = new LocalTime();
                    int hour = localTime.getHourOfDay();
                    eventStartText.setText(day + " " + String.valueOf(hour) + ":00");
                    eventEndText.setText(day + " " + String.valueOf(hour + 1) + ":00");
                }
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
                eventStartNumberPicker.initView(day);
            }
        });

        eventEndText.setText(day);
        eventEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eventEndNumberPicker.initView(day);
            }
        });

        repeatView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                if(eventTitle.getText().equals("")){
                    Toast.makeText(getApplicationContext(), "제목을 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.anim_noanim, R.anim.anim_slide_out_bottom);
    }
}
