package com.example.tripmanager.model.trip;

import com.example.tripmanager.model.AbstractAuditableDto;
import lombok.Data;

@Data
public class TripDto extends AbstractAuditableDto {
    private String id;
    private String name;
    private String description;
    private int dayLength;
    private double summaryCost;
}
