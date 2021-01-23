package com.saveteam.ridesharing.modelling.firebase.model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.List;

@IgnoreExtraProperties
public class Trip {
    public String userName;
    public List<Geo> path;
    public Geo startGeo;
    public Geo endGeo;

    public Trip() {
    }

    public Trip(String userName, List<Geo> path, Geo startGeo, Geo endGeo) {
        this.userName = userName;
        this.path = path;
        this.startGeo = startGeo;
        this.endGeo = endGeo;
    }

//    @Exclude
//    public void insert(DatabaseReference dbRef) {
//        DatabaseReference _dbRef = dbRef.push();
//        _dbRef.child("userName").setValue(userName);
//        _dbRef.child("path").setValue(path);
//        _dbRef.child("startGeo").setValue(startGeo);
//        _dbRef.child("endGeo").setValue(endGeo);
//    }

    @Exclude
    public String print() {
        String start = " start: " + this.startGeo.print();
        String end = " end: " + this.endGeo.print();

        return this.userName + " - " + start + end;
    }

}
