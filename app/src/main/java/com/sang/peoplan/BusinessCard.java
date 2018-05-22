package com.sang.peoplan;

import com.google.gson.annotations.SerializedName;


/**
 * Created by sanginLee on 2018-03-12.
 */

public class BusinessCard {
    @SerializedName("_id")
    public String _id;
    @SerializedName("owner")
    public String owner;
    @SerializedName("removed")
    public boolean removed;
    @SerializedName("counter")
    public int counter;
    @SerializedName("name")
    public String name;
    @SerializedName("department")
    public String department;
    @SerializedName("tel")
    public String tel;
    @SerializedName("address")
    public String address;
    @SerializedName("img")
    public String img;

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public BusinessCard(){

    }

    public BusinessCard(String owner, String name, String department, String tel, String address, String img){
        this.owner = owner;
        this.name = name;
        this.department = department;
        this.tel = tel;
        this.address =  address;
        this.img = img;
    }

    public BusinessCard(String _id, String owner, boolean removed, int counter, String name, String department, String tel, String address, String img) {
        this._id = _id;
        this.owner = owner;
        this.removed = removed;
        this.counter = counter;
        this.name = name;
        this.department = department;
        this.tel = tel;
        this.address = address;
        this.img = img;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public boolean isRemoved() {
        return removed;
    }

    public void setRemoved(boolean removed) {
        this.removed = removed;
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
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
    public String get_id() {
        return _id;
    }
    public void set_id(String _id) {
        this._id = _id;
    }

}
