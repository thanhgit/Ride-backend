package com.saveteam.ridesharing.controller;

import com.saveteam.ridesharing.modelling.ServiceApi;
import com.saveteam.ridesharing.modelling.firebase.model.Query;
import com.saveteam.ridesharing.modelling.firebase.model.Trip;
import com.saveteam.ridesharing.modelling.firebase.model.TripFB;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/v1/ridesharing/firebase")
public class FirebaseController {
    @GetMapping("")
    public @ResponseBody String home() {
        List<String> urls = new ArrayList<>();
        return "Welcome to ride sharing firebase api /n";
    }

    @GetMapping("/paths")
    public @ResponseBody
    List<TripFB> getTrips() throws InterruptedException {
        ServiceApi serviceApi = new ServiceApi();
        List<TripFB> trips = serviceApi.getTrips();
        Thread.sleep(5000);
        return trips;
    }

    @GetMapping("/queries")
    public @ResponseBody
    List<Query> getQueries() throws InterruptedException {
        ServiceApi serviceApi = new ServiceApi();
        List<Query> queries = serviceApi.getQueries();
        Thread.sleep(5000);
        return queries;
    }
}
