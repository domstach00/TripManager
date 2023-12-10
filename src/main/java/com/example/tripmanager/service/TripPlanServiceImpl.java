package com.example.tripmanager.service;

import com.example.tripmanager.exception.ItemNotFound;
import com.example.tripmanager.model.Trip;
import com.example.tripmanager.model.TripPlan;
import com.example.tripmanager.repository.TripPlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TripPlanServiceImpl implements TripPlanService{
    @Autowired
    private TripPlanRepository tripPlanRepository;
    @Autowired
    private TripService tripService;
    @Autowired
    private GoogleMapPinService googleMapPinService;

    @Override
    public List<TripPlan> getAllTripPlansForTrip(String tripId) {
        Trip trip = tripService.getTripById(tripId)
                .orElseThrow(() -> new ItemNotFound("Trip not found - id=" + tripId));
        return this.tripPlanRepository.findAllByTrip(trip);
    }

    @Override
    public TripPlan insertTripPlan(TripPlan tripPlan) {
        if (tripPlan.getMapElement() != null) {
            tripPlan.setMapElement(this.googleMapPinService.insertGoogleMapPin(tripPlan.getMapElement()));
        }
        return this.tripPlanRepository.insert(tripPlan);
    }
}
