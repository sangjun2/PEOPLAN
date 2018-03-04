package com.sang.peoplan;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Sangjun on 2018-02-23.
 */

public class Calendar {
    @SerializedName("kakaoid")
    public String kakaoid;
    @SerializedName("events")
    public Event[] events;

    public Calendar() {
    }

    public Calendar(String kakaoid, Event[] events) {
        this.kakaoid = kakaoid;
        this.events = events;
    }

    public String getKakaoid() {
        return kakaoid;
    }

    public void setKakaoid(String kakaoid) {
        this.kakaoid = kakaoid;
    }

    public Event[] getEvents() {
        return events;
    }

    public void setEvents(Event[] events) {
        this.events = events;
    }
}
