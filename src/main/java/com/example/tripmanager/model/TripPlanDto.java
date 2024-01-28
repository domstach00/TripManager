package com.example.tripmanager.model;

import com.example.tripmanager.exception.PatchValidationException;
import lombok.Data;

import java.util.Objects;

@Data
public class TripPlanDto {
    private String id;

    private String displayName;
    private int day;
    private double cost;
    private String desc;
    private String link;
    private String tripId;

    private GoogleMapPin mapElement;

    public void checkPatchValidation(TripPlanDto originalTripPlan) throws PatchValidationException {
        if (!Objects.equals(this.id, originalTripPlan.getId())
                || !Objects.equals(this.tripId, originalTripPlan.tripId)) {
            throw new PatchValidationException("TripPlanDto has not passed patch validation");
        }
    }
}
