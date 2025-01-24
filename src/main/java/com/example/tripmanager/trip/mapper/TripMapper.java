package com.example.tripmanager.trip.mapper;

import com.example.tripmanager.account.mapper.AccountMapper;
import com.example.tripmanager.account.model.Account;
import com.example.tripmanager.shared.mapper.AuditableMapper;
import com.example.tripmanager.shared.mapper.MemberMapper;
import com.example.tripmanager.trip.model.Trip;
import com.example.tripmanager.trip.model.TripDto;
import com.example.tripmanager.account.service.AccountService;
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
        tripDto.setOwner(AccountMapper.toDto(trip.getOwner()));
        tripDto.setMembers(MemberMapper.toDto(trip.getMembers()));
        tripDto.setPublic(trip.isPublic());
        tripDto.setClosed(trip.isClosed());
        tripDto.setArchived(trip.isArchived());
        return tripDto;
    }

    public static Page<TripDto> toDto(Page<Trip> tripDtos, AccountService accountService) {
        return tripDtos.map(trip -> toDto(trip, accountService));
    }

    public static Trip createFromDto(TripDto tripDto, Account account, final AccountService accountService) {
        Trip trip = new Trip();
        trip.setId(tripDto.getId());
        trip.setName(tripDto.getName());
        trip.setDescription(tripDto.getDescription());
        trip.setMembers(MemberMapper.createFromDto(tripDto.getMembers(), accountService));
        trip.setOwner(account);
        trip.setPublic(tripDto.isPublic());
        trip.setClosed(tripDto.isClosed());
        trip.setArchived(tripDto.isArchived());
        return trip;
    }
}
