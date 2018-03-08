package com.sang.peoplan;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.squareup.picasso.Picasso;
import com.victorminerva.widget.edittext.AutofitEdittext;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class KakaoSignupActivity extends AppCompatActivity { // DB에 유저정보 저장

    UserProfile userProfile;
    ImageView userImage;
    AutofitEdittext userName;
    AutofitEdittext userEmail;
    AutofitEdittext userTel;
    Button confirmButton;

    private final String postURL = "/api/users";

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kakao_signup);
        mContext = this;

        // 카카오 연동된 유저 정보
        userProfile = SplashActivity.USER_PROFILE;

        Toolbar toolbar = findViewById(R.id.kakao_toolbar);
        TextView toolbarTitle = findViewById(R.id.confirm_toolbar_title);
        toolbarTitle.setText("프로필 설정");
        setSupportActionBar(toolbar);

        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                userTel.setText(getPhoneNumber());
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {

            }
        };
        TedPermission.with(this)
                .setPermissionListener(permissionListener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.READ_PHONE_STATE)
                .check();

        // 디자인 정보랑 코드 정보랑 매칭증
        userImage = findViewById(R.id.user_image);
        userName = findViewById(R.id.user_name);
        userEmail = findViewById(R.id.user_email);
        userTel = findViewById(R.id.user_tel);
        confirmButton = findViewById(R.id.confirm_toolbar_bt);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User user = new User(); // 어플에서 사용할 유저 클래스,
                user.setKakaoUID(String.valueOf(userProfile.getId()));
                user.setName(userName.getText().toString());
                user.setTel(userTel.getText().toString());
                user.setEmail(userEmail.getText().toString());

                // DB에 유저 정보 전달
                CreateUserAsyncTask task = new CreateUserAsyncTask();
                task.execute(user);
            }
        });

        if(userProfile.getProfileImagePath() != null) {
            Picasso.with(this).load(userProfile.getProfileImagePath()).into(userImage);
        }
        // ??
        userName.setText(userProfile.getNickname());
        userName.setSelection(userName.getText().toString().length());
        userEmail.setText(userProfile.getEmail());
    }

    public String getPhoneNumber() {
        // 휴대폰 번호 획득
        TelephonyManager telephony = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        String phoneNumber ="";

        try {
            if (telephony.getLine1Number() != null) {
                phoneNumber = telephony.getLine1Number();
            }
        } catch(SecurityException e) {
            e.printStackTrace();
        }

        return phoneNumber;
    }

    // DB에 user 객체 추가
    public class CreateUserAsyncTask extends AsyncTask<User, Void, Boolean> {
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
                Intent intent = new Intent(KakaoSignupActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }

        @Override
        protected Boolean doInBackground(User... users) {
            // 유저 정보 추가
            Call<User> user = service.createUser(users[0]);
            try {
                if(user.execute().code() == 200) { // 추가 성공
                    return true;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return false;
        }
    }

    // ?? 왜?
    public class ConnectASyncTask extends AsyncTask<HashMap<String, User>, Void, String> {
        URL url;
        HttpURLConnection urlConnection;
        InputStream is;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                if(s != null) {
                    JSONObject jsonObject = new JSONObject(s);
                    if(jsonObject.get("result") != null) {
                        int result = Integer.parseInt(jsonObject.getString("result"));
                        if(result == 1) {
                            finish();
                        } else {
                            Toast.makeText(mContext, "오류가 발생했습니다. 잠시후 시도해주세요.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(HashMap<String, User>[] hashMaps) {
            try {
                url = new URL(GlobalApplication.SERVER_URL + postURL);
                urlConnection = (HttpURLConnection) url.openConnection();

                if(hashMaps[0].get("POST") != null) {
                    User user = hashMaps[0].get("POST");
                    JSONObject jsonObject = new JSONObject();
                    setJsonData(jsonObject, "uid", user.getKakaoUID());
                    setJsonData(jsonObject, "name", user.getName());
                    setJsonData(jsonObject, "tel", user.getTel());
                    setJsonData(jsonObject, "email", user.getEmail());

                    urlConnection.setRequestProperty("Accept", "application/json");
                    urlConnection.setRequestProperty("Content-type", "application/json");

                    urlConnection.setDoOutput(true);
                    urlConnection.setDoInput(true);

                    String jsonString = jsonObject.toString();

                    OutputStream os = urlConnection.getOutputStream();
                    os.write(jsonString.getBytes("UTF-8"));
                    os.flush();

                    try {
                        is = urlConnection.getInputStream();
                        if(is != null) {
                            BufferedReader br = new BufferedReader(new InputStreamReader(is));
                            StringBuffer buf = new StringBuffer();
                            String line = br.readLine();
                            while(line != null) {
                                buf.append(line);
                                line = br.readLine();
                            }

                            return buf + "";
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        urlConnection.disconnect();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }
    }
    // ?? 왜?
    private void setJsonData(JSONObject jsonObject, String name, String data) {
        try {
            if(data == null) {
            } else if(data != null) {
                jsonObject.accumulate(name, data);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
