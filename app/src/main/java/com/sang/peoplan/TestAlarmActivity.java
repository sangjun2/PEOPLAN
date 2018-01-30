package com.sang.peoplan;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import java.io.File;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

public class TestAlarmActivity extends AppCompatActivity {
    private static final String BASE_PATH = Environment.getRootDirectory().getPath();

    private AlarmManager _am;
    Intent intent;

    private Button on_regist;
    private Button on_Unregist;
    private Button time_picker_bt;
    private static int hourOfDay = 15;
    private static int minute = 24;

    private ToggleButton _toggleSun, _toggleMon, _toggleTue, _toggleWed, _toggleThu, _toggleFri, _toggleSat;

    public int getMediaResIdByName(String resName) {
        String pkgName = this.getPackageName();
        // Return 0 if not found.
        int resID = this.getResources().getIdentifier(resName, "raw", pkgName);
        Log.i("AndroidVideoView", "Res Name: " + resName + "==> Res ID = " + resID);
        return resID;
    }
    private TimePickerDialog.OnTimeSetListener listener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // 설정버튼 눌렀을 때
            TestAlarmActivity.hourOfDay = hourOfDay;
            TestAlarmActivity.minute = minute;
            Toast.makeText(getApplicationContext(), hourOfDay + "시 " + minute + "분", Toast.LENGTH_SHORT).show();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_alarm);

//        Log.d("filepath==", );
        intent = new Intent(this, AlarmReceiver.class);


        _am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        _toggleSun = (ToggleButton) findViewById(R.id.toggle_sun);
        _toggleMon = (ToggleButton) findViewById(R.id.toggle_mon);
        _toggleTue = (ToggleButton) findViewById(R.id.toggle_tue);
        _toggleWed = (ToggleButton) findViewById(R.id.toggle_wed);
        _toggleThu = (ToggleButton) findViewById(R.id.toggle_thu);
        _toggleFri = (ToggleButton) findViewById(R.id.toggle_fri);
        _toggleSat = (ToggleButton) findViewById(R.id.toggle_sat);

        on_regist = findViewById(R.id.on_regist_bt);
        on_Unregist = findViewById(R.id.on_unregist_bt);

        time_picker_bt = findViewById(R.id.time_picker_bt);

        on_regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onRegist();
            }
        });

        on_Unregist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onUnregist();
            }
        });

        time_picker_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog dialog = new TimePickerDialog(TestAlarmActivity.this, listener, hourOfDay, minute, false);
                dialog.show();
            }
        });

    }

    public void onRegist()
    //알람 등록
    // 설정 시간 정보를 담은 객체를 hashcode를 생성하여 broadcast해 pendingintent를 사용한다.
    // intent로 넘어가는 extras만이 업데이트됨
    {


//        File file = new File();
        MediaPlayer mp = MediaPlayer.create(this, R.raw.impact_intermezzo);

        boolean[] week = { false, _toggleSun.isChecked(), _toggleMon.isChecked(), _toggleTue.isChecked(), _toggleWed.isChecked(),
                _toggleThu.isChecked(), _toggleFri.isChecked(), _toggleSat.isChecked() }; // sunday=1 이라서 0의 자리에는 아무 값이나 넣었음


//        intent.putExtra("file", (Serializable) mp);
        intent.putExtra("weekday", week);
        intent.putExtra("music", R.raw.impact_intermezzo);
        intent.putExtra("hourOfDay", hourOfDay);
        intent.putExtra("minute", minute);
        PendingIntent pIntent = PendingIntent.getBroadcast(this, mp.hashCode(), intent, PendingIntent.FLAG_UPDATE_CURRENT);//알람을 해재할때의 식별자 hashcode

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.SECOND, cal.get(Calendar.SECOND));
        DateTime dateTime = new DateTime(2018, 1, 30, hourOfDay, minute);

        long oneday = 24 * 60 * 60 * 1000;// 24시간

        // 10초 뒤에 시작해서 매일 같은 시간에 반복하기
        _am.setRepeating(AlarmManager.RTC_WAKEUP, dateTime.getMillis(), oneday, pIntent);
    }

    public void onUnregist()
    {
        MediaPlayer mp = MediaPlayer.create(this, R.raw.impact_intermezzo);
        PendingIntent pIntent = PendingIntent.getBroadcast(this, mp.hashCode(), intent, PendingIntent.FLAG_UPDATE_CURRENT);

        _am.cancel(pIntent);
    }


}
