package com.saveteam.ridesharing.modelling.firebase.utils;

import com.google.firebase.database.*;
import com.saveteam.ridesharing.modelling.firebase.model.Matching;
import com.saveteam.ridesharing.modelling.firebase.model.Query;
import com.saveteam.ridesharing.modelling.firebase.model.Trip;
import com.saveteam.ridesharing.modelling.firebase.model.TripFB;

import java.util.ArrayList;
import java.util.List;

public class FirebaseDBUtils {

    public static List<TripFB> getTripFromTime(String fromDB, long time) {
        List<TripFB> result = new ArrayList<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child(fromDB);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    TripFB document = data.getValue(TripFB.class);
                    result.add(document);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.out.println("error");
            }
        });

        try{
            Thread.sleep(time);
        }catch (Exception e) {
            System.err.println("error");
        }

        return result;
    }

    public interface GetTripFBListener {
        void done(List<TripFB> trips);
    }

    public static void getTripUtilDone(DatabaseReference ref,String fromDB, GetTripFBListener listener) {
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<TripFB> results = new ArrayList<>();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    TripFB document = data.getValue(TripFB.class);
                    results.add(document);
                }

                listener.done(results);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.out.println("error");
            }
        });
    }

    public static List<TripFB> getTrips(String fromDB) {
        return getTripFromTime(fromDB, 40000);
    }

    public static  List<Query> getQueries(String fromDB) {
        List<Query> result = new ArrayList<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child(fromDB);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    TripFB document = data.getValue(TripFB.class);
                    result.add(new Query(data.getKey(), document));
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.out.println("error");
            }
        });

        try{
            Thread.sleep(3000);
        }catch (Exception e) {
            System.err.println("error");
        }
        return result;
    }

    public static boolean insertMatching(Matching data) {
        boolean isDone = false;
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child("matching");
        String key = dbRef.push().getKey();
        dbRef.child(key).setValueAsync(data);

        try{
            Thread.sleep(1000);
        }catch (Exception e) {
            System.err.println("error");
        }
        return isDone;
    }
}
