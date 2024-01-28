package com.example.tripmanager.mapper;

import com.example.tripmanager.model.Trip;
import com.example.tripmanager.model.TripPlan;
import com.example.tripmanager.model.TripPlanDto;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class TripPlanMapper {

    @Mapping(target = "tripId", expression = "java(tripPlan.getTrip().getId())")
    public abstract TripPlanDto toDto(TripPlan tripPlan);

    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdDateTime", ignore = true)
    @Mapping(target = "trip", expression = "java(trip)")
    public abstract TripPlan fromDto(TripPlanDto tripPlanDto, @Context Trip trip);

    public abstract List<TripPlanDto> toDto(List<TripPlan> tripPlans);
}
