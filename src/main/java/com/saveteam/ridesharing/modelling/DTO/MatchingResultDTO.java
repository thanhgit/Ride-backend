package com.saveteam.ridesharing.modelling.DTO;

import java.io.Serializable;
import java.util.List;

public class MatchingResultDTO implements Serializable {
    String key;
    List<String> users;

    public MatchingResultDTO() {
    }

    public MatchingResultDTO(String key, List<String> users) {
        this.key = key;
        this.users = users;
    }


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public List<String> getUsers() {
        return users;
    }

    public void setUsers(List<String> users) {
        this.users = users;
    }
}
