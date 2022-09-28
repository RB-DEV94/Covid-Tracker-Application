package com.covid.track.Services;


import com.covid.track.Models.LocationStats;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.*;
import java.util.ArrayList;
import java.util.List;


//Tracker Service which gets the latest covid data
@Service
public class CovidTrackerService
{
    private static String VIRUS_URL="https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_confirmed_global.csv";

    public List<LocationStats> getAllstats() {
        return allstats;
    }

    private List<LocationStats> allstats=new ArrayList<>();
    @PostConstruct
   @Scheduled(cron = "* * 1 * * *")
    public void fetchVirusData() throws IOException, InterruptedException {
        List<LocationStats> newStats =new ArrayList<>();
        HttpClient client=HttpClient.newHttpClient();
        HttpRequest request= HttpRequest.newBuilder().uri(URI.create(VIRUS_URL)).build();
        HttpResponse<String> response=client.send(request,HttpResponse.BodyHandlers.ofString());
        StringReader csvBody=new StringReader(response.body());

        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(csvBody);
        for (CSVRecord record : records) {
            LocationStats loc=new LocationStats();
            loc.setState(record.get("Province/State"));
            loc.setCountry(record.get("Country/Region"));
             int latest=Integer.parseInt(record.get(record.size()-1));
             int prev=Integer.parseInt(record.get(record.size()-2));
             loc.setLastestTotalCases(latest);
             loc.setDiffprev(latest-prev);
              System.out.println(loc);
              newStats.add(loc);

        }
        this.allstats=newStats;
    }

}
