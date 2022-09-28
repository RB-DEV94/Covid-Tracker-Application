package com.covid.track.Models;


import lombok.Data;

@Data
public class LocationStats
{
    private String state;
    private String country;
    private int lastestTotalCases;
    private int diffprev;
}
