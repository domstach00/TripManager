package com.example.tripmanager.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "GoogleMapPins")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class GoogleMapPin {
    @Id
    private String id;

    private String displayName;
    private String address;
    private double locationLat;
    private double locationLng;
    private String iconUrl;
    private String name;
    private String vicinity;
}
