package com.sang.peoplan;

import android.*;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.kakao.auth.ErrorCode;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.LoginButton;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.exception.KakaoException;
import com.kakao.util.helper.log.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SplashActivity extends AppCompatActivity {
    public static User USER;

    public static UserProfile KAKAO_PROFILE;

    public static HashMap<String, Event> EVENT_LIST;
    public static ArrayList<BusinessCard> BUSINESSCARD_LIST;
    public static ArrayList<Group> GROUP_LIST;
    private Context mContext;
    private SessionCallback callback;

    private LoginButton loginButton;

    ProgressBar progressBar;

    private final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED ){
            // 주소록 권한이 없는 경우 권한 요청
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_CONTACTS)){

            }else{
                ActivityCompat.requestPermissions( this,
                        new String[] { android.Manifest.permission.READ_CONTACTS},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS );
            }
        }else{
            // 걍 지나가~
        }


        loginButton = findViewById(R.id.login_bt);

        progressBar = findViewById(R.id.splash_progressbar);

        EVENT_LIST = new HashMap<>();
        BUSINESSCARD_LIST = new ArrayList<>();
        GROUP_LIST = new ArrayList<>();
        mContext = this;

        callback = new SessionCallback();
        Session.getCurrentSession().addCallback(callback);
        Session.getCurrentSession().checkAndImplicitOpen();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch( requestCode ){
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                if ( grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED ) {

                } else {
                    finish();
                }
            }
            return;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Session.getCurrentSession().removeCallback(callback);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private class SessionCallback implements ISessionCallback {
        @Override
        public void onSessionOpened() {
            requestMe();
        }

        @Override
        public void onSessionOpenFailed(KakaoException exception) {


            if(exception != null) {
                Logger.e(exception);
            }
        }
    }

    private void requestMe() {
        UserManagement.requestMe(new MeResponseCallback() {
            @Override
            public void onFailure(ErrorResult errorResult) {
                String message = "failed to get user info. msg=" + errorResult;
                Logger.d(message);
                Log.d("KAKAOTAG==", "onFailure");

                ErrorCode result = ErrorCode.valueOf(errorResult.getErrorCode());
                if (result == ErrorCode.CLIENT_ERROR_CODE) {
                    finish();
                } else {

                }
            }

            @Override
            public void onSessionClosed(ErrorResult errorResult) {
                Log.d("KAKAOTAG==", "onSessionClosed");
            }

            @Override
            public void onSuccess(UserProfile userProfile) {
                Log.d("KAKAOTAG==", "onSuccess");
                loginButton.setVisibility(View.GONE);

                KAKAO_PROFILE = userProfile;

                GetUserAsyncTask asyncTask = new GetUserAsyncTask();
                asyncTask.execute(String.valueOf(userProfile.getId())); // kakaoid -> 유저 _id필요 (받아올 방법 : 세션말고없나)

            }

            @Override
            public void onNotSignedUp() {
                Log.d("KAKAOTAG==", "onNotSignedUp");
            }
        });
    }

    public class GetUserAsyncTask extends AsyncTask<String, Void, Integer> {
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

            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Integer responseCode) {
            super.onPostExecute(responseCode);

            progressBar.setVisibility(View.GONE);

            if(responseCode == 200) {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else if(responseCode == 404) {
                Intent intent = new Intent(SplashActivity.this, KakaoSignupActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(mContext, "서버 점검 중...", Toast.LENGTH_SHORT).show();
                finish();
            }
        }

        @Override
        protected Integer doInBackground(String... uid) {
            Call<List<User>> users = service.getUser(uid[0]);

            try {
                Response<List<User>> response = users.execute();
                if(response.code() == 404) {
                    return 404;
                } else if(response.code() == 500) {
                    return 500;
                } else if(response.code() == 200) {
                    if(response.body().size() == 1) {
                        USER = response.body().get(0);

                        Call<List<Event>> callCalendar = service.getUserEvents(USER.get_id());
                        Response<List<Event>> calendars = callCalendar.execute();
                        Call<List<BusinessCard>> callBusinessCards = service.getBusinessCards(USER.get_id());
                        Response<List<BusinessCard>> businessCards = callBusinessCards.execute();
                        Call<List<Group>> callGroupLists = service.getGroups(USER.get_id());
                        Response<List<Group>> groups = callGroupLists.execute();

                        if(calendars.code() == 200) {
                            for(int i = 0; i < calendars.body().size(); i++) {
                                Event e = calendars.body().get(i);
                                e.getStart().setTime(e.getStart().getTime() - 1000 * 60 * 60 * 9);
                                e.getEnd().setTime(e.getEnd().getTime()  - 1000 * 60 * 60 * 9);
                                EVENT_LIST.put(e._id, e);
                            }
                        }

                        if(businessCards.code() == 200){
                            for(int i = 0; i < businessCards.body().size(); i++){
                                BusinessCard b = businessCards.body().get(i);
                                BUSINESSCARD_LIST.add(b);
                            }
                        }

                        return 200; // 정상 응답
                    }
                    return 500;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return -1; // 서버 꺼져 있을때 -1
        }
    }
}
