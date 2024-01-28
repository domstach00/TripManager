package com.example.tripmanager.service;

import com.example.tripmanager.exception.ItemNotFound;
import com.example.tripmanager.mapper.TripPlanMapper;
import com.example.tripmanager.model.Trip;
import com.example.tripmanager.model.TripPlan;
import com.example.tripmanager.model.TripPlanDto;
import com.example.tripmanager.repository.TripPlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class TripPlanServiceImpl implements TripPlanService{
    @Autowired
    private TripPlanRepository tripPlanRepository;
    @Autowired
    private TripService tripService;
    @Autowired
    private GoogleMapPinService googleMapPinService;
    @Autowired
    private TripPlanMapper tripPlanMapper;

    @Override
    public List<TripPlan> getAllTripPlansForTrip(String tripId) {
        Trip trip = tripService.getTripById(tripId)
                .orElseThrow(() -> new ItemNotFound("Trip not found - id=" + tripId));
        return this.tripPlanRepository.findAllByTrip(trip);
    }

    @Override
    public TripPlan insertTripPlan(TripPlanDto tripPlanDto, String tripId) {
        Trip trip = tripService.getTripById(tripId)
                .orElseThrow(() -> new ItemNotFound("Trip not found - id=" + tripId));
        TripPlan tripPlan = tripPlanMapper.fromDto(tripPlanDto, trip);

        if (tripPlan.getMapElement() != null) {
            tripPlan.setMapElement(this.googleMapPinService.insertGoogleMapPin(tripPlan.getMapElement()));
        }
        return this.tripPlanRepository.insert(tripPlan);
    }

    @Override
    public void deleteTripPlan(String tripPlanId) {
        TripPlan tripPlanToDelete = this.tripPlanRepository.findById(tripPlanId)
                .orElseThrow(() -> new ItemNotFound("TripPlan not found - id=" + tripPlanId));
        this.tripPlanRepository.delete(tripPlanToDelete);
    }

    @Override
    public TripPlan patchTripPlan(TripPlanDto updatedTripPlanDto) {
        TripPlan originalTripPlan = tripPlanRepository.findById(updatedTripPlanDto.getId())
                .orElseThrow(() -> new ItemNotFound("TripPlan not found - id=" + updatedTripPlanDto.getTripId()));
        updatedTripPlanDto.checkPatchValidation(tripPlanMapper.toDto(originalTripPlan));

        if (!Objects.equals(updatedTripPlanDto.getMapElement(), originalTripPlan.getMapElement())) {
            if (originalTripPlan.getMapElement() != null) {
                googleMapPinService.deleteGoogleMapPin(originalTripPlan.getMapElement().getId());
            }
            if (updatedTripPlanDto.getMapElement() != null) {
                googleMapPinService.insertGoogleMapPin(updatedTripPlanDto.getMapElement());
            }
        }

        TripPlan updatedTripPlan = tripPlanMapper.fromDto(updatedTripPlanDto, originalTripPlan.getTrip());
        return tripPlanRepository.save(updatedTripPlan);
    }

}
