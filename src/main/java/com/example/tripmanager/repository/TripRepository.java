package com.example.tripmanager.repository;

import com.example.tripmanager.model.account.Account;
import com.example.tripmanager.model.trip.Trip;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;


public interface TripRepository extends MongoRepository<Trip, String> {

    Page<Trip> findAllRelatedTrips(Pageable pageable, Account account);
    Optional<Trip> findTripById(String tripId, Account account);
}
