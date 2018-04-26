package com.sang.peoplan;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Sangjun on 2018-02-22.
 */

public class Event implements Serializable {
    @SerializedName("_id")
    public String _id;
    @SerializedName("name")
    public String name;
    @SerializedName("start")
    public Date start;
    @SerializedName("end")
    public Date end;
    @SerializedName("repeat")
    public int repeat;
    @SerializedName("alarm")
    public boolean alarm;
    @SerializedName("color")
    public String color;
    @SerializedName("content")
    public String content;

    public Event() {
    }

    public Event(String name, Date start, Date end, int repeat, boolean alarm, String color, String content) {
        this.name = name;
        this.start = start;
        this.end = end;
        this.repeat = repeat;
        this.alarm = alarm;
        this.color = color;
        this.content = content;
    }

    public Event(String _id, String name, Date start, Date end, int repeat, boolean alarm, String color, String content) {
        this._id = _id;
        this.name = name;
        this.start = start;
        this.end = end;
        this.repeat = repeat;
        this.alarm = alarm;
        this.color = color;
        this.content = content;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public int getRepeat() {
        return repeat;
    }

    public void setRepeat(int repeat) {
        this.repeat = repeat;
    }

    public boolean isAlarm() {
        return alarm;
    }

    public void setAlarm(boolean alarm) {
        this.alarm = alarm;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
