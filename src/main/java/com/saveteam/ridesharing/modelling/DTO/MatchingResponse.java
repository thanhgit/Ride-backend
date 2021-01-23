package com.saveteam.ridesharing.modelling.DTO;

import com.saveteam.ridesharing.modelling.firebase.model.Trip;

import java.util.List;

public class MatchingResponse {
    private String keyQuery;
    private List<String> users;
    private List<Double> percents;

    public MatchingResponse() {

    }

    public MatchingResponse(String keyQuery, List<String> users) {
        this.keyQuery = keyQuery;
        this.users = users;
    }

    public String getKeyQuery() {
        return keyQuery;
    }

    public void setKeyQuery(String keyQuery) {
        this.keyQuery = keyQuery;
    }

    public List<String> getUsers() {
        return users;
    }

    public void setUsers(List<String> users) {
        this.users = users;
    }

    public List<Double> getPercents() {
        return percents;
    }

    public void setPercents(List<Double> percents) {
        this.percents = percents;
    }
}
