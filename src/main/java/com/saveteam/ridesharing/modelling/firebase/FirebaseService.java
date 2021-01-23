package com.saveteam.ridesharing.modelling.firebase;

import com.saveteam.ridesharing.modelling.firebase.model.Matching;
import com.saveteam.ridesharing.modelling.firebase.model.Query;
import com.saveteam.ridesharing.modelling.firebase.model.Trip;
import com.saveteam.ridesharing.modelling.firebase.model.TripFB;

import java.util.List;

public interface FirebaseService {

    List<TripFB> getTrips();
    List<Query> getQueries();
    List<TripFB> getTripWithDatabaseAndTime(String db, long time);

    boolean insertMatching(Matching matching);
}
