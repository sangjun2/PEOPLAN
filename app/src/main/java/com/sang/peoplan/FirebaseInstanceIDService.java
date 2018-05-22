package com.sang.peoplan;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Sangjun on 2018-05-03.
 */

public class FirebaseInstanceIDService extends FirebaseInstanceIdService {
    private static final String TAG = "FirebaseIIDService";

    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        SharedPreferences sharedPreferences = getSharedPreferences("TOKEN", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("token", refreshedToken);
        editor.commit();

        Log.d(TAG, "Refreshed token: " + refreshedToken);
        if(SplashActivity.USER != null) {
            refreshTokenToServer(refreshedToken);
        }
    }

    private void refreshTokenToServer(String token) {
        Token t = new Token(String.valueOf(SplashActivity.USER.get_id()), token);
        RefreshTokenAsyncTask task = new RefreshTokenAsyncTask();
        task.execute(t);
    }

    private class RefreshTokenAsyncTask extends AsyncTask<Token, Void, Boolean> {
        Retrofit retrofit;
        APIService service;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            retrofit = new Retrofit.Builder()
                    .baseUrl(GlobalApplication.SERVER_URL)
                    .build();

            service = retrofit.create(APIService.class);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
        }

        @Override
        protected Boolean doInBackground(Token... tokens) {
            Token token = tokens[0];

            try {
                Call<Token> call = service.refreshToken(token);

                if(call.execute().code() == 200) {
                    return true;
                } else {
                    return false;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }
    }
}
