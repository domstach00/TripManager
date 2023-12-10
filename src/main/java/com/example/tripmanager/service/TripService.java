package com.example.tripmanager.service;

import com.example.tripmanager.model.Trip;
import com.example.tripmanager.model.TripDto;

import java.util.List;
import java.util.Optional;

public interface TripService {
    Trip createTrip(TripDto tripDto);
    Optional<Trip> getTripById(String tripId);
    List<Trip> getTripsForCurrentUser();
}
