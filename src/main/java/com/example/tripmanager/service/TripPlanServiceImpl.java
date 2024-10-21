package com.example.tripmanager.service;

import com.example.tripmanager.exception.ItemNotFound;
import com.example.tripmanager.mapper.TripPlanMapper;
import com.example.tripmanager.model.account.Account;
import com.example.tripmanager.model.googleMapPin.GoogleMapPin;
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

    protected TripPlan fromDto(TripPlanDto tripPlanDto) {
        return TripPlanMapper.fromDto(tripPlanDto);
    }

    @Override
    public Page<TripPlan> getAllTripPlansForTrip(Pageable pageable, String tripId, Account currentAccount) {
        Trip trip = tripService.getTripById(tripId)
                .orElseThrow(() -> new ItemNotFound("Trip not found - id=" + tripId));
        return this.tripPlanRepository.findAllByTripId(pageable, trip.getId(), currentAccount.getId());
    }

    @Override
    public TripPlan insertTripPlan(TripPlanDto tripPlanDto, String tripId) {
        Trip trip = tripService.getTripById(tripId)
                .orElseThrow(() -> new ItemNotFound("Trip not found - id=" + tripId));
        TripPlan tripPlan = fromDto(tripPlanDto);

        return this.tripPlanRepository.save(tripPlan);
    }

    @Override
    public void deleteTripPlan(String tripPlanId, Account currentAccount) {
        TripPlan tripPlanToDelete = this.tripPlanRepository.findByIdWhereUserIsAdmin(tripPlanId, currentAccount.getId())
                .orElseThrow(() -> new ItemNotFound("TripPlan not found - id=" + tripPlanId));
        this.tripPlanRepository.delete(tripPlanToDelete);
    }

    @Override
    public TripPlan patchTripPlan(TripPlan updatedTripPlan, String tripId) {
      return tripPlanRepository.save(updatedTripPlan);
    }

    @Override
    public Page<GoogleMapPin> getAllGoogleMapPinsForTrip(Pageable pageable, String tripId, Account currentAccount) {
        return tripPlanRepository.findAllGoogleMapPinsForTripId(pageable, tripId, currentAccount.getId());
    }
}
