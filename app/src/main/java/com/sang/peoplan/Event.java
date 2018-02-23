package com.sang.peoplan;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Sangjun on 2018-02-22.
 */

public class Event {
    @SerializedName("name")
    public String name;
    @SerializedName("start")
    public String start;
    @SerializedName("end")
    public String end;
    @SerializedName("repeat")
    public Repeat repeat;
    @SerializedName("alarm")
    public boolean alarm;

    public Event() {
    }

    public Event(String name, String start, String end, Repeat repeat, boolean alarm) {
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

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
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
