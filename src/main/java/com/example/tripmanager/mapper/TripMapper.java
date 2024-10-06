package com.example.tripmanager.mapper;

import com.example.tripmanager.exception.AccountNotFoundException;
import com.example.tripmanager.model.account.Account;
import com.example.tripmanager.model.trip.Trip;
import com.example.tripmanager.model.trip.TripDto;
import com.example.tripmanager.service.AccountService;
import org.springframework.data.domain.Page;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

public class TripMapper {

    public static TripDto toDto(Trip trip, AccountService accountService) {
        if (trip == null) {
            return null;
        }
        TripDto tripDto = new TripDto();
        tripDto.setId(trip.getId());
        tripDto.setLastModifiedTime(trip.getLastModifiedTime());
        tripDto.setCreatedTime(trip.getCreatedTime());
        if (accountService != null) {
            final Account createdBy = accountService.getAccountById(trip.getCreatedBy()).orElseThrow(AccountNotFoundException::new);
            tripDto.setCreatedBy(AccountMapper.toDto(createdBy));

            final Account lastModifiedBy = Objects.equals(createdBy.getId(), trip.getLastModifiedBy())
                    ? createdBy
                    : accountService.getAccountById(trip.getLastModifiedBy()).orElseThrow(AccountNotFoundException::new);
            tripDto.setLastModifiedBy(AccountMapper.toDto(lastModifiedBy));
        }

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
        final Instant now = Instant.now();
        trip.setLastModifiedTime(now);
        trip.setLastModifiedBy(account.getId());
        trip.setCreatedTime(now);
        trip.setCreatedBy(account.getId());

        trip.setName(tripDto.getName());
        trip.setDescription(tripDto.getDescription());
        trip.setDayLength(tripDto.getDayLength());
        trip.addAllowedAccount(account);
        return trip;
    }
}
