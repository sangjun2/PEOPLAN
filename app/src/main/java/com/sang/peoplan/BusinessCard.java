package com.sang.peoplan;

import com.google.gson.annotations.SerializedName;


/**
 * Created by sanginLee on 2018-03-12.
 */

public class BusinessCard {
    @SerializedName("kakaoid")
    public String kakaoid;
    @SerializedName("name")
    public String name;
    @SerializedName("department")
    public String department;
    @SerializedName("tel")
    public String tel;

    public BusinessCard(){

    }

    public BusinessCard(String kakaoid, String name, String department, String tel){
        this.kakaoid = kakaoid;
        this.name = name;
        this.department = department;
        this.tel = tel;
    }

    public String getKakaoid() {
        return kakaoid;
    }

    public void setKakaoid(String kakaoid) {
        this.kakaoid = kakaoid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }
}
