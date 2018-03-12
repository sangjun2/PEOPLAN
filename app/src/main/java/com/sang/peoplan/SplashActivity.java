package com.sang.peoplan;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

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
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SplashActivity extends AppCompatActivity {
    public static UserProfile USER_PROFILE;
    public static HashMap<String, Event> EVENT_LIST;
    private Context mContext;
    private SessionCallback callback;

    private LoginButton loginButton;

    public static String USER_TEL = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        loginButton = findViewById(R.id.login_bt);

        EVENT_LIST = new HashMap<>();
        mContext = this;

        callback = new SessionCallback();
        Session.getCurrentSession().addCallback(callback);
        Session.getCurrentSession().checkAndImplicitOpen();

        if(USER_PROFILE == null) {
            loginButton.setVisibility(View.VISIBLE);
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
                USER_PROFILE = userProfile;
                loginButton.setVisibility(View.GONE);

                GetUserAsyncTask asyncTask = new GetUserAsyncTask();
                asyncTask.execute(String.valueOf(USER_PROFILE.getId()));
            }

            @Override
            public void onNotSignedUp() {
                Log.d("KAKAOTAG==", "onNotSignedUp");
            }
        });
    }

    public class GetUserAsyncTask extends AsyncTask<String, Void, Boolean> {
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
        protected void onPostExecute(Boolean isExist) {
            super.onPostExecute(isExist);

            if(isExist) {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                Intent intent = new Intent(SplashActivity.this, KakaoSignupActivity.class);
                startActivity(intent);
                finish();
            }
        }

        @Override
        protected Boolean doInBackground(String... uid) {
            Call<List<User>> users = service.getUser(uid[0]);

            try {
                Response<List<User>> response = users.execute();
                if(response.code() == 404) {
                    return false;
                } else if(response.code() == 500) {
                    return false;
                } else if(response.code() == 200) {
                    if(response.body().size() == 1) {
                        USER_TEL = response.body().get(0).getTel();
                        Call<List<Event>> callCalendar = service.getUserEvents(uid[0]);
                        Response<List<Event>> calendars = callCalendar.execute();
                        if(calendars.code() == 200) {
                            for(int i = 0; i < calendars.body().size(); i++) {
                                Event e = calendars.body().get(i);
                                e.getStart().setTime(e.getStart().getTime() - 1000 * 60 * 60 * 9);
                                e.getEnd().setTime(e.getEnd().getTime()  - 1000 * 60 * 60 * 9);
                                EVENT_LIST.put(e._id, e);
                            }
                        }
                        return true;
                    }
                    return false;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return false;
        }
    }
}
