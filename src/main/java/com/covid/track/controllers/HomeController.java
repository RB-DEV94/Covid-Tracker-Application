package com.covid.track.controllers;

import com.covid.track.Models.LocationStats;
import com.covid.track.Services.CovidTrackerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    CovidTrackerService covidTrackerService;

    @GetMapping("/")
    public String home(Model model)
    {
        List<LocationStats> allStats=covidTrackerService.getAllstats();
        int totalcases=allStats.stream().mapToInt(stat -> stat.getLastestTotalCases()).sum();
        int totalnewcases=allStats.stream().mapToInt(stat -> stat.getDiffprev()).sum();
        model.addAttribute("locationstats",allStats);
        model.addAttribute("totalReportedCases",totalcases);
        model.addAttribute("newcases",totalnewcases);
        return "home";
    }
}
