package com.example.tripmanager.repository;

import com.example.tripmanager.model.trip.Trip;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface TripRepository extends MongoRepository<Trip, String> {

    Page<Trip> findAllByOwnerId(Pageable pageable, String ownerId);

}
