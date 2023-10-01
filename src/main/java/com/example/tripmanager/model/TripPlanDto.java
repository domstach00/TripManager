package com.example.tripmanager.model;

import org.springframework.data.mongodb.core.mapping.DocumentReference;

public class TripPlanDto {
    private String id;

    private String displayName;
    private int day;
    private double cost;
    private String desc;
    private String link;

    private GoogleMapPin mapElement;
}
