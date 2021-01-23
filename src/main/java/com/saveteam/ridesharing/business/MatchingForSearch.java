package com.saveteam.ridesharing.business;

import com.saveteam.ridesharing.DataManager;
import com.saveteam.ridesharing.ParamManager;
import com.saveteam.ridesharing.business.lsh.LSH;
import com.saveteam.ridesharing.modelling.firebase.model.*;
import org.springframework.scheduling.annotation.Async;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MatchingForSearch {

    Query query;
    double threshold;
    long time;
    MatchingListener listener;

    List<TripFB> trips;
    List<Query> queries;

    long start;
    long end;

    public interface MatchingListener {
        void success(List<String> users, List<Double> percents);
        void fail(String error);
    }

    public MatchingForSearch(Query query, double threshold, long time, MatchingListener listener) {
        this.query = query;
        this.threshold = threshold;
        this.time = time;
        this.listener = listener;
    }

    @Async
    public void init() {
        start = System.currentTimeMillis();

        DataManager.getInstance().updateData();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        List<TripFB> _trips = DataManager.getInstance().getOfferTrips();

        if (_trips == null || _trips.size() == 0) {
            _trips = MatchingRoute.getOfferRide(20000);
        }

        System.out.println("Offer trips size: " + (_trips != null ? _trips.size() : "null"));

        queries = new ArrayList<>();
        queries.add(query);

        if (queries.size() > 0) {
            trips = MatchingRoute.removeTripByLength(_trips, queries);
        }
    }

    public void matching() {
        if (trips.size() == 0) {
            listener.fail("Nobody offer ride");
        } else if (queries.size() == 0) {
            listener.fail("Error query");
        } else {
            Matching matching = null;
            if (trips.size() > 0) {
                List<List<String>> data = MatchingRoute.shingling(trips);
                List<TripFB> search = new ArrayList<>();
                search.add(queries.get(0).getTrip());
                List<List<String>> query = MatchingRoute.shingling(search);

                matching = lsh(data, query, queries.get(0).getKey(), threshold);

                List<String> users = new ArrayList<>();
                for (int index : matching.similarSet) {
                    if (index < trips.size()) {
                        users.add(trips.get(index).getUid());
                    }
                }
                listener.success(users, matching.similarSetPercent);
            } else {
                TripFB q = queries.get(0).getTrip();
                List<Set<Long>> offerRideSet = new ArrayList<>();
                Set<Long> querySet = new HashSet<>();
                for (TripFB trip : trips) {
                    Set<Long> tripSet = new HashSet<>();
                    for (Geo geo : trip.getPaths()) {
                        tripSet.add(geo.cellId);
                    }

                    offerRideSet.add(tripSet);
                }

                for (Geo geo : q.getPaths()) {
                    querySet.add(geo.cellId);
                }

                int count = 0;
                List<Double> percents = new ArrayList<>();
                for (Set<Long> offerTrip : offerRideSet) {
                    int index =0;
                    for (int index1=0; index1< offerTrip.size(); index1++) {
                        for (int index2=0; index2 < querySet.size(); index2++) {
                            if (offerTrip.toArray()[index1].equals(querySet.toArray()[index2])) {
                                if (index2 >= index) {
                                    count++;
                                    index = index2;
                                }

                                break;
                            }
                        }
                    }

                    percents.add((double)count/offerTrip.size());
                    count = 0;
                }

                List<String> users = new ArrayList<>();
                for (TripFB trip : trips) {
                    users.add(trip.getUid());
                }

                listener.success(users, percents);

            }
        }

        end =System.currentTimeMillis();
        System.out.println("take time: " +(end - start)/1000 +"s");
    }

    public Matching lsh (List<List<String>> data, List<List<String>> queries, String key, double threshold ) {
        long maxBytes = Runtime.getRuntime().maxMemory();
        System.out.println("Max memory: " + maxBytes / 1024 / 1024 + "M");


        List<Set<Integer>> CompleteSet= new ArrayList<Set<Integer>>();

        for(int i=0;i<data.size();i++)
        {
            CompleteSet.add(Create_Shingles_optimized(documentization(data.get(i))));
        }

        LSH<Integer> res = new LSH<Integer>(CompleteSet);
        res.getSimilarity(CompleteSet);
        res.LSHDriver();

        Set<Integer> newSet = Create_Shingles_optimized(documentization(queries.get(0)));
        CompleteSet.add(newSet);
        res.NewQuery(newSet);//to update existing state with the new set
        res.UpdateLSHMapping();
        res.PrintSimilarSets();

        List<Integer> setSimilar = res.getSimilarOf(data.size(), threshold);

        List<Double> similarPercent = new ArrayList<>();
        for (int index = 0; index < setSimilar.size(); index++) {
            if (index < data.size()) {
                System.out.println("ung cu vien: " + setSimilar.toArray()[index].toString());
                System.out.println(res.getPercent(data.size(), setSimilar.get(index)));
                similarPercent.add(res.getPercent(data.size(), setSimilar.get(index)));
            }

        }

        return new Matching(key, setSimilar, similarPercent);
    }

    public static String documentization(List<String> strs) {
        String result = "";
        for (String str : strs) {
            result += str + " ";
        }
        return result;
    }

    public Set<Integer> Create_Shingles_optimized(String s)
    {
        int shingle_length = 5;

        Set<Integer> shingle_set = new HashSet<Integer>();
        String shingle;
        Integer HashedShingle;
        shingle = s.substring(0, shingle_length);
        HashedShingle = HashShingle(shingle);
        shingle_set.add(HashedShingle);
        String tempstring;

        for(int i=shingle_length;i<s.length();i++)
        {
            tempstring = shingle.substring(1);
            tempstring += s.charAt(i);
            shingle = tempstring;
            HashedShingle = HashShingle(shingle);
            shingle_set.add(HashedShingle);

        }
        return shingle_set;
    }

    public Integer HashShingle(String shingle)//to hash shingles to a smaller bucket
    {
        int shingle_length = 5;
        int HashMod = 10000;

        int val = 0;
        for(int i=0;i<shingle_length;i++)
        {
            val += shingle.charAt(i) * Math.pow(shingle_length, i);
        }
        return (Integer)(val%HashMod);

    }
}
