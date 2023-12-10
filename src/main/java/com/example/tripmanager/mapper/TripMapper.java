package com.example.tripmanager.mapper;

import com.example.tripmanager.model.Trip;
import com.example.tripmanager.model.TripDto;
import com.example.tripmanager.service.UserService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class TripMapper {
    @Autowired
    protected UserService userService;

    public abstract TripDto toDto(Trip trip);

    @Mapping(target = "lastUpdateDate", expression = "java(getLocalDate())")
    @Mapping(target = "lastUpdateTime", expression = "java(getLocalTime())")
    @Mapping(target = "lastUpdateBy", expression = "java(userService.getCurrentUser())")
    @Mapping(target = "createdBy", expression = "java(userService.getCurrentUser())")
    @Mapping(target = "createdDate", expression = "java(getLocalDate())")
    @Mapping(target = "createdTime", expression = "java(getLocalTime())")
    @Mapping(target = "allowedUsers", expression = "java(List.of(userService.getCurrentUser()))")
    public abstract Trip createFromDto(TripDto tripDto);

    public abstract List<TripDto> toDto(List<Trip> trips);

    protected LocalDate getLocalDate() {
        return LocalDate.now();
    }

    protected LocalTime getLocalTime() {
        return LocalTime.now();
    }
}
