package com.example.tripmanager.repository;

import com.example.tripmanager.model.Trip;
import com.example.tripmanager.model.account.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TripRepository extends MongoRepository<Trip, String> {

    Page<Trip> findAllByAllowedAccountsContaining(Pageable pageable, List<Account> allowedAccounts);

}
