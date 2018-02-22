package com.sang.peoplan;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Sangjun on 2018-02-22.
 */

public class Repeat {
    @SerializedName("year")
    public boolean year;
    @SerializedName("month")
    public boolean month;
    @SerializedName("oneWeek")
    public boolean oneWeek;
    @SerializedName("twoWeek")
    public boolean twoWeek;
    @SerializedName("day")
    public int number;

    public Repeat() {
        this.year = false;
        this.month = false;
        this.oneWeek = false;
        this.twoWeek = false;
        this.number = 0;
    }

    public Repeat(boolean year, boolean month, boolean oneWeek, boolean twoWeek, int number) {
        this.year = year;
        this.month = month;
        this.oneWeek = oneWeek;
        this.twoWeek = twoWeek;
        this.number = number;
    }

    public boolean isYear() {
        return year;
    }

    public void setYear(boolean year) {
        this.year = year;
    }

    public boolean isMonth() {
        return month;
    }

    public void setMonth(boolean month) {
        this.month = month;
    }

    public boolean isOneWeek() {
        return oneWeek;
    }

    public void setOneWeek(boolean oneWeek) {
        this.oneWeek = oneWeek;
    }

    public boolean isTwoWeek() {
        return twoWeek;
    }

    public void setTwoWeek(boolean twoWeek) {
        this.twoWeek = twoWeek;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
