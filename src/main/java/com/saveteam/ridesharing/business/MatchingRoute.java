package com.saveteam.ridesharing.business;

import com.saveteam.ridesharing.business.lsh.LSH;
import com.saveteam.ridesharing.modelling.DTO.MatchingResponse;
import com.saveteam.ridesharing.modelling.ServiceApi;
import com.saveteam.ridesharing.modelling.firebase.model.*;
import org.springframework.util.StringUtils;
import java.util.*;

public class MatchingRoute {
    public static String SPLIT = "_";

    public static List<String> shinglingList(List<Geo> geos) {
        List<String> result = new ArrayList<>();
        for (int index = 0; index < geos.size() - 1; index++) {
            // C1 result.add(geos.get(index).cellId + SPLIT +geos.get(index+1).cellId);
            if (geos.get(index).cellId != geos.get(index + 1).cellId) {
                result.add(geos.get(index).cellId + SPLIT +geos.get(index+1).cellId);
            }
        }
        return result;
    }

    public static String documentization(List<String> strs) {
        String result = "";
        for (String str : strs) {
            result += str + " ";
        }
        return result;
    }

    public static boolean detectCounter(String doc1, String doc2) {
        String[] set1 = StringUtils.split(doc1, SPLIT);
        String[] set2 = StringUtils.split(doc2, SPLIT);
        return set1[0].equals(set2[1]) && set1[1].equals(set2[0]);
    }

    public static List<TripFB> getOfferRide(long time) {
        ServiceApi serviceApi = ServiceApi.getInstance();
        List<TripFB> trips = serviceApi.getTripWithDatabaseAndTime("offertrips", time);
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return trips;

    }

    public static List<Query> getSearchRide(long time) {
        ServiceApi serviceApi = ServiceApi.getInstance();
        List<Query> queries = serviceApi.getQueries();
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return queries;
    }

    public static List<List<String>> shingling(List<TripFB> trips) {
        List<List<String>> shinglingData = new ArrayList<>();
        List<String> lines = new ArrayList<>();
        for (TripFB trip : trips) {
            shinglingData.add(shinglingList(trip.getPaths()));
            String str = "";
            for (Geo geo : trip.getPaths()) {
                str += geo.cellId + " ";
            }
            lines.add(str);
        }

        return shinglingData;
    }

    public static List<TripFB> removeTripByLength(List<TripFB> trips, List<Query> queries) {
        List<TripFB> results = new ArrayList<>();
        int sizeOfQuery = queries.get(0).getTrip().getPaths().size();
        String queryId = queries.get(0).getKey();
        for (TripFB tripFB : trips) {
            if (sizeOfQuery <= tripFB.getSize() && !queryId.equals(tripFB.getUid())) {
                results.add(tripFB);
            }
        }

        System.out.println("remain offer trips: " + results.size());
        return results;
    }

    public static Matching  matchingRequest(double threshold, long time) {
        long start = System.currentTimeMillis();
        List<TripFB> _trips = getOfferRide(time);
        List<Query> queries = getSearchRide(time);

        List<TripFB> trips = new ArrayList<>();
        /**
         * remove trip has size of path < size of query
         */

        if (queries.size() > 0) {
            trips = removeTripByLength(_trips, queries);
        } else {
            return null;
        }


        List<List<String>> data = shingling(trips);
        List<TripFB> search = new ArrayList<>();
        if (queries.size() > 0) {
            search.add(queries.get(0).getTrip());
        } else {
            return null;
        }

        List<List<String>> query = shingling(search);

        Matching result = new Matching();

        if (data.size() == 0) {
            System.out.println("not drive");
        } else if (query.size() == 0) {
            System.out.println("not ride");
        } else {
            Matching matching = lsh(data, query, queries.get(0).getKey(), threshold);
            result = matching;
        }

        long end =System.currentTimeMillis();
        System.out.println("take time: " +(end - start)/1000 +"s");

        return result;
    }

    public static MatchingResponse  matchingRequest(Query q, double threshold, long time) {
        long start = System.currentTimeMillis();
        List<TripFB> _trips = getOfferRide(time);
        List<Query> queries = new ArrayList<>();
        queries.add(q);

        List<TripFB> trips = new ArrayList<>();

        /**
         * remove trip has size of path < size of query
         */
        if (queries.size() > 0) {
            trips = removeTripByLength(_trips, queries);
        } else {
            return null;
        }

        List<List<String>> data = shingling(trips);

        List<TripFB> search = new ArrayList<>();
        search.add(queries.get(0).getTrip());
        List<List<String>> query = shingling(search);

        MatchingResponse result = null;

        if (data.size() == 0) {
            System.out.println("not drive");
        } else if (query.size() == 0) {
            System.out.println("not ride");
        } else {
            Matching matching = lsh(data, query, queries.get(0).getKey(), threshold);

            if (matching != null && matching.similarSet.size() > 0) {
                result.setKeyQuery(matching.keyQuery);

                List<String> users = new ArrayList<>();
                for (int index : matching.similarSet) {
                    users.add(trips.get(index).getUid());
                }

                result.setUsers(users);
            }
        }

        long end =System.currentTimeMillis();
        System.out.println("take time: " +(end - start)/1000 +"s");
        return result;
    }


    public static Matching lsh (List<List<String>> data, List<List<String>> queries, String key, double threshold ) {
        long maxBytes = Runtime.getRuntime().maxMemory();
        System.out.println("Max memory: " + maxBytes / 1024 / 1024 + "M");

        List<Set<Integer>> CompleteSet= new ArrayList<Set<Integer>>();
        LSH lsh = new LSH(CompleteSet);


        for(int i=0;i<data.size();i++)
        {
            CompleteSet.add(lsh.Create_Shingles_optimized(documentization(data.get(i))));
        }
        LSH<Integer> res = new LSH<Integer>(CompleteSet);
        res.getSimilarity(CompleteSet);
        res.LSHDriver();

        Set<Integer> newSet = lsh.Create_Shingles_optimized(documentization(queries.get(0)));
        CompleteSet.add(newSet);
        res.NewQuery(newSet);//to update existing state with the new set
        res.UpdateLSHMapping();
        lsh.PrintSimilarSets();

        List<Integer> setSimilar = lsh.getSimilarOf(data.size(), threshold);

        List<Double> similarPercent = new ArrayList<>();
        for (int index = 0; index < setSimilar.size(); index++) {
            if (index < data.size()) {
                System.out.println("ung cu vien: " + setSimilar.toArray()[index].toString());
                System.out.println(lsh.getPercent(data.size(), setSimilar.get(index)));
                similarPercent.add(lsh.getPercent(data.size(), setSimilar.get(index)));
            }

        }

        return new Matching(key, setSimilar, similarPercent);
    }



    public static void print(String str) {
        System.out.println("------------------------------");
        System.out.println(str);
        System.out.println("------------------------------");
    }

    public static void printStringList(List<String> strList) {
        System.out.println("----------START LIST-------");
        for (String str : strList) {
            System.out.println("------------------------------");
            System.out.println(str);
            System.out.println("------------------------------");
        }
        System.out.println("----------END LIST-------");
    }
}
