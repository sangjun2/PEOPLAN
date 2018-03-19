package com.sang.peoplan;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Sangjun on 2018-01-30.
 */

public class User {
    @SerializedName("id")
    private String kakaoUID;
    @SerializedName("name")
    private String name;
    @SerializedName("tel")
    private String tel;
    @SerializedName("email")
    private String email;

    public User() {
        this.kakaoUID = null;
        this.name = null;
        this.tel = null;
        this.email = null;
    }

    public User(String kakaoUID, String name, String tel, String email) {
        this.kakaoUID = kakaoUID;
        this.name = name;
        this.tel = tel;
        this.email = email;
    }

    public String getKakaoUID() {
        return kakaoUID;
    }

    public void setKakaoUID(String kakaoUID) {
        this.kakaoUID = kakaoUID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
