package com.saveteam.ridesharing.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletContext;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/v1/ridesharing")
public class HomeController {
    @GetMapping("")
    public @ResponseBody String home() {
        List<String> urls = new ArrayList<>();
        urls.add("GET http://35.237.166.237/v1/ridesharing/firebase/paths");
        urls.add("GET http://35.237.166.237/v1/ridesharing/firebase/queries");
        urls.add("GET http://35.237.166.237/v1/ridesharing/here/search/suoi tien quan 9");
        urls.add("GET http://35.237.166.237/v1/ridesharing/matching/schedule/{threshold}");
        urls.add("POST http://35.237.166.237/v1/ridesharing/matching/search body { threshold, query: { key, trip } }");

        String result = "Welcom to ride sharing api . ";

        for (String url : urls) {
            result += "\n" + url;
        }

        return result;
    }
}
