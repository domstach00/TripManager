package com.example.tripmanager.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class GoogleMapPin {
    private String displayName;
    private String address;
    private double locationLat;
    private double locationLng;
    private String iconUrl;
    private String icon;
    private String name;
    private String vicinity;
}
