package com.sang.peoplan;

import android.content.Intent;
import android.content.SharedPreferences;
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

public class MainActivity extends AppCompatActivity {
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        BottomNavigationViewEx bnve = findViewById(R.id.navigation);

        bnve.enableAnimation(false);
        bnve.enableShiftingMode(false);
        bnve.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_planner:
                        fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.frame, PlannerFragment.newInstance());
                        fragmentTransaction.commit();
                        return true;
                    case R.id.navigation_group:
                        fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.frame, GroupFragment.newInstance());
                        fragmentTransaction.commit();
                        return true;
                }
                return false;
            }
        });

        /*
        SharedPreferences preferences = getSharedPreferences("Account", MODE_PRIVATE);
        boolean existData = preferences.getBoolean("Login", false);
        if(!existData) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            //finish();
        }
        */

        fragmentTransaction.add(R.id.frame, PlannerFragment.newInstance());
        fragmentTransaction.commit();

    }

}