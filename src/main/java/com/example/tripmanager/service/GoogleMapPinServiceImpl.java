package com.example.tripmanager.service;

import com.example.tripmanager.exception.ItemNotFound;
import com.example.tripmanager.model.GoogleMapPin;
import com.example.tripmanager.repository.GoogleMapPinRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoogleMapPinServiceImpl implements GoogleMapPinService{
    @Autowired
    private GoogleMapPinRepository googleMapPinRepository;
    @Override
    public GoogleMapPin insertGoogleMapPin(GoogleMapPin googleMapPin) {
        return this.googleMapPinRepository.insert(googleMapPin);
    }

    @Override
    public List<GoogleMapPin> findAll() {
        return this.googleMapPinRepository.findAll();
    }

    @Override
    public boolean deleteGoogleMapPin(String googleMapPinId) {
        GoogleMapPin googleMapPin = googleMapPinRepository.findById(googleMapPinId)
                .orElseThrow(() -> new ItemNotFound("GoogleMapPin not found - id=" + googleMapPinId));
        googleMapPinRepository.delete(googleMapPin);
        return true;
    }
}
