package com.sang.peoplan;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.kakao.auth.ErrorCode;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.exception.KakaoException;
import com.kakao.util.helper.log.Logger;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {
    private SessionCallback callback; // ???
    public static UserProfile USER_PROFILE; // kakao reference
    private Context mContext; // ??
    private final String getURL = "/api/users/"; // ??


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login); // 로그인 UI 설정
        mContext = this;

        callback = new SessionCallback();
        Session.getCurrentSession().addCallback(callback); // 세션 상태 변화시 반응할 콜백 설정
        Session.getCurrentSession().checkAndImplicitOpen(); // ?? 이거 왜 ?, 하는 건 로그인 시도
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
        } // 세션이 오픈 됬을 시

        @Override
        public void onSessionOpenFailed(KakaoException exception) { // 로그인 실패 시
            if(exception != null) {
                Logger.e(exception);
            }
        }
    }

    private void requestMe() {
        UserManagement.requestMe(new MeResponseCallback() { // 사용자 정보 요청, 해당 객체의 내용으로 콜백
            @Override
            public void onFailure(ErrorResult errorResult) {  // 실패시
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
            public void onSessionClosed(ErrorResult errorResult) { // 세션 끊김
                Log.d("KAKAOTAG==", "onSessionClosed");
            }

            @Override
            public void onSuccess(UserProfile userProfile) { // 연결 성공
                Log.d("KAKAOTAG==", "onSuccess");
                USER_PROFILE = userProfile; // 카카오 유저 정보 저장

                //동기화 작업
                GetUserAsyncTask asyncTask = new GetUserAsyncTask();
                asyncTask.execute(String.valueOf(USER_PROFILE.getId())); // 파라미터는 doInBackground() 여기 파라미터로 사용됨
            }

            /**
             * 세션 오픈은 성공했으나 사용자 정보 요청 결과 사용자 가입이 안된 상태로
             * 일반적으로 가입창으로 이동한다.
             * 자동 가입 앱이 아닌 경우에만 호출된다.
             */
            @Override
            public void onNotSignedUp() {
                Log.d("KAKAOTAG==", "onNotSignedUp");
            }
        });
    }

    public class GetUserAsyncTask extends AsyncTask<String, Void, Boolean> {
        Retrofit retrofit; // 라이브러리, REST API 통신을 위함, 개꿀임
        APIService service;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // retrofit 사용, GSON 컨버터 사용
            retrofit = new Retrofit.Builder()
                    .baseUrl(GlobalApplication.SERVER_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            service = retrofit.create(APIService.class);
        }

        @Override
        protected void onPostExecute(Boolean isExist) {
            super.onPostExecute(isExist);

            if(isExist) { // 이미 카카오와 연동 된 유저면 바로 메인으로
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else { // 그렇지 않을 경우, DB에 저장을 위한 KakaoSignupActivity로 이동
                Intent intent = new Intent(LoginActivity.this, KakaoSignupActivity.class);
                startActivity(intent);
                finish();
            }
        }

        @Override
        protected Boolean doInBackground(String... uid) {
            // 유저 uid 이용해 등록여부 확인
            Call<List<User>> users = service.getUser(uid[0]); // Call 객체는 retrofit에 있음,

            try {
                // Response 객체도 retrofit 관련
                Response<List<User>> response = users.execute();
                if(response.code() == 404) { // 아직 등록 X
                    return false;
                } else if(response.code() == 500) { // 서버 오류
                    return false;
                } else if(response.code() == 200) { // 이미 등록 O
                    if(response.body().size() == 1) {
                        /*
                        Call<List<Event>> callCalendar = service.getUserEvents(uid[0]);
                        Response<List<Event>> calendars = callCalendar.execute();
                        if(calendars.code() == 200) {
                            for(int i = 0; i < calendars.body().size(); i++) {
                                Log.d("item", calendars.body().get(i).name);
                            }
                        }
*/
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
