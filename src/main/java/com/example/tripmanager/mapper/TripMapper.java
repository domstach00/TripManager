package com.example.tripmanager.mapper;

import com.example.tripmanager.model.account.Account;
import com.example.tripmanager.model.trip.Trip;
import com.example.tripmanager.model.trip.TripDto;
import com.example.tripmanager.service.AccountService;
import org.springframework.data.domain.Page;

public class TripMapper {

    public static TripDto toDto(Trip trip, AccountService accountService) {
        if (trip == null) {
            return null;
        }
        TripDto tripDto = new TripDto();
        tripDto = AuditableMapper.toDto(trip, tripDto, accountService);

        tripDto.setName(trip.getName());
        tripDto.setDescription(trip.getDescription());
        tripDto.setDayLength(trip.getDayLength());
        tripDto.setSummaryCost(tripDto.getSummaryCost());
        return tripDto;
    }

    public static Page<TripDto> toDto(Page<Trip> tripDtos, AccountService accountService) {
        return tripDtos.map(trip -> toDto(trip, accountService));
    }

    public static Trip createFromDto(TripDto tripDto, Account account) {
        Trip trip = new Trip();
        trip.setId(tripDto.getId());

        trip.setName(tripDto.getName());
        trip.setDescription(tripDto.getDescription());
        trip.setDayLength(tripDto.getDayLength());
        trip.addAllowedAccount(account);
        return trip;
    }
}
