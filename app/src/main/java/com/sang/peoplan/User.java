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
    @SerializedName("_id")
    public String _id;
    @SerializedName("kakaoid")
    public String kakaoUID;
    @SerializedName("name")
    private String name;
    @SerializedName("tel")
    private String tel;
    @SerializedName("email")
    private String email;
    @SerializedName("groups")
    private String[] groups;
    @SerializedName("businesscards")
    private BusinessCard[] businessCards;
    @SerializedName("limit")
    private int limit;
    @SerializedName("token")
    private String token;

    public User() {
        this._id = null;
        this.kakaoUID = null;
        this.name = null;
        this.tel = null;
        this.email = null;
        this.groups = null;
        this.businessCards = null;
        this.limit = 1;
        this.token = null;
    }

    public User(String kakaoUID, String name, String tel, String email, String[] groups, BusinessCard[] businessCards, int limit, String token) {
        this.kakaoUID = kakaoUID;
        this.name = name;
        this.tel = tel;
        this.email = email;
        this.groups = groups;
        this.businessCards = businessCards;
        this.limit = limit;
        this.token = token;
    }

    public User(String _id, String kakaoUID, String name, String tel, String email, String[] groups, BusinessCard[] businessCards, int limit, String token) {
        this._id = _id;
        this.kakaoUID = kakaoUID;
        this.name = name;
        this.tel = tel;
        this.email = email;
        this.groups = groups;
        this.businessCards = businessCards;
        this.limit = limit;
        this.token = token;
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

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String[] getGroups() {
        return groups;
    }

    public void setGroups(String[] groups) {
        this.groups = groups;
    }

    public BusinessCard[] getBusinessCards() {
        return businessCards;
    }

    public void setBusinessCards(BusinessCard[] businessCards) {
        this.businessCards = businessCards;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
