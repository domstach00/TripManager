package com.example.tripmanager.trip.model.tripPlan;

import com.example.tripmanager.shared.model.AbstractAuditable;
import com.example.tripmanager.trip.model.googleMapPin.GoogleMapPin;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = TripPlan.COLLECTION_NAME)
public class TripPlan extends AbstractAuditable {
    public static final String COLLECTION_NAME = "tripPlans";
    public static final String FIELD_NAME_NAME = "name";
    public static final String FIELD_NAME_DAY = "day";
    public static final String FIELD_NAME_COST = "cost";
    public static final String FIELD_NAME_DESC = "desc";
    public static final String FIELD_NAME_link = "link";
    public static final String FIELD_NAME_TRIP_ID = "tripId";
    public static final String FIELD_NAME_MAP_ELEMENT = "mapElement";
    private String name;
    private int day;
    private double cost;
    private String desc;
    private String link;

    @Indexed
    private ObjectId tripId;

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

    public String getTripId() {
        return toString(this.tripId);
    }

    public void setTripId(String tripId) {
        this.tripId = toObjectId(tripId);
    }

    public GoogleMapPin getMapElement() {
        return mapElement;
    }

    public void setMapElement(GoogleMapPin mapElement) {
        this.mapElement = mapElement;
    }
}
