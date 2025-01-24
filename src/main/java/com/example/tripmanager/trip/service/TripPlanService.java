package com.example.tripmanager.trip.service;

import com.example.tripmanager.account.model.Account;
import com.example.tripmanager.trip.model.tripPlan.TripPlan;
import com.example.tripmanager.trip.model.tripPlan.TripPlanDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TripPlanService {
    Page<TripPlan> getAllTripPlansForTrip(Pageable pageable, String tripId, Account currentAccount);
    TripPlan insertTripPlan(TripPlanDto tripPlan, String tripId);
    void deleteTripPlan(String tripPlanId, Account currentAccount);
    TripPlan patchTripPlan(TripPlan updatedTripPlan, String tripId);
    Page<TripPlan> getAllTripPlansWithMapElement(Pageable pageable, String tripId, Account currentAccount);
}
