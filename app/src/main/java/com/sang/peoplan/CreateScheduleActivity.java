package com.sang.peoplan;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
import android.widget.ToggleButton;

import org.joda.time.DateTime;
import org.joda.time.LocalTime;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;

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

    public static AlarmManager _am;
    Intent intentAlarm;

    private Button on_Unregist;
    private static int hourOfDay = 15;
    private static int minute = 24;

    private ToggleButton _toggleSun, _toggleMon, _toggleTue, _toggleWed, _toggleThu, _toggleFri, _toggleSat;


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

        intentAlarm = new Intent(this, AlarmReceiver.class);


        _am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        _toggleSun = (ToggleButton) findViewById(R.id.toggle_sun);
        _toggleMon = (ToggleButton) findViewById(R.id.toggle_mon);
        _toggleTue = (ToggleButton) findViewById(R.id.toggle_tue);
        _toggleWed = (ToggleButton) findViewById(R.id.toggle_wed);
        _toggleThu = (ToggleButton) findViewById(R.id.toggle_thu);
        _toggleFri = (ToggleButton) findViewById(R.id.toggle_fri);
        _toggleSat = (ToggleButton) findViewById(R.id.toggle_sat);



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
                    return;
                }
                intentAlarm.putExtra("eventTitle", eventTitle.getText().toString());

            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.anim_noanim, R.anim.anim_slide_out_bottom);
    }

    public void onRegist()
    //알람 등록
    // 설정 시간 정보를 담은 객체를 hashcode를 생성하여 broadcast해 pendingintent를 사용한다.
    // intent로 넘어가는 extras만이 업데이트됨
    {


//        File file = new File();
        MediaPlayer mp = MediaPlayer.create(this, R.raw.impact_intermezzo);



        if(true){//날짜별 알람인 경우
            DateTime dateTime = new DateTime(2018, 1, 30, hourOfDay, minute);//날짜 정하기
            //알람에 대한 hashcode정보 또한 db에 저장되어야함.
            PendingIntent pIntent = PendingIntent.getBroadcast(this, dateTime.hashCode(), intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT);//알람을 해재할때의 식별자 hashcode
            long oneday = 24 * 60 * 60 * 1000;// 24시간
            intentAlarm.putExtra("eventTitle", eventTitle.getText().toString());

            // 10초 뒤에 시작해서 매일 같은 시간에 반복하기
            _am.setRepeating(AlarmManager.RTC_WAKEUP, dateTime.getMillis(), oneday, pIntent);
        }
        else{//요일별 알람인 경우
            DateTime dateTime = new DateTime();
            PendingIntent pIntent = PendingIntent.getBroadcast(this, dateTime.hashCode(), intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT);//알람을 해재할때의 식별자 hashcode
            long oneday = 24 * 60 * 60 * 1000;// 24시간

            // 10초 뒤에 시작해서 매일 같은 시간에 반복하기
            _am.setRepeating(AlarmManager.RTC_WAKEUP, dateTime.getMillis(), oneday, pIntent);
        }

    }

    public void onUnregist() {//dateTime에 대한 hash코드를 db에서 불러와 alarmmanager 삭제
        MediaPlayer mp = MediaPlayer.create(this, R.raw.impact_intermezzo);
        PendingIntent pIntent = PendingIntent.getBroadcast(this, mp.hashCode(), intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT);

        _am.cancel(pIntent);
    }

    public void onRegistWeekday(DateTime[] dateTimes){//dateTimes는 등록하려는 요일들의 시작시간을 담고 있어야함.
        long week = 7 * 24 * 60 * 60 * 1000;// 일주일
        for(int i = 0; i < dateTimes.length; i++){
            if(false){//이미 존재하는 이벤트이름일 경우 뒤에 숫자 붙혀서 저장

            }
            DateTime dateTime = dateTimes[i];
            String requestString = eventTitle.getText().toString() + dateTime.getDayOfWeek();
            int requestCode = requestString.hashCode();
            //db에 requesCode저장 필요(배열)

            PendingIntent pIntent = PendingIntent.getBroadcast(this, requestCode, intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT);//알람을 해재할때의 식별자 hashcode
            _am.setRepeating(AlarmManager.RTC_WAKEUP, dateTime.getMillis(), week, pIntent);
        }
    }
}
