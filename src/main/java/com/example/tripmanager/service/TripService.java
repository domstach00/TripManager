package com.example.tripmanager.service;

import com.example.tripmanager.model.trip.Trip;
import com.example.tripmanager.model.trip.TripDto;
import com.example.tripmanager.model.account.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface TripService {
    Trip createTrip(TripDto tripDto);
    Optional<Trip> getTripById(String tripId);
    Page<Trip> getTripsForAccount(Pageable pageable, Account account);
    boolean isTripAdmin(Trip trip, Account account);
    void deleteTrip(String tripId, Account account);
    Trip archiveTrip(String tripId, Account account);
}
