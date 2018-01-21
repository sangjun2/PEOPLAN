package com.sang.peoplan;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class CreateScheduleActivity extends AppCompatActivity {
    Switch isGroup;
    Switch isAlarm;
    EditText eventTitle;
    Button selectGroupButton;
    Button eventStartButton;
    Button eventEndButton;
    Button repeatButton;
    Button alarmSelectButton;
    EditText eventContent;
    Button confirm;

    final int REQUESTCODE_GROUP = 100;
    final int REQUESTCODE_REPEAT = 200;
    final int REQUESTCODE_ALARM = 300;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_schedule);

        Toolbar toolbar = findViewById(R.id.schedule_toolbar);
        TextView toolbarTitle = findViewById(R.id.confirm_toolbar_title);
        toolbarTitle.setText("일정 추가");
        setSupportActionBar(toolbar);

        isGroup = findViewById(R.id.isGroup);
        isAlarm = findViewById(R.id.isAlarm);
        eventTitle = findViewById(R.id.eventTitle);
        selectGroupButton = findViewById(R.id.selectGroupButton);
        eventStartButton = findViewById(R.id.eventStartButton);
        eventEndButton = findViewById(R.id.eventEndButton);
        repeatButton = findViewById(R.id.repeatButton);
        alarmSelectButton = findViewById(R.id.alarmSelectButton);
        eventContent = findViewById(R.id.eventContent);
        confirm = findViewById(R.id.confirm_toolbar_bt);


        isGroup.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    selectGroupButton.setVisibility(View.VISIBLE);
                }
                else{
                    selectGroupButton.setVisibility(View.GONE);
                }
            }
        });

        isAlarm.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    alarmSelectButton.setVisibility(View.VISIBLE);
                }
                else{
                    alarmSelectButton.setVisibility(View.GONE);
                }
            }
        });

        selectGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        eventStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        eventEndButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        repeatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        alarmSelectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
