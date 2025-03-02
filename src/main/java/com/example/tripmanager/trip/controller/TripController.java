package com.example.tripmanager.trip.controller;

import com.example.tripmanager.shared.controller.AbstractController;
import com.example.tripmanager.shared.controller.support.PageParams;
import com.example.tripmanager.trip.mapper.TripMapper;
import com.example.tripmanager.account.model.Account;
import com.example.tripmanager.trip.model.Trip;
import com.example.tripmanager.trip.model.TripDto;
import com.example.tripmanager.account.service.AccountService;
import com.example.tripmanager.trip.service.TripService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping(TripController.TRIP_PLAN_CONTROLLER_URL)
public class TripController extends AbstractController {
    private static final Logger log = LoggerFactory.getLogger(TripController.class);
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
        Account currentAccount = getCurrentAccount(principal);
        log.info("Attempting to create Trip by accountId={}", currentAccount.getId());
        return toDto(this.tripService.createTrip(tripDto, currentAccount));
    }

    @GetMapping
    public Page<TripDto> getTrips(
            Principal principal,
            @ParameterObject PageParams pageParams
    ) {
        Account currentAccount = getCurrentAccount(principal);
        Page<Trip> trips = this.tripService.getTripsForAccount(pageParams.asPageable(), currentAccount);
        return toDto(trips);
    }

    @DeleteMapping("/{tripId}")
    public void deleteTrip(
            Principal principal,
            @PathVariable String tripId
    ) {
        Account currentAccount = getCurrentAccount(principal);
        log.info("Attempting to delete Trip {} by accountId={}", tripId, currentAccount.getId());
        this.tripService.deleteTrip(tripId, currentAccount);
    }

    @PatchMapping("/{tripId}/archive")
    public TripDto archiveTrip(
            Principal principal,
            @PathVariable String tripId
    ) {
        Account currentAccount = getCurrentAccount(principal);
        log.info("Attempting to archive Trip {} by accountId={}", tripId, currentAccount.getId());
        Trip archivedTrip = this.tripService.archiveTrip(tripId, currentAccount);
        return toDto(archivedTrip);
    }

    @PostMapping("/{tripId}/duplicate")
    public TripDto duplicateTrip(
            Principal principal,
            @PathVariable String tripId
    ) {
        Account currentAccount = getCurrentAccount(principal);
        log.info("Attempting to duplicate Trip {} by accountId={}", tripId, currentAccount.getId());
        Trip duplicatedTrip = this.tripService.duplicateTrip(tripId, currentAccount);
        return toDto(duplicatedTrip);
    }

    @DeleteMapping("/{tripId}/leave")
    public void leaveTripAsMember(
            Principal principal,
            @PathVariable String tripId
    ) {
        Account currentAccount = getCurrentAccount(principal);
        log.info("Attempting to leave (as member) Trip {} by accountId={}", tripId, currentAccount.getId());
        this.tripService.removeAccountFromTrip(tripId, currentAccount, currentAccount);
    }
}
