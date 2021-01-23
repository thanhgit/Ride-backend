package com.saveteam.ridesharing.modelling.firebase.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@IgnoreExtraProperties
public class Matching implements Serializable {
    public String keyQuery;
    public List<Integer> similarSet;
    public List<Double> similarSetPercent;

    public Matching() {
        this.similarSet = new ArrayList<>();
        this.similarSetPercent = new ArrayList<>();
    }

    public Matching(String keyQuery, List<Integer> similarSet, List<Double> similarSetPercent) {
        this.keyQuery = keyQuery;
        this.similarSet = similarSet;
        this.similarSetPercent = similarSetPercent;
    }
}
