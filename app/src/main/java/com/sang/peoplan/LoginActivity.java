package com.sang.peoplan;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.kakao.auth.ErrorCode;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.exception.KakaoException;
import com.kakao.util.helper.log.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {
    private SessionCallback callback;
    public static UserProfile USER_PROFILE;
    private Context mContext;
    private final String getURL = "/api/users/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mContext = this;

        callback = new SessionCallback();
        Session.getCurrentSession().addCallback(callback);
        Session.getCurrentSession().checkAndImplicitOpen();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Session.getCurrentSession().removeCallback(callback);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return;
        }

        super.onActivityResult(requestCode, resultCode, data);
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
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                Intent intent = new Intent(LoginActivity.this, KakaoSignupActivity.class);
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
