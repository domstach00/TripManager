package com.example.tripmanager.trip.model.tripPlan;

import com.example.tripmanager.shared.model.AbstractAuditableDto;
import com.example.tripmanager.trip.model.googleMapPin.GoogleMapPin;

public class TripPlanDto extends AbstractAuditableDto {
    private String name;
    private int day;
    private double cost;
    private String desc;
    private String link;
    private String tripId;

    private GoogleMapPin mapElement;

    public TripPlanDto() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getTripId() {
        return tripId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }

    public GoogleMapPin getMapElement() {
        return mapElement;
    }

    public void setMapElement(GoogleMapPin mapElement) {
        this.mapElement = mapElement;
    }
}
