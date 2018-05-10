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
    @SerializedName("color")
    public String color;
    @SerializedName("content")
    public String content;
    @SerializedName("owner")
    public String owner;
    @SerializedName("participants")
    public String[] participants;


    public Event() {
    }

    public Event(String name, Date start, Date end, int repeat, String color, String content, String owner, String[] participants) {
        this.name = name;
        this.start = start;
        this.end = end;
        this.repeat = repeat;
        this.color = color;
        this.content = content;
        this.owner = owner;
        this.participants = participants;
    }

    public Event(String _id, String name, Date start, Date end, int repeat, String color, String content, String owner, String[] participants) {
        this._id = _id;
        this.name = name;
        this.start = start;
        this.end = end;
        this.repeat = repeat;
        this.color = color;
        this.content = content;
        this.owner = owner;
        this.participants = participants;
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

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String[] getParticipants() {
        return participants;
    }

    public void setParticipants(String[] participants) {
        this.participants = participants;
    }
}
