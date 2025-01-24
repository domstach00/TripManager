package com.example.tripmanager.service;

import com.example.tripmanager.model.account.Account;
import com.example.tripmanager.model.trip.tripPlan.TripPlan;
import com.example.tripmanager.model.trip.tripPlan.TripPlanDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TripPlanService {
    Page<TripPlan> getAllTripPlansForTrip(Pageable pageable, String tripId, Account currentAccount);
    TripPlan insertTripPlan(TripPlanDto tripPlan, String tripId);
    void deleteTripPlan(String tripPlanId, Account currentAccount);
    TripPlan patchTripPlan(TripPlan updatedTripPlan, String tripId);
    Page<TripPlan> getAllTripPlansWithMapElement(Pageable pageable, String tripId, Account currentAccount);
}
