package com.example.tripmanager.service;

import com.example.tripmanager.model.Trip;
import com.example.tripmanager.model.TripDto;
import com.example.tripmanager.model.account.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface TripService {
    Trip createTrip(TripDto tripDto);
    Optional<Trip> getTripById(String tripId);
    Page<Trip> getTripsForAccount(Pageable pageable, Account account);
    boolean hasAccountAccessToTrip(String tripId, Account account);
}
