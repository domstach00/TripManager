package com.example.tripmanager.model;

import com.example.tripmanager.model.user.User;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.time.LocalDateTime;

@Document(collection = "TripPlans")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class TripPlan {
    @Id
    private String id;

    private String displayName;
    private int day;
    private double cost;
    private String desc;
    private String link;
    private User createdBy;
    private LocalDateTime createdDateTime;

    @DocumentReference
    private Trip trip;


    @DocumentReference
    private GoogleMapPin mapElement;
}
