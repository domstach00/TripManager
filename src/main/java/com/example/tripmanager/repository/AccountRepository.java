package com.example.tripmanager.repository;

import com.example.tripmanager.model.account.Account;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface AccountRepository extends MongoRepository<Account, String> {
    Optional<Account> findByUsername(String username);
    Optional<Account> findByEmail(String email);

    Boolean existsByUsernameOrEmail(String username, String email);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
}
