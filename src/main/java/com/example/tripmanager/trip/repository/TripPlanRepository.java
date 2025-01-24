package com.example.tripmanager.trip.repository;

import com.example.tripmanager.trip.model.tripPlan.TripPlan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface TripPlanRepository extends MongoRepository<TripPlan, String> {
    Page<TripPlan> findAllByTripId(Pageable pageable, String tripId, String accountId);
    Optional<TripPlan> findByIdWhereUserIsAdmin(String tripPlanId, String accountId);
    Page<TripPlan> findAllTripPlansWithMapElementForGivenTripId(Pageable pageable, String tripId, String accountId);

}
