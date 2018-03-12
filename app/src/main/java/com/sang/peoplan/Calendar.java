package com.sang.peoplan;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Sangjun on 2018-02-23.
 */

public class Calendar {
    @SerializedName("id")
    public String id;
    @SerializedName("events")
    public Event[] events;

    public Calendar() {
    }

    public Calendar(String id, Event[] events) {
        this.id = id;
        this.events = events;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Event[] getEvents() {
        return events;
    }

    public void setEvents(Event[] events) {
        this.events = events;
    }
}
