package com.sang.peoplan;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

// 그룹 삭제나 탈퇴시 팝업 형식으로 뜨는 액티비티, DB 처리까지 해줘야하므로 액티비티로
public class GroupDeleteOrLeave extends AppCompatActivity {

    int WhatToDo; // 100 : 삭제, 200 : 탈퇴
    Group group; // 처리할 그룹 정보
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_group_delete_or_leave);

        TextView detailTxt = findViewById(R.id.detailTxt);

        // 그룹 탈퇴인지 삭제인지 데이터 가져오기, 해당 그룹 정보도
        Intent intent = getIntent();
        WhatToDo = intent.getIntExtra("WhatToDo", 1);
        group = (Group) intent.getSerializableExtra("Group");
        switch (WhatToDo){
            case GroupDetailActivity.REQUEST_DELETE_GROUP:
                detailTxt.setText("정말 그룹을 삭제하시겠습니까?");
                break;
            case GroupDetailActivity.REQUEST_LEAVE_GROUP:
                detailTxt.setText("정말 그룹을 탈퇴하시겠습니까?");
                break;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        // 바깥 레이어 클릭시 안닫히게
        if(event.getAction() == MotionEvent.ACTION_OUTSIDE)
        {
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        //안드로이드 백버튼 막기
        return;
    }

    public void delete_or_leave(View v)
    {
        DeleteOrLeaveGroupAsyncTask task = new DeleteOrLeaveGroupAsyncTask();
        task.execute(group);
    }

    public void maintain(View v)
    {
        // 그룹 유지
        setResult(RESULT_CANCELED);
        finish();
    }

    // 스레드 이용한 DB에 그룹 데이터 저장
    private class DeleteOrLeaveGroupAsyncTask extends AsyncTask<Group, Void, Boolean> {
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
                Intent returnIntent = new Intent();
                setResult(RESULT_OK,returnIntent);
            }else{
                setResult(RESULT_CANCELED);

            }
            finish();
        }


        @Override
        protected Boolean doInBackground(Group... groups) {
            // 그룹 탈퇴 나 삭제
            // switch 문으로 요청에 따른 처리\
            Call<Group> group;
            switch(WhatToDo){
                case GroupDetailActivity.REQUEST_DELETE_GROUP:
                    group = service.createGroup(groups[0]);
                    break;
                case GroupDetailActivity.REQUEST_LEAVE_GROUP:
                    group = service.createGroup(groups[0]);
                    break;
                default: // 잘못된 입력인 경우
                    group = null;
            }
            try {
                if(group.execute().code() == 200) { // 처리 성공
                    return true;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return false;
        }
    }
}
