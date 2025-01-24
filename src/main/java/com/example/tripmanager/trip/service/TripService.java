package com.example.tripmanager.trip.service;

import com.example.tripmanager.trip.model.Trip;
import com.example.tripmanager.trip.model.TripDto;
import com.example.tripmanager.account.model.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface TripService {
    Trip createTrip(TripDto tripDto, Account currentAccount);
    Optional<Trip> getTripById(String tripId);
    Page<Trip> getTripsForAccount(Pageable pageable, Account account);
    boolean isTripAdmin(Trip trip, Account account);
    void deleteTrip(String tripId, Account account);
    Trip archiveTrip(String tripId, Account account);
    Trip duplicateTrip(String tripId, Account account);
    boolean removeAccountFromTrip(String tripId, Account account, Account currentAccount);
}
