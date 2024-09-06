package com.example.tripmanager.controller;

import com.example.tripmanager.model.TripPlan;
import com.example.tripmanager.model.TripPlanDto;
import com.example.tripmanager.service.TripPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(TripPlanController.tripPlanControllerUrl)
public class TripPlanController {
    protected final static String tripPlanControllerUrl = "/api/trip/{tripId}/plan";

    @Autowired
    private TripPlanService tripPlanService;

    @GetMapping()
    public List<TripPlanDto> getAllTripPlan(@PathVariable("tripId") String tripId) {
        return TripPlan.toDto(
                tripPlanService.getAllTripPlansForTrip(tripId)
        );
    }

    @PostMapping()
    public TripPlanDto postTripPlan(@PathVariable String tripId,
                                    @RequestBody TripPlanDto tripPlanDto) {
        return TripPlan.toDto(
                this.tripPlanService.insertTripPlan(tripPlanDto, tripId)
        );
    }

    @PatchMapping()
    public TripPlanDto patchTripPlan(@PathVariable String tripId,
                                     @RequestBody TripPlanDto tripPlanDto) {
        return TripPlan.toDto(
                tripPlanService.patchTripPlan(tripPlanDto)
        );
    }

    @DeleteMapping("/{tripPlanId}")
    public void deleteTripPlan(@PathVariable String tripId, @PathVariable String tripPlanId) {
        this.tripPlanService.deleteTripPlan(tripPlanId);
    }

}
