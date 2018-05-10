package com.sang.peoplan;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Sangjun on 2018-05-04.
 */

public class Token {
    @SerializedName("kakaoid")
    private String kakaoid;
    @SerializedName("token")
    private String token;

    public Token(String kakaoid, String token) {
        this.kakaoid = kakaoid;
        this.token = token;
    }

    public String getKakaoid() {
        return kakaoid;
    }

    public void setKakaoid(String kakaoid) {
        this.kakaoid = kakaoid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
