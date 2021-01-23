package com.saveteam.ridesharing.controller;

import com.saveteam.ridesharing.modelling.ServiceApi;
import com.saveteam.ridesharing.modelling.here.model.Place;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/v1/ridesharing/here")
public class HereController {
    @GetMapping("/search/{query}")
    public @ResponseBody
    Place searchPlace(@PathVariable("query")String query) throws InterruptedException {
        ServiceApi service = new ServiceApi();
        service.searchPlace(query);
        Thread.sleep(3000);
        return service.getPlace();
    }
}
