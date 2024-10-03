package com.example.tripmanager.service;

import com.example.tripmanager.model.trip.tripPlan.TripPlan;
import com.example.tripmanager.model.trip.tripPlan.TripPlanDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TripPlanService {
    Page<TripPlan> getAllTripPlansForTrip(Pageable pageable, String tripId);
    TripPlan insertTripPlan(TripPlanDto tripPlan, String tripId);
    void deleteTripPlan(String tripPlanId);
    TripPlan patchTripPlan(TripPlanDto tripPlanDto);
}
