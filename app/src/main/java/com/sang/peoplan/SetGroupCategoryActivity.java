package com.sang.peoplan;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class SetGroupCategoryActivity extends AppCompatActivity { // 그룹 카테고리 선택
    RadioGroup radioGroup;
    Button confirm;
    RadioButton radioButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_group_category);

        Toolbar toolbar = findViewById(R.id.set_group_category_toolbar);
        TextView toolbarTitle = findViewById(R.id.confirm_toolbar_title);
        toolbarTitle.setText("카테고리 설정");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        radioGroup = findViewById(R.id.category_group);
        confirm = findViewById(R.id.confirm_toolbar_bt);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radioButton = findViewById(radioGroup.getCheckedRadioButtonId());
                Toast.makeText(SetGroupCategoryActivity.this, radioButton.getText(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.putExtra("category", radioButton.getText());
                setResult(Activity.RESULT_OK, intent);
                finish();
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
            }
        });
    }
}
