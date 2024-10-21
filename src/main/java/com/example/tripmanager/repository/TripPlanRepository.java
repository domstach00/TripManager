package com.example.tripmanager.repository;

import com.example.tripmanager.model.googleMapPin.GoogleMapPin;
import com.example.tripmanager.model.trip.tripPlan.TripPlan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface TripPlanRepository extends MongoRepository<TripPlan, String> {
    Page<TripPlan> findAllByTripId(Pageable pageable, String tripId, String accountId);
    Optional<TripPlan> findByIdWhereUserIsAdmin(String tripPlanId, String accountId);
    Page<GoogleMapPin> findAllGoogleMapPinsForTripId(Pageable pageable, String tripId, String accountId);

}
