package com.sang.peoplan;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
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

import com.victorminerva.widget.edittext.AutofitEdittext;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;

import java.text.SimpleDateFormat;
import java.util.List;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CreateScheduleActivity extends AppCompatActivity { // 일정 추가 액티비티
    Switch isByDay; // 요일별 선택
    Switch isAllDay; // 하루종일 선택
    AutofitEdittext eventTitle; // 이벤트 제목
    Button selectGroupButton; // 그룹 선택
    LinearLayout timeGroup; // 시간 설정위한 레이아웃

    LinearLayout eventStart; // 시작 시간 설정 위한 레이아웃
    LinearLayout eventEnd; // 종료 시간 설정 위한 레이아웃
    LinearLayout repeatView; // 반복 횟수 설정 위한 레이아웃
    LinearLayout alarmSelect; // 알람 설정 위한 레이아웃
    EditText eventContent; // 일정 내용
    Button confirm; // 저장 버튼
    TextView repeatConfirm; // 반복 정도

    TextView eventStartText; // 시작 일정 선택을 위함
    TextView eventEndText; // 종료 일정 선택을 위함

    DayPicker eventStartNumberPicker; // 일정 시작 날짜 선택
    DayPicker eventEndNumberPicker; // 일정 종료 날짜 선택

    WeekdaysGroup weekdaysGroup; // 요일별 선택시 어떤 요일들을 선택할지 위한 레이아웃

    final int REQUESTCODE_GROUP = 100;
    final int REQUESTCODE_REPEAT = 200;
    final int REQUESTCODE_ALARM = 300;

    private String day = "";

    public static final int DATE_YEAR_TYPE = 0;
    public static final int DATE_MONTH_TYPE = 1;
    public static final int DATE_DAY_TYPE = 2;
    public static final int DATE_AMPM_TYPE = 3;
    public static final int DATE_HOUR_TYPE = 4;
    public static final int DATE_MINUTE_TYPE = 5;
    public static final int DATE_TYPE = 6;
    public static final int DATE_TIME_TYPE = 7;
    public static final int DATE_FULL_HOUR_TYPE = 8;

    public static AlarmManager _am;
    Intent intentAlarm;

    private Button on_Unregist;
    private static int hourOfDay = 15;
    private static int minute = 24;

    private ToggleButton _toggleSun, _toggleMon, _toggleTue, _toggleWed, _toggleThu, _toggleFri, _toggleSat;

    public static final int REPEAT_NONE = 0;
    public static final int REPEAT_EVERYDAY = 1;
    public static final int REPEAT_EVERYWEEK = 2;
    public static final int REPEAT_EVERYTWOWEEK = 3;
    public static final int REPEAT_EVERYMONTH = 4;
    public static final int REPEAT_EVERYYEAR = 5;



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

                isAllDay.setChecked(true);
            }
        });

        // 하루 종일 이란 의미를 다시 확인 필요
        isAllDay.setChecked(true);
        isAllDay.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                removeViews();

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
            }
        });

        // 그룹 선택 시 반응, 코딩~~
        selectGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        // 시작 요일 설정
        eventStartText.setText(day);
        eventStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(eventStartNumberPicker.getChildCount() == 0) {
                    removePickerView(view);
                    if(isAllDay.isChecked()) {
                        eventStartNumberPicker.initView(getDateToString(eventStartText, DATE_TYPE));
                    } else {
                        eventStartNumberPicker.initView(getDateToString(eventStartText, DATE_TYPE), Integer.parseInt(getDateToString(eventStartText, DATE_FULL_HOUR_TYPE)));
                    }
                } else {
                    eventStartNumberPicker.removeAllViews();
                }
            }
        });

        // 종료 요일 설정
        eventEndText.setText(day);
        eventEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(eventEndNumberPicker.getChildCount() == 0) {
                    removePickerView(view);
                    if(isAllDay.isChecked()) {
                        eventEndNumberPicker.initView(getDateToString(eventEndText, DATE_TYPE));
                    } else {
                        eventEndNumberPicker.initView(getDateToString(eventEndText, DATE_TYPE), Integer.parseInt(getDateToString(eventEndText, DATE_FULL_HOUR_TYPE)));
                    }
                } else {
                    eventEndNumberPicker.removeAllViews();
                }
            }
        });

        // 주기 설정
        repeatView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removePickerView(view);
                Intent intent = new Intent(getApplicationContext(), SetEventRepeatActivity.class);
                startActivityForResult(intent, REQUESTCODE_REPEAT);
                overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);


            }
        });

        // 알람 설정
        alarmSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), TestAlarmActivity.class);
                startActivityForResult(intent, REQUESTCODE_ALARM);
            }
        });

        // 추가 버튼 설정
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LocalDate startDate = new LocalDate(Integer.parseInt(getDateToString(eventStartText, DATE_YEAR_TYPE)),
                                                    Integer.parseInt(getDateToString(eventStartText, DATE_MONTH_TYPE)),
                                                    Integer.parseInt(getDateToString(eventStartText, DATE_DAY_TYPE)));

                LocalDate endDate = new LocalDate(Integer.parseInt(getDateToString(eventEndText, DATE_YEAR_TYPE)),
                        Integer.parseInt(getDateToString(eventEndText, DATE_MONTH_TYPE)),
                        Integer.parseInt(getDateToString(eventEndText, DATE_DAY_TYPE)));

                DateTime startTime;
                DateTime endTime;

                if(!isAllDay.isChecked()) {
                    int startHour = Integer.parseInt(getDateToString(eventStartText, DATE_FULL_HOUR_TYPE));
                    int startMinute = Integer.parseInt(getDateToString(eventStartText, DATE_MINUTE_TYPE));

                    int endHour = Integer.parseInt(getDateToString(eventEndText, DATE_FULL_HOUR_TYPE));
                    int endMinute = Integer.parseInt(getDateToString(eventEndText, DATE_MINUTE_TYPE));

                    startTime = startDate.toDateTime(new LocalTime(startHour, startMinute));
                    endTime = endDate.toDateTime(new LocalTime(endHour, endMinute));
                } else {
                    startTime = startDate.toDateTime(new LocalTime(0, 0, 0, 0));
                    endTime = endDate.toDateTime(new LocalTime(23, 59, 59, 999));
                }

                if(isExistAllData(startTime, endTime)) {

                    SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
                    Toast.makeText(getApplicationContext(), dateFormat.format(startTime.toDate()) + ", " + dateFormat.format(endTime.toDate()), Toast.LENGTH_SHORT).show();
                    intentAlarm.putExtra("eventTitle", eventTitle.getText().toString());

                    int repeat = REPEAT_NONE;
                    if(repeatConfirm.getText().toString().equals("없음")) {

                    } else if(repeatConfirm.getText().toString().equals("매일")) {
                        repeat = REPEAT_EVERYDAY;
                    } else if(repeatConfirm.getText().toString().equals("매주")) {
                        repeat = REPEAT_EVERYWEEK;
                    } else if(repeatConfirm.getText().toString().equals("매월")) {
                        repeat = REPEAT_EVERYMONTH;
                    } else if(repeatConfirm.getText().toString().equals("매년")) {
                        repeat = REPEAT_EVERYYEAR;
                    }
                   //db 등록
                    Event event = new Event(eventTitle.getText().toString(), new Date(startTime.getMillis()), new Date(endTime.getMillis()), repeat, false);

                    CreateEventAsyncTask task = new CreateEventAsyncTask();
                    task.execute(event);
                }
            }
        });


    }


    // Date 형식 데이터 String 으로 변환
    public class CreateEventAsyncTask extends AsyncTask<Event, Void, Boolean> {
        Retrofit retrofit;
        APIService service;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            retrofit = new Retrofit.Builder()
                    .baseUrl(GlobalApplication.SERVER_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            service = retrofit.create(APIService.class);
        }

        @Override
        protected void onPostExecute(Boolean isSuccessed) {
            super.onPostExecute(isSuccessed);
            if(isSuccessed) {
                finish();
            }
        }

        @Override
        protected Boolean doInBackground(Event... events) {
            Call<Event> event = service.createEvent(String.valueOf(SplashActivity.USER_PROFILE.getId()), events[0]);
            try {
                if(event.execute().code() == 200) {
                    Call<List<Event>> callCalendar = service.getUserEvents(String.valueOf(SplashActivity.USER_PROFILE.getId()));
                    Response<List<Event>> calendars = callCalendar.execute();
                    if(calendars.code() == 200) {
                        for(int i = 0; i < calendars.body().size(); i++) {
                            Event e = calendars.body().get(i);
                            if(SplashActivity.EVENT_LIST.containsKey(e._id)) {
                                continue;
                            } else {
                                SplashActivity.EVENT_LIST.put(e._id, e);
                            }
                        }
                    }
                    return true;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return false;
        }
    }


    private String getDateToString(TextView text, int type) {
        String[] texts = text.getText().toString().split(" ");
        String[] dates = texts[0].split("\\.");

        switch (type) {
            case DATE_YEAR_TYPE:
                return dates[0];
            case DATE_MONTH_TYPE:
                return dates[1];
            case DATE_DAY_TYPE:
                return dates[2];
            case DATE_AMPM_TYPE:
                return texts[1];
            case DATE_HOUR_TYPE:
                return texts[2].split(":")[0];
            case DATE_FULL_HOUR_TYPE:
                int hour = Integer.parseInt(texts[2].split(":")[0]);

                if(texts[1].equals("오전")) {
                    if(hour == 12) {
                        return "0";
                    } else {
                        return String.valueOf(hour);
                    }
                } else {
                    if(hour != 12) {
                        return String.valueOf(hour + 12);
                    }
                }
            case DATE_MINUTE_TYPE:
                return texts[2].split(":")[1];
            case DATE_TYPE:
                return texts[0];
            case DATE_TIME_TYPE:
                return texts[2];
            default:
                return "";
        }
    }

    private boolean isExistAllData(DateTime startTime, DateTime endTime) {
        if(eventTitle.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(), "제목을 입력해주세요.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(startTime.getMillis() > endTime.getMillis()) {
            Toast.makeText(getApplicationContext(), "종료시간이 시작시간보다 빠릅니다.", Toast.LENGTH_SHORT).show();
            return false;
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

    public void onRegist()
    //알람 등록
    // 설정 시간 정보를 담은 객체를 hashcode를 생성하여 broadcast해 pendingintent를 사용한다.
        // intent로 넘어가는 extras만이 업데이트됨
        {
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
