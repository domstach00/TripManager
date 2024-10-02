package com.example.tripmanager.repository;

import com.example.tripmanager.model.Trip;
import com.example.tripmanager.model.TripPlan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TripPlanRepository extends MongoRepository<TripPlan, String> {
    Page<TripPlan> findAllByTrip(Pageable pageable, Trip trip);

}
