package com.example.tripmanager.service;

import com.example.tripmanager.model.GoogleMapPin;

import java.util.List;

public interface GoogleMapPinService {
    GoogleMapPin insertGoogleMapPin(GoogleMapPin googleMapPin);
    List<GoogleMapPin> findAll();
}
