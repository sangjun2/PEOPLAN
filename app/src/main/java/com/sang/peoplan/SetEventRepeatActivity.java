package com.sang.peoplan;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class SetEventRepeatActivity extends AppCompatActivity {
    RadioGroup radioGroup;
    Button confirm;
    RadioButton radioButton;

    final int RESULTCODE_REPEAT = 200;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_event_repeat);

        Toolbar toolbar = findViewById(R.id.event_repeat_toolbar);
        TextView toolbarTitle = findViewById(R.id.confirm_toolbar_title);
        toolbarTitle.setText("반복 설정");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        radioGroup = findViewById(R.id.repeatGroup);
        confirm = findViewById(R.id.confirm_toolbar_bt);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radioButton = findViewById(radioGroup.getCheckedRadioButtonId());
                Toast.makeText(SetEventRepeatActivity.this, radioButton.getText(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.putExtra("repeat", radioButton.getText());
                setResult(Activity.RESULT_OK, intent);
                finish();

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return false;
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.anim_noanim, R.anim.anim_slide_out_bottom);
    }
}
