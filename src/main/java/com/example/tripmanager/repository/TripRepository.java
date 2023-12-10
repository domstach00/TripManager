package com.example.tripmanager.repository;

import com.example.tripmanager.model.Trip;
import com.example.tripmanager.model.user.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TripRepository extends MongoRepository<Trip, String> {

    List<Trip> findAllByAllowedUsersContaining(List<User> allowedUsers);

}
