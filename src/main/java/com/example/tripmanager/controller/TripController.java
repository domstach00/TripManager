package com.example.tripmanager.controller;

import com.example.tripmanager.controller.support.PageParams;
import com.example.tripmanager.model.trip.Trip;
import com.example.tripmanager.model.trip.TripDto;
import com.example.tripmanager.service.AccountService;
import com.example.tripmanager.service.TripService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/trip")
public class TripController extends AbstractController {
    @Autowired
    private TripService tripService;
    @Autowired
    private AccountService accountService;

    @PostMapping
    public TripDto postTrip(Principal principal,
                            @RequestBody TripDto tripDto) {
        return Trip.toDto(this.tripService.createTrip(tripDto));
    }

    @GetMapping
    public Page<TripDto> getTrips(
            Principal principal,
            @ParameterObject PageParams pageParams
    ) {
        return Trip.toDto(this.tripService.getTripsForAccount(pageParams.asPageable(), accountService.getCurrentAccount()));
    }
}
