package com.example.tripmanager.controller;

import com.example.tripmanager.controller.support.PageParams;
import com.example.tripmanager.mapper.TripMapper;
import com.example.tripmanager.model.account.Account;
import com.example.tripmanager.model.trip.Trip;
import com.example.tripmanager.model.trip.TripDto;
import com.example.tripmanager.service.AccountService;
import com.example.tripmanager.service.TripService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping(TripController.TRIP_PLAN_CONTROLLER_URL)
public class TripController extends AbstractController {
    public static final String TRIP_PLAN_CONTROLLER_URL = "/api/trip";

    @Autowired
    private TripService tripService;
    @Autowired
    private AccountService accountService;

    protected TripDto toDto(Trip trip) {
        return TripMapper.toDto(trip, accountService);
    }

    protected Page<TripDto> toDto(Page<Trip> trip) {
        return TripMapper.toDto(trip, accountService);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TripDto postTrip(Principal principal,
                            @RequestBody TripDto tripDto) {
        return toDto(this.tripService.createTrip(tripDto));
    }

    @GetMapping
    public Page<TripDto> getTrips(
            Principal principal,
            @ParameterObject PageParams pageParams
    ) {
        return toDto(this.tripService.getTripsForAccount(pageParams.asPageable(), accountService.getCurrentAccount()));
    }

    @DeleteMapping("/{tripId}")
    public void deleteTrip(
            @PathVariable String tripId
    ) {
        final Account currentAccount = this.accountService.getCurrentAccount();
        this.tripService.deleteTrip(tripId, currentAccount);
    }

    @PatchMapping("/{tripId}/archive")
    public TripDto archiveTrip(
            @PathVariable String tripId
    ) {
       final Account currentAccount = this.accountService.getCurrentAccount();
       return toDto(this.tripService.archiveTrip(tripId, currentAccount));
    }

    @PostMapping("/{tripId}/duplicate")
    public TripDto duplicateTrip(
            @PathVariable String tripId
    ) {
        final Account currentAccount = this.accountService.getCurrentAccount();
        return toDto(this.tripService.duplicateTrip(tripId, currentAccount));
    }
}
