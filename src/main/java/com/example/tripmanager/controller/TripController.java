package com.example.tripmanager.controller;

import com.example.tripmanager.model.Trip;
import com.example.tripmanager.model.TripDto;
import com.example.tripmanager.service.AccountService;
import com.example.tripmanager.service.TripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
            @RequestParam(name = "pageSize", required = false, defaultValue = "5") int pageSize,
            @RequestParam(name = "pageNumber", required = false, defaultValue = "0") int pageNumber
    ) {
        Pageable pageable = buildPageable(pageSize, pageNumber, "ASC");
        return Trip.toDto(this.tripService.getTripsForAccount(pageable, accountService.getCurrentAccount()));
    }
}
