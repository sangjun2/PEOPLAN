package com.sang.peoplan;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by Sangjun on 2018-02-22.
 */

public class DaySchedule {
    @SerializedName("date")
    public Date date;
    @SerializedName("events")
    public Event[] events;

    public DaySchedule() {
    }

    public DaySchedule(Date date, Event[] events) {
        this.date = date;
        this.events = events;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Event[] getEvents() {
        return events;
    }

    public void setEvents(Event[] events) {
        this.events = events;
    }
}
