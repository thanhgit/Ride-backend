package com.saveteam.ridesharing.modelling.firebase.model;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.List;

@IgnoreExtraProperties
public class TripFB {
    public static final String DB_IN_FB = "offertripsv1";

    private String uid;
    private String userName;
    private String startTime;
    private Geo geoStart;
    private Geo geoEnd;
    private int size;
    private List<Geo> paths;

    public TripFB() {
    }

    public TripFB(String uid, String userName, String startTime, Geo geoStart, Geo geoEnd, int size, List<Geo> paths) {
        this.uid = uid;
        this.userName = userName;
        this.startTime = startTime;
        this.geoStart = geoStart;
        this.geoEnd = geoEnd;
        this.size = size;
        this.paths = paths;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Geo getGeoStart() {
        return geoStart;
    }

    public void setGeoStart(Geo geoStart) {
        this.geoStart = geoStart;
    }

    public Geo getGeoEnd() {
        return geoEnd;
    }

    public void setGeoEnd(Geo geoEnd) {
        this.geoEnd = geoEnd;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public List<Geo> getPaths() {
        return paths;
    }

    public void setPaths(List<Geo> paths) {
        this.paths = paths;
    }
}
