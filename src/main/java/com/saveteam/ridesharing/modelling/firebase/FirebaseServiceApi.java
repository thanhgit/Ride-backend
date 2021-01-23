package com.saveteam.ridesharing.modelling.firebase;

import com.fasterxml.jackson.databind.deser.DataFormatReaders;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.saveteam.ridesharing.RidesharingApplication;
import com.saveteam.ridesharing.modelling.firebase.model.Matching;
import com.saveteam.ridesharing.modelling.firebase.model.Query;
import com.saveteam.ridesharing.modelling.firebase.model.Trip;
import com.saveteam.ridesharing.modelling.firebase.model.TripFB;
import com.saveteam.ridesharing.modelling.firebase.utils.FirebaseDBUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class FirebaseServiceApi implements FirebaseService {

    private static String TEST_PATHS = "offertrips";
    private static String TEST_QUERIES = "testqueries";

    private static String PATHS = "paths";
    private static String QUERIES = "queries";

    private List<TripFB> trips;
    private List<Query> queries;


    public FirebaseServiceApi() {
    }

    public List<TripFB> getTrips() {
        return FirebaseDBUtils.getTrips(TEST_PATHS);
    }


    @Override
    public List<Query> getQueries() {
        queries = FirebaseDBUtils.getQueries(QUERIES);

        return queries;
    }

    @Override
    public List<TripFB> getTripWithDatabaseAndTime(String db, long time) {
        return FirebaseDBUtils.getTripFromTime(db, time);
    }

    @Override
    public boolean insertMatching(Matching matching) {
        return FirebaseDBUtils.insertMatching(matching);
    }

    public static final void main(String[] args) {
        FirebaseService service = new FirebaseServiceApi();
        List<TripFB> trips = service.getTrips();
        List<Query> queries = service.getQueries();

        System.out.println("..............................size trips "+trips.size());
        System.out.println("..............................size queries "+queries.size());
    }


}
