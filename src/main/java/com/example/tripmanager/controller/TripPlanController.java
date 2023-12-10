package com.example.tripmanager.controller;

import com.example.tripmanager.mapper.TripPlanMapper;
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
    @Autowired
    private TripPlanMapper tripPlanMapper;

    @GetMapping()
    public List<TripPlan> getAllTripPlan(@PathVariable("tripId") String tripId) {
        return this.tripPlanService.getAllTripPlansForTrip(tripId);
    }

    @PostMapping()
    public TripPlanDto postTripPlan(@RequestBody TripPlan tripPlan) {
        return tripPlanMapper.toDto(this.tripPlanService.insertTripPlan(tripPlan));
    }

}
