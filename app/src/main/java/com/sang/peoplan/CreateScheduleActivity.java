package com.sang.peoplan;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class CreateScheduleActivity extends AppCompatActivity {
    Switch isGroup;
    Switch isAlarm;
    EditText eventTitle;
    Button selectGroupButton;
    LinearLayout eventStart;
    LinearLayout eventEnd;
    LinearLayout repeatView;
    LinearLayout alarmSelect;
    EditText eventContent;
    Button confirm;
    TextView repeatConfirm;

    TextView eventStartText;
    TextView eventEndText;

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

        isGroup = findViewById(R.id.is_group);
        isAlarm = findViewById(R.id.isAlarm);
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
                    alarmSelect.setVisibility(View.VISIBLE);
                }
                else{
                    alarmSelect.setVisibility(View.GONE);
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

            }
        });

        eventEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
