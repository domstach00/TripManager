package com.example.tripmanager.service;

import com.example.tripmanager.model.TripPlan;
import com.example.tripmanager.model.TripPlanDto;

import java.util.List;

public interface TripPlanService {
    List<TripPlan> getAllTripPlansForTrip(String tripId);
    TripPlan insertTripPlan(TripPlanDto tripPlan, String tripId);
    void deleteTripPlan(String tripPlanId);
    TripPlan patchTripPlan(TripPlanDto tripPlanDto);
}
