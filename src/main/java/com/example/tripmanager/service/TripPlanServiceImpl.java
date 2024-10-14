package com.example.tripmanager.service;

import com.example.tripmanager.exception.ItemNotFound;
import com.example.tripmanager.mapper.TripPlanMapper;
import com.example.tripmanager.model.trip.Trip;
import com.example.tripmanager.model.trip.tripPlan.TripPlan;
import com.example.tripmanager.model.trip.tripPlan.TripPlanDto;
import com.example.tripmanager.repository.TripPlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class TripPlanServiceImpl implements TripPlanService{
    @Autowired
    private TripPlanRepository tripPlanRepository;
    @Autowired
    private TripService tripService;
    @Autowired
    protected AccountService accountService;

    protected TripPlanDto toDto(TripPlan tripPlan) {
        return TripPlanMapper.toDto(tripPlan, accountService);
    }

    protected TripPlan fromDto(TripPlanDto tripPlanDto) {
        return TripPlanMapper.fromDto(tripPlanDto);
    }

    @Override
    public Page<TripPlan> getAllTripPlansForTrip(Pageable pageable, String tripId) {
        Trip trip = tripService.getTripById(tripId)
                .orElseThrow(() -> new ItemNotFound("Trip not found - id=" + tripId));
        return this.tripPlanRepository.findAllByTrip(pageable, trip);
    }

    @Override
    public TripPlan insertTripPlan(TripPlanDto tripPlanDto, String tripId) {
        Trip trip = tripService.getTripById(tripId)
                .orElseThrow(() -> new ItemNotFound("Trip not found - id=" + tripId));
        TripPlan tripPlan = fromDto(tripPlanDto);

        return this.tripPlanRepository.insert(tripPlan);
    }

    @Override
    public void deleteTripPlan(String tripPlanId) {
        TripPlan tripPlanToDelete = this.tripPlanRepository.findById(tripPlanId)
                .orElseThrow(() -> new ItemNotFound("TripPlan not found - id=" + tripPlanId));
        this.tripPlanRepository.delete(tripPlanToDelete);
    }

    @Override
    public TripPlan patchTripPlan(TripPlan updatedTripPlan, String tripId) {
      return tripPlanRepository.save(updatedTripPlan);
    }
}
