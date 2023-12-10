package com.example.tripmanager.repository;

import com.example.tripmanager.model.Trip;
import com.example.tripmanager.model.TripPlan;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TripPlanRepository extends MongoRepository<TripPlan, String> {
    List<TripPlan> findAllByTrip(Trip trip);

}
