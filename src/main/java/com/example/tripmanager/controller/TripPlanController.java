package com.example.tripmanager.controller;

import com.example.tripmanager.controller.support.PageParams;
import com.example.tripmanager.mapper.TripPlanMapper;
import com.example.tripmanager.model.trip.tripPlan.TripPlan;
import com.example.tripmanager.model.trip.tripPlan.TripPlanDto;
import com.example.tripmanager.service.AccountService;
import com.example.tripmanager.service.TripPlanService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(TripPlanController.tripPlanControllerUrl)
public class TripPlanController {
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

    @GetMapping()
    public Page<TripPlanDto> getAllTripPlan(
            @ParameterObject PageParams pageParams,
            @PathVariable("tripId") String tripId
    ) {
        return toDto(
                tripPlanService.getAllTripPlansForTrip(pageParams.asPageable(), tripId)
        );
    }

    @PostMapping()
    public TripPlanDto postTripPlan(@PathVariable String tripId,
                                    @RequestBody TripPlanDto tripPlanDto) {
        return toDto(
                this.tripPlanService.insertTripPlan(tripPlanDto, tripId)
        );
    }

    @PatchMapping()
    public TripPlanDto patchTripPlan(@PathVariable String tripId,
                                     @RequestBody TripPlanDto tripPlanDto) {
        return toDto(
                tripPlanService.patchTripPlan(tripPlanDto)
        );
    }

    @DeleteMapping("/{tripPlanId}")
    public void deleteTripPlan(@PathVariable String tripId, @PathVariable String tripPlanId) {
        this.tripPlanService.deleteTripPlan(tripPlanId);
    }

}
