package com.sang.peoplan;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.security.acl.Group;

public class MainActivity extends AppCompatActivity implements PlannerFragment.OnFragmentInteractionListener, GroupFragment.OnFragmentInteractionListener, BusinessCardFragment.OnFragmentInteractionListener, SearchFragment.OnFragmentInteractionListener { // 프래그먼트 변화만 관여
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    private static MediaPlayer player;
    DrawerLayout drawerLayout;
    LinearLayout drawerView;
    NotificationView notificationView;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;

        // fragment 매니저 선언
        fragmentManager = getSupportFragmentManager();
        // fragment 트랜잭션 시작
        fragmentTransaction = fragmentManager.beginTransaction();

        // fragment 교체를 위한 네비게이션 설정
        BottomNavigationViewEx bnve = findViewById(R.id.navigation);

        final PlannerFragment plannerFragment = PlannerFragment.newInstance();

        bnve.enableAnimation(false);
        bnve.enableShiftingMode(false);
        bnve.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) { // 네비게이션 리스너 설정
                switch (item.getItemId()) {
                    case R.id.navigation_planner: // 플래너
                        fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.frame, plannerFragment);
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
        fragmentTransaction.add(R.id.frame, plannerFragment);
        fragmentTransaction.commit();

        drawerLayout = findViewById(R.id.container);
        drawerView = findViewById(R.id.notification_drawer);
        notificationView = findViewById(R.id.notification_view);

        drawerLayout.setDrawerListener(drawerListener);

    }

    public void replaceBusinessCardFragment(){
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame, BusinessCardFragment.newInstance());
        fragmentTransaction.commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    DrawerLayout.DrawerListener drawerListener = new DrawerLayout.DrawerListener() {
        @Override
        public void onDrawerSlide(View drawerView, float slideOffset) {

        }

        @Override
        public void onDrawerOpened(View drawerView) {

        }

        @Override
        public void onDrawerClosed(View drawerView) {

        }

        @Override
        public void onDrawerStateChanged(int newState) {

        }
    };

    @Override
    public void onGroupFragmentNotificationInteraction() {
        drawerLayout.openDrawer(drawerView);
        notificationView.initView();
    }

    @Override
    public void onGroupFragmentSearchInteraction() {
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame, SearchFragment.newInstance());
        fragmentTransaction.commit();
    }

    @Override
    public void onPlannerFragmentInteraction() {
        drawerLayout.openDrawer(drawerView);
        notificationView.initView();
    }

    @Override
    public void onBusinessCardFragmentInteraction() {
        drawerLayout.openDrawer(drawerView);
        notificationView.initView();
    }

    @Override
    public void onSearchFragmentInteraction() {

    }

    @Override
    public void onBackPressed() {
        Fragment fragment = fragmentManager.findFragmentById(R.id.frame);
        if(fragment instanceof SearchFragment) {
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame, GroupFragment.newInstance());
            fragmentTransaction.commit();
        } else {
            super.onBackPressed();
        }
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