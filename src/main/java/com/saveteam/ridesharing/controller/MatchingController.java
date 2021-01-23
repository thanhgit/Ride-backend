package com.saveteam.ridesharing.controller;

import com.saveteam.ridesharing.business.MatchingForSchedule;
import com.saveteam.ridesharing.business.MatchingForSearch;
import com.saveteam.ridesharing.modelling.DTO.MatchingResponse;
import com.saveteam.ridesharing.modelling.DTO.QueryRequest;
import com.saveteam.ridesharing.modelling.firebase.model.Query;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/v1/ridesharing/matching/")
public class MatchingController {

    @GetMapping("schedule/{threshold}")
    public @ResponseBody
    MatchingResponse startMatchingInServer(@PathVariable(name = "threshold") String threshold) {
        double thres = 0.8;
        try {
            thres = Double.parseDouble(threshold);
            thres = thres == 0 ? 0.8 : thres;
        } catch (Exception e) {

        }

        MatchingResponse matching = new MatchingResponse();
        matching.setKeyQuery("schedule");

        MatchingForSchedule matchingForSchedule = new MatchingForSchedule(thres, 10000, new MatchingForSchedule.MatchingListener() {
            @Override
            public void success(List<String> users, List<Double> percents) {
                matching.setUsers(users);
                matching.setPercents(percents);
            }

            @Override
            public void fail(String error) {
                matching.setKeyQuery("schedule error");
            }
        });

        matchingForSchedule.schedule();

        return matching;
    }

    @PostMapping("search")
    public @ResponseBody
    MatchingResponse startMatchingInPersional(@RequestBody QueryRequest body) {
        double threshold = body.getThreshold() == 0 ? 0.8 : body.getThreshold();
        Query query = body.getQuery();
        MatchingResponse matching = new MatchingResponse();
        matching.setKeyQuery(query.getKey());

        MatchingForSearch matchingForSearch = new MatchingForSearch(query, threshold, 10000, new MatchingForSearch.MatchingListener() {
            @Override
            public void success(List<String> users, List<Double> percents) {
                matching.setUsers(users);
                matching.setPercents(percents);
            }

            @Override
            public void fail(String error) {
                matching.setKeyQuery(error);
            }
        });

        matchingForSearch.init();
        matchingForSearch.matching();

        return matching;
    }
}
