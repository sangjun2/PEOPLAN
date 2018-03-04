package com.sang.peoplan;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by Sangjun on 2018-02-22.
 */

public class Event {
    @SerializedName("name")
    public String name;
    @SerializedName("start")
    public Date start;
    @SerializedName("end")
    public Date end;
    @SerializedName("repeat")
    public Repeat repeat;
    @SerializedName("alarm")
    public boolean alarm;

    public Event() {
    }

    public Event(String name, Date start, Date end, Repeat repeat, boolean alarm) {
        this.name = name;
        this.start = start;
        this.end = end;
        this.repeat = repeat;
        this.alarm = alarm;
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

    public Repeat getRepeats() {
        return repeat;
    }

    public void setRepeats(Repeat repeats) {
        this.repeat = repeat;
    }

    public boolean isAlarm() {
        return alarm;
    }

    public void setAlarm(boolean alarm) {
        this.alarm = alarm;
    }
}
