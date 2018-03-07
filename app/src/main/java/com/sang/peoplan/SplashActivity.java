package com.sang.peoplan;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;

public class SplashActivity extends AppCompatActivity {
    public static UserProfile USER_PROFILE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try { // 예외 처리
            Thread.sleep(2500); // 이건 왜 ?..
            Intent intent = new Intent(SplashActivity.this, LoginActivity.class); // 인텐트 넘기며 로그인 화면으로
            startActivity(intent); // 로그인 화면 start
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
