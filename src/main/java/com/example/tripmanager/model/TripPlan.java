package com.example.tripmanager.model;

import com.example.tripmanager.model.account.Account;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Document(collection = "TripPlans")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class TripPlan {
    @Id
    private String id;

    private String displayName;
    private int day;
    private double cost;
    private String desc;
    private String link;
    private Account createdBy;
    private LocalDateTime createdDateTime;

    @DocumentReference
    private Trip trip;


    private GoogleMapPin mapElement;

    public static TripPlanDto toDto(TripPlan tripPlan) {
        TripPlanDto tripPlanDto = new TripPlanDto();
        tripPlanDto.setId(tripPlan.getId());
        tripPlanDto.setTripId(tripPlan.getTrip().getId());
        tripPlanDto.setCost(tripPlan.getCost());
        tripPlanDto.setDesc(tripPlan.getDesc());
        tripPlanDto.setLink(tripPlan.getLink());
        tripPlanDto.setDisplayName(tripPlan.getDisplayName());
        tripPlanDto.setDay(tripPlan.getDay());
        tripPlanDto.setMapElement(tripPlan.getMapElement());
        return tripPlanDto;
    }

    public static List<TripPlanDto> toDto(List<TripPlan> tripPlanList) {
        return tripPlanList.stream()
                .map(TripPlan::toDto)
                .collect(Collectors.toList());
    }

    public static TripPlan fromDto(TripPlanDto tripPlanDto, Trip trip) {
        TripPlan.TripPlanBuilder tripPlan = TripPlan.builder();
        tripPlan.id(tripPlanDto.getId());
        tripPlan.displayName(tripPlanDto.getDisplayName());
        tripPlan.day(tripPlanDto.getDay());
        tripPlan.cost(tripPlanDto.getCost());
        tripPlan.desc(tripPlanDto.getDesc());
        tripPlan.link(tripPlanDto.getLink());
        tripPlan.mapElement(tripPlanDto.getMapElement());
        tripPlan.trip(trip);
        return tripPlan.build();
    }

}
