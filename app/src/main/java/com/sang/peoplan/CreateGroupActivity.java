package com.sang.peoplan;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.joda.time.DateTime;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class CreateGroupActivity extends AppCompatActivity { // 그룹 추가
    LinearLayout categoryView; // 카테고리 종류 띄우는 뷰
    TextView categoryConfirm; // 선택된 카테고리 보이는 텍스트

    final int REQUESTCODE_CATEGORY = 500;

    /*
     * 카테고리 설정은 끝
     * 그룹 이름 정하고, 확인 버튼 누르기
     * 확인 버튼 눌렀을 시, DB에 추가
     * 2018.3.7 미구현 부분
     * - 그룹 클래스 모델링
     * - 카테고리 추가
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        Button confirm_toolbar_bt = findViewById(R.id.confirm_toolbar_bt); // 그룹 생성 버튼
        final EditText group_name = findViewById(R.id.group_name); // 그룹 이름 EditText

        Toolbar toolbar = findViewById(R.id.group_toolbar);
        TextView toolbarTitle = findViewById(R.id.confirm_toolbar_title);

        toolbarTitle.setText("그룹 만들기");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // 생성 버튼 리스너 설정
        confirm_toolbar_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                // test 값
                Group newGroup = new Group("test",group_name.getText().toString());
                // DB에만 저장
                CreateGroupAsyncTask task = new CreateGroupAsyncTask();
                task.execute(newGroup);
            }
        });
        // 그룹 카테고리 설정
        categoryView = findViewById(R.id.category_view);
        categoryConfirm = findViewById(R.id.category_confirm);
        categoryView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CreateGroupActivity.this, SetGroupCategoryActivity.class);
                startActivityForResult(intent, REQUESTCODE_CATEGORY);
                overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUESTCODE_CATEGORY){
            if(resultCode == Activity.RESULT_OK){
                String repeat = data.getStringExtra("category");
                categoryConfirm.setText(repeat);
            }
        }
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

    // 스레드 이용한 DB에 그룹 데이터 저장
    public class CreateGroupAsyncTask extends AsyncTask<Group, Void, Boolean> {
        Retrofit retrofit;
        APIService service;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            retrofit = new Retrofit.Builder()
                    .baseUrl(GlobalApplication.SERVER_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            service = retrofit.create(APIService.class);
        }

        @Override
        protected void onPostExecute(Boolean isSuccessed) {
            super.onPostExecute(isSuccessed);
            if(isSuccessed) {
                finish();
            }
        }

        @Override
        protected Boolean doInBackground(Group... groups) {
            // 새로운 그룹 추가
            // DB 그룹 추가는 possible, but 그룹 클래스 아직 완성 안되있음
            Call<Group> group = service.createGroup(groups[0]);
            try {
                if(group.execute().code() == 200) { // 추가 성공
                    return true;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return false;
        }
    }
}
