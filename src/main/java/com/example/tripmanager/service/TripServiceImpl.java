package com.example.tripmanager.service;

import com.example.tripmanager.exception.ItemNotFound;
import com.example.tripmanager.model.Trip;
import com.example.tripmanager.model.TripDto;
import com.example.tripmanager.model.account.Account;
import com.example.tripmanager.repository.TripRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TripServiceImpl implements TripService {
    @Autowired
    private TripRepository tripRepository;
    @Autowired
    private AccountService accountService;

    @Override
    public Trip createTrip(TripDto tripDto) {
        return this.tripRepository.save(Trip.createFromDto(tripDto, accountService.getCurrentAccount()));
    }

    @Override
    public Optional<Trip> getTripById(String tripId) {
        return this.tripRepository.findById(tripId);
    }

    @Override
    public List<Trip> getTripsForCurrentAccount() {
        return this.tripRepository.findAllByAllowedAccountsContaining(List.of(accountService.getCurrentAccount()));
    }

    @Override
    public boolean hasAccountAccessToTrip(String tripId, Account account) {
        Trip trip = tripRepository.findById(tripId).orElseThrow(() -> new ItemNotFound("Trip not found"));
        return trip.isPublic() || trip.getAllowedAccounts().contains(account);
    }

}
