package com.example.tripmanager.model;

import lombok.Data;

@Data
public class TripPlanDto {
    private String id;

    private String displayName;
    private int day;
    private double cost;
    private String desc;
    private String link;

    private TripDto trip;
    private GoogleMapPin mapElement;
}
