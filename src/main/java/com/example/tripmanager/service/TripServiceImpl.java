package com.example.tripmanager.service;

import com.example.tripmanager.exception.ItemNotFound;
import com.example.tripmanager.mapper.TripMapper;
import com.example.tripmanager.model.trip.Trip;
import com.example.tripmanager.model.trip.TripDto;
import com.example.tripmanager.model.account.Account;
import com.example.tripmanager.repository.TripRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TripServiceImpl implements TripService {
    @Autowired
    private TripRepository tripRepository;
    @Autowired
    private AccountService accountService;

    protected Trip createFromDto(TripDto tripDto) {
        return TripMapper.createFromDto(tripDto, accountService.getCurrentAccount());
    }

    @Override
    public Trip createTrip(TripDto tripDto) {
        return this.tripRepository.save(createFromDto(tripDto));
    }

    @Override
    public Optional<Trip> getTripById(String tripId) {
        return this.tripRepository.findById(tripId);
    }

    @Override
    public Page<Trip> getTripsForAccount(Pageable pageable, Account account) {
        return this.tripRepository.findAllByOwnerId(pageable, account.getId());
    }

    @Override
    public boolean hasAccountAccessToTrip(String tripId, Account account) {
        Trip trip = tripRepository.findById(tripId).orElseThrow(() -> new ItemNotFound("Trip not found"));
        return trip.isPublic() || trip.getMembers().contains(account.getId());
    }

}
