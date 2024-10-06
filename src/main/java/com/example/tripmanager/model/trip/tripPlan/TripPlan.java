package com.example.tripmanager.model.trip.tripPlan;

import com.example.tripmanager.model.AbstractAuditable;
import com.example.tripmanager.model.googleMapPin.GoogleMapPin;
import com.example.tripmanager.model.trip.Trip;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;


@Document(collection = "tripPlans")
public class TripPlan extends AbstractAuditable {
    private String name;
    private int day;
    private double cost;
    private String desc;
    private String link;

    @DocumentReference
    private Trip trip;

    private GoogleMapPin mapElement;

    public TripPlan() {
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

    public Trip getTrip() {
        return trip;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
    }

    public GoogleMapPin getMapElement() {
        return mapElement;
    }

    public void setMapElement(GoogleMapPin mapElement) {
        this.mapElement = mapElement;
    }
}
