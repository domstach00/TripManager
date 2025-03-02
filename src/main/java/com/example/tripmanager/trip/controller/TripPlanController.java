package com.example.tripmanager.trip.controller;

import com.example.tripmanager.shared.controller.AbstractController;
import com.example.tripmanager.shared.controller.support.PageParams;
import com.example.tripmanager.trip.mapper.TripPlanMapper;
import com.example.tripmanager.account.model.Account;
import com.example.tripmanager.trip.model.tripPlan.TripPlan;
import com.example.tripmanager.trip.model.tripPlan.TripPlanDto;
import com.example.tripmanager.account.service.AccountService;
import com.example.tripmanager.trip.service.TripPlanService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping(TripPlanController.tripPlanControllerUrl)
public class TripPlanController extends AbstractController {
    protected final static String tripPlanControllerUrl = "/api/trip/{tripId}/plan";

    @Autowired
    private TripPlanService tripPlanService;
    @Autowired
    private AccountService accountService;

    protected TripPlanDto toDto(TripPlan tripPlan) {
        return TripPlanMapper.toDto(tripPlan, accountService);
    }

    protected Page<TripPlanDto> toDto(Page<TripPlan> tripPlan) {
        return TripPlanMapper.toDto(tripPlan, accountService);
    }

    protected TripPlan createFromDto(TripPlanDto tripPlanDto) {
        return TripPlanMapper.fromDto(tripPlanDto);
    }

    @GetMapping()
    public Page<TripPlanDto> getAllTripPlan(
            Principal principal,
            @ParameterObject PageParams pageParams,
            @PathVariable("tripId") String tripId
    ) {
        Account currentAccount = getCurrentAccount(principal);
        Page<TripPlan> tripPlans = tripPlanService.getAllTripPlansForTrip(pageParams.asPageable(), tripId, currentAccount);
        return toDto(tripPlans);
    }

    @GetMapping("/map")
    public Page<TripPlanDto> getAllTripPlansWithMapElement(
            Principal principal,
            @ParameterObject PageParams pageParams,
            @PathVariable("tripId") String tripId
    ) {
        Account currentAccount = getCurrentAccount(principal);
        Page<TripPlan> tripPlans = tripPlanService.getAllTripPlansWithMapElement(pageParams.asPageable(), tripId, currentAccount);
        return toDto(tripPlans);
    }

    @PostMapping()
    public TripPlanDto postTripPlan(@PathVariable String tripId,
                                    @RequestBody TripPlanDto tripPlanDto) {
        throwErrorOnValidateIdsFromUrlAndBody(tripId, tripPlanDto.getTripId());
        return toDto(
                this.tripPlanService.insertTripPlan(tripPlanDto, tripId)
        );
    }

    @PatchMapping()
    public TripPlanDto patchTripPlan(@PathVariable String tripId,
                                     @RequestBody TripPlanDto tripPlanDto) {
        throwErrorOnValidateIdsFromUrlAndBody(tripId, tripPlanDto.getTripId());
        TripPlan updatedTripPlan = createFromDto(tripPlanDto);
        return toDto(tripPlanService.patchTripPlan(updatedTripPlan, tripId));
    }

    @DeleteMapping("/{tripPlanId}")
    public void deleteTripPlan(
            Principal principal,
            @PathVariable String tripId,
            @PathVariable String tripPlanId) {
        Account currentAccount = getCurrentAccount(principal);
        this.tripPlanService.deleteTripPlan(tripPlanId, currentAccount);
    }

}
