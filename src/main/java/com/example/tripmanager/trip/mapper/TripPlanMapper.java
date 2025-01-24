package com.example.tripmanager.trip.mapper;

import com.example.tripmanager.shared.mapper.AuditableMapper;
import com.example.tripmanager.trip.model.tripPlan.TripPlan;
import com.example.tripmanager.trip.model.tripPlan.TripPlanDto;
import com.example.tripmanager.account.service.AccountService;
import org.springframework.data.domain.Page;

public class TripPlanMapper {
    public static TripPlanDto toDto(TripPlan tripPlan, AccountService accountService) {
        TripPlanDto tripPlanDto = new TripPlanDto();
        tripPlanDto = AuditableMapper.toDto(tripPlan, tripPlanDto, accountService);

        tripPlanDto.setTripId(tripPlan.getTripId());
        tripPlanDto.setCost(tripPlan.getCost());
        tripPlanDto.setDesc(tripPlan.getDesc());
        tripPlanDto.setLink(tripPlan.getLink());
        tripPlanDto.setName(tripPlan.getName());
        tripPlanDto.setDay(tripPlan.getDay());
        tripPlanDto.setMapElement(tripPlan.getMapElement());

        return tripPlanDto;
    }

    public static Page<TripPlanDto> toDto(Page<TripPlan> tripPlanList, AccountService accountService) {
        return tripPlanList.map(tripPlan -> toDto(tripPlan, accountService));
    }

    public static TripPlan fromDto(TripPlanDto tripPlanDto) {
        TripPlan tripPlan = new TripPlan();
        tripPlan.setId(tripPlanDto.getId());
        tripPlan.setName(tripPlanDto.getName());
        tripPlan.setDay(tripPlanDto.getDay());
        tripPlan.setCost(tripPlanDto.getCost());
        tripPlan.setDesc(tripPlanDto.getDesc());
        tripPlan.setLink(tripPlanDto.getLink());
        tripPlan.setMapElement(tripPlanDto.getMapElement());
        tripPlan.setTripId(tripPlanDto.getTripId());
        return tripPlan;
    }
}
