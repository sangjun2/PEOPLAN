package com.sang.peoplan;

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

    public static FragmentTransaction transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationViewEx bnve = findViewById(R.id.navigation);

        bnve.enableAnimation(false);
        bnve.enableItemShiftingMode(false);
        bnve.enableShiftingMode(false);
        bnve.setTextVisibility(false);
        bnve.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                transaction = getSupportFragmentManager().beginTransaction();
                switch (item.getItemId()){
                    case R.id.navigation_calendar:
                        transaction.replace(R.id.frame, PlannerFragment.newInstance()).commit();
                        return true;
                    case R.id.navigation_group:
                        transaction.replace(R.id.frame, GroupFragment.newInstance()).commit();
                        return true;
                    case R.id.navigation_post:
                        return true;
                    case R.id.navigation_business_card:
                        return true;
                    default:
                        break;
                }
                return false;
            }
        });

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.frame, PlannerFragment.newInstance());
        fragmentTransaction.commit();

    }

}
