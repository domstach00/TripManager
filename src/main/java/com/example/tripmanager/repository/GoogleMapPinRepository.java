package com.example.tripmanager.repository;

import com.example.tripmanager.model.GoogleMapPin;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface GoogleMapPinRepository extends MongoRepository<GoogleMapPin, String> {
}
