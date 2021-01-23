package com.saveteam.ridesharing.modelling.firebase.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Geo {
    public double lat;
    public double lng;
    public long cellId;
    public String title;

    public Geo() {
    }

    public Geo(double lat, double lng, long cellId) {
        this.lat = lat;
        this.lng = lng;
        this.cellId = cellId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Exclude
    public String print() {
        String tripStr = "( " + this.lat + ", " + this.lng + ") - " + this.cellId;
        return tripStr;
    }
}
