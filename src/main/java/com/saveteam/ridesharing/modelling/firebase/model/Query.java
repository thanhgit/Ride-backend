package com.saveteam.ridesharing.modelling.firebase.model;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Query {
    private String key;
    private TripFB trip;

    public Query() {
        trip = new TripFB();
    }

    public Query(String key, TripFB trip) {
        this.key = key;
        this.trip = trip;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public TripFB getTrip() {
        return trip;
    }

    public void setTrip(TripFB trip) {
        this.trip = trip;
    }
}
