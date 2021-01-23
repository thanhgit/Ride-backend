package com.saveteam.ridesharing;


import com.google.firebase.database.*;
import com.saveteam.ridesharing.modelling.firebase.model.TripFB;
import com.saveteam.ridesharing.modelling.firebase.utils.FirebaseDBUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DataManager {

    private static DataManager instance;

    private Set<TripFB> offerTrips;

    private DatabaseReference ref;

    private DataManager() {
        offerTrips = new HashSet<>();
        ref = FirebaseDatabase.getInstance().getReference().child(TripFB.DB_IN_FB);
        FirebaseDBUtils.getTripUtilDone(ref,TripFB.DB_IN_FB, new FirebaseDBUtils.GetTripFBListener() {
            @Override
            public void done(List<TripFB> trips) {
                offerTrips = new HashSet<>(trips);

                System.out.println("get offer trips done");
                System.out.println("size: "+offerTrips.size());
            }
        });
    }

    public void updateData() {
        FirebaseDBUtils.getTripUtilDone(ref,TripFB.DB_IN_FB, new FirebaseDBUtils.GetTripFBListener() {
            @Override
            public void done(List<TripFB> trips) {
                offerTrips.clear();
                offerTrips = new HashSet<>(trips);

                System.out.println("get offer trips done");
                System.out.println("size: "+offerTrips.size());
            }
        });
    }

    public static DataManager getInstance() {
        if (instance == null) {
            instance = new DataManager();
        }

        return instance;
    }

    public List<TripFB> getOfferTrips() {
        List<TripFB> result = new ArrayList<>(offerTrips);
        return result;
    }
}
