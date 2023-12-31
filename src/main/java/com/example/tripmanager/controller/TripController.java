package com.example.tripmanager.controller;

import com.example.tripmanager.mapper.TripMapper;
import com.example.tripmanager.model.TripDto;
import com.example.tripmanager.service.TripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/trip")
public class TripController {
    @Autowired
    private TripService tripService;
    @Autowired
    private TripMapper tripMapper;

    @PostMapping
    public TripDto postTrip(Principal principal,
                            @RequestBody TripDto tripDto) {
        return tripMapper.toDto(this.tripService.createTrip(tripDto));
    }

    @GetMapping
    public List<TripDto> getTrips(Principal principal) {
        return tripMapper.toDto(this.tripService.getTripsForCurrentUser());
    }
}
