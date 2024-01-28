package com.example.tripmanager.service;

import com.example.tripmanager.exception.ItemNotFound;
import com.example.tripmanager.mapper.TripMapper;
import com.example.tripmanager.model.Trip;
import com.example.tripmanager.model.TripDto;
import com.example.tripmanager.model.user.User;
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
    private TripMapper tripMapper;
    @Autowired
    private UserService userService;

    @Override
    public Trip createTrip(TripDto tripDto) {
        return this.tripRepository.save(tripMapper.createFromDto(tripDto));
    }

    @Override
    public Optional<Trip> getTripById(String tripId) {
        return this.tripRepository.findById(tripId);
    }

    @Override
    public List<Trip> getTripsForCurrentUser() {
        return this.tripRepository.findAllByAllowedUsersContaining(List.of(userService.getCurrentUser()));
    }

    @Override
    public boolean hasUserAccessToTrip(String tripId, User user) {
        Trip trip = tripRepository.findById(tripId).orElseThrow(() -> new ItemNotFound("Trip not found"));
        return trip.isPublic() || trip.getAllowedUsers().contains(user);
    }

}
