package com.example.tripmanager.model.trip;

import com.example.tripmanager.model.AbstractAuditableDto;

public class TripDto extends AbstractAuditableDto {
    private String name;
    private String description;
    private int dayLength;
    private double summaryCost;

    public TripDto() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDayLength() {
        return dayLength;
    }

    public void setDayLength(int dayLength) {
        this.dayLength = dayLength;
    }

    public double getSummaryCost() {
        return summaryCost;
    }

    public void setSummaryCost(double summaryCost) {
        this.summaryCost = summaryCost;
    }
}
