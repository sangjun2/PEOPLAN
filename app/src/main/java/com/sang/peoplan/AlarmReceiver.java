package com.sang.peoplan;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioFocusRequest;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;

import java.io.File;
import java.util.Calendar;

/**
 * Created by sanginLee on 2018-01-22.
 */

public class AlarmReceiver extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent)
    {

        boolean[] week = intent.getBooleanArrayExtra("weekday");
        int music_res = intent.getIntExtra("musicRes", R.raw.impact_intermezzo);
        int hourOfDay = intent.getIntExtra("hourOfDay", 0);
        int minute = intent.getIntExtra("minute", 0);
        Calendar cal = Calendar.getInstance();
        // 오늘 요일의 알람 재생이 true이면 사운드 재생

        if (!week[cal.get(Calendar.DAY_OF_WEEK)])
            return;
        Intent alarm_intent = new Intent(context, AlarmActivity.class);
        context.startActivity(alarm_intent);
        MainActivity.startAlarm(context, music_res);

    }

}
