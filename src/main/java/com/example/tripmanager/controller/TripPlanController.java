package com.example.tripmanager.controller;

import com.example.tripmanager.model.TripPlan;
import com.example.tripmanager.service.TripPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(TripPlanController.tripPlanControllerUrl)
public class TripPlanController {
    protected final static String tripPlanControllerUrl = "/api/trip/plan";

    @Autowired
    private TripPlanService tripPlanService;

    @GetMapping()
    public List<TripPlan> getAllTripPlan() {
        return this.tripPlanService.findAll();
    }

    @PostMapping()
    public TripPlan postTripPlan(@RequestBody TripPlan tripPlan) {
        return this.tripPlanService.insertTripPlan(tripPlan);
    }

}
