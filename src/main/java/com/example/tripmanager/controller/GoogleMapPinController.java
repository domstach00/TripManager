package com.example.tripmanager.controller;

import com.example.tripmanager.model.GoogleMapPin;
import com.example.tripmanager.service.GoogleMapPinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(GoogleMapPinController.tripPlanControllerUrl)
public class GoogleMapPinController {
    protected final static String tripPlanControllerUrl = "/api/googleMapPin";

    @Autowired
    private GoogleMapPinService googleMapPinService;

    @GetMapping
    public List<GoogleMapPin> getAllGoogleMapPin() {
        return this.googleMapPinService.findAll();
    }
}
