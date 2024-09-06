package com.example.tripmanager.model;

import com.example.tripmanager.model.account.Account;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Document
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Trip {
    @Id
    private String id;
    private String name;
    private String description;
    private int dayLength;
    private LocalDate lastUpdateDate;
    private LocalTime lastUpdateTime;
    @DocumentReference
    private Account lastUpdateBy;

    @DocumentReference
    private Account createdBy;
    private LocalDate createdDate;
    private LocalTime createdTime;

    private boolean isPublic;
    private boolean isClosed;

    @DocumentReference
    @Indexed
    private List<Account> allowedAccounts;

    public static TripDto toDto(Trip trip) {
        TripDto tripDto = new TripDto();
        tripDto.setId(trip.getId());
        tripDto.setDescription(trip.getDescription());
        tripDto.setName(trip.getName());
        tripDto.setDayLength(trip.getDayLength());
        tripDto.setLastUpdateBy(Account.toDto(trip.getLastUpdateBy()));
        tripDto.setLastUpdateTime(trip.getLastUpdateTime());
        tripDto.setLastUpdateDate(trip.getLastUpdateDate());
        tripDto.setSummaryCost(0); // TODO : summary cost
        return tripDto;
    }

    public static List<TripDto> toDto(List<Trip> tripList) {
        return tripList.stream()
                .map(Trip::toDto)
                .collect(Collectors.toList());
    }

    public static Trip createFromDto(TripDto tripDto, Account currentAccount) {
        Trip.TripBuilder trip = Trip.builder();
        trip.id(tripDto.getId());
        trip.name(tripDto.getName());
        trip.description(tripDto.getDescription());
        trip.dayLength(tripDto.getDayLength());
        trip.lastUpdateDate(LocalDate.now());
        trip.lastUpdateTime(LocalTime.now());
        trip.lastUpdateBy(currentAccount);
        trip.createdBy(currentAccount);
        trip.createdDate(LocalDate.now());
        trip.createdTime(LocalTime.now());
        trip.allowedAccounts(List.of(currentAccount));
        return trip.build();
    }
}
