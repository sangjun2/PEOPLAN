package com.sang.peoplan;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.security.acl.Group;

public class MainActivity extends AppCompatActivity { // 프래그먼트 변화만 관여
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    private static MediaPlayer player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // fragment 매니저 선언
        fragmentManager = getSupportFragmentManager();
        // fragment 트랜잭션 시작
        fragmentTransaction = fragmentManager.beginTransaction();

        // fragment 교체를 위한 네비게이션 설정
        BottomNavigationViewEx bnve = findViewById(R.id.navigation);

        bnve.enableAnimation(false);
        bnve.enableShiftingMode(false);
        bnve.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) { // 네비게이션 리스너 설정
                switch (item.getItemId()) {
                    case R.id.navigation_planner: // 플래너
                        fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.frame, PlannerFragment.newInstance());
                        fragmentTransaction.commit();
                        return true;
                    case R.id.navigation_group: // 그룹
                        fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.frame, GroupFragment.newInstance());
                        fragmentTransaction.commit();
                        return true;
                    case R.id.navigation_businesscard: // 명함
                        fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.frame, BusinessCardFragment.newInstance());
                        fragmentTransaction.commit();
                        return true;
                }
                return false;
            }
        });
        fragmentTransaction.add(R.id.frame, PlannerFragment.newInstance());
        fragmentTransaction.commit();
    }

    // 일정 알람
    public static void startAlarm(Context context, int ResId){
        player = MediaPlayer.create(context, ResId);
        player.setOnPreparedListener(new MediaPlayer.OnPreparedListener()
        {
            @Override
            public void onPrepared(MediaPlayer mp)
            {
                mp.start();
            }
        });
        try
        {
            player.prepare();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void stopAlarm() {
        if(player == null){
            return;
        }
        player.stop();
        player.release();
        player = null;
    }
}