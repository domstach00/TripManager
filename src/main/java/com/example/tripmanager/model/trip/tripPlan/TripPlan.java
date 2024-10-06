package com.example.tripmanager.model.trip.tripPlan;

import com.example.tripmanager.model.AbstractAuditable;
import com.example.tripmanager.model.googleMapPin.GoogleMapPin;
import com.example.tripmanager.model.trip.Trip;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;


@Document(collection = "tripPlans")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class TripPlan extends AbstractAuditable {
    private String name;
    private int day;
    private double cost;
    private String desc;
    private String link;

    @DocumentReference
    private Trip trip;

    private GoogleMapPin mapElement;

}
