package com.example.tripmanager.service;

import com.example.tripmanager.model.TripPlan;

import java.util.List;

public interface TripPlanService {
    List<TripPlan> getAllTripPlansForTrip(String tripId);
    TripPlan insertTripPlan(TripPlan tripPlan);
}
