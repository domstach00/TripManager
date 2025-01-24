package com.example.tripmanager.trip.repository;

import com.example.tripmanager.account.model.Account;
import com.example.tripmanager.trip.model.Trip;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;


public interface TripRepository extends MongoRepository<Trip, String> {

    Page<Trip> findAllRelatedTrips(Pageable pageable, Account account);
    Optional<Trip> findTripById(String tripId, Account account);
    Optional<Trip> findTripByIdWhereAccountIsOwnerOrAdmin(String tripId, Account account);
}
