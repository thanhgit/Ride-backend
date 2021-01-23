package com.saveteam.ridesharing.business;

import com.saveteam.ridesharing.ParamManager;
import com.saveteam.ridesharing.modelling.firebase.model.Query;

import java.util.List;

public class MatchingForSchedule {
        List<Query> queries;
        double threshold;
        long time;
        MatchingListener listener;

        public interface MatchingListener {
            void success(List<String> users, List<Double> percents);
            void fail(String error);
        }

        public MatchingForSchedule(double threshold, long time, MatchingListener listener) {
            this.threshold = threshold;
            this.time = time;
            this.listener = listener;
        }

        public void schedule() {
            List<Query> queries = MatchingRoute.getSearchRide(3000);
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            MatchingForSearch matchingForSearch = new MatchingForSearch(queries.get(0), threshold, 10000, new MatchingForSearch.MatchingListener() {
                @Override
                public void success(List<String> users, List<Double> percents) {
                    listener.success(users, percents);
                }

                @Override
                public void fail(String error) {
                    listener.fail(error);
                }
            });

            matchingForSearch.init();
            matchingForSearch.matching();
        }
    }
