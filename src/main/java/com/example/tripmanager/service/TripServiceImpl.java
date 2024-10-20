package com.example.tripmanager.service;

import com.example.tripmanager.exception.ItemNotFound;
import com.example.tripmanager.mapper.TripMapper;
import com.example.tripmanager.model.common.Member;
import com.example.tripmanager.model.trip.Trip;
import com.example.tripmanager.model.trip.TripDto;
import com.example.tripmanager.model.account.Account;
import com.example.tripmanager.repository.TripRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class TripServiceImpl implements TripService {
    @Autowired
    private TripRepository tripRepository;
    @Autowired
    private AccountService accountService;

    protected Trip createFromDto(TripDto tripDto, Account currentAccount) {
        return TripMapper.createFromDto(tripDto, currentAccount, accountService);
    }

    @Override
    public Trip createTrip(TripDto tripDto, Account currentAccount) {
        Trip tripToSave = createFromDto(tripDto, currentAccount);
        return this.tripRepository.save(tripToSave);
    }

    @Override
    public Optional<Trip> getTripById(String tripId) {
        return this.tripRepository.findById(tripId);
    }

    @Override
    public Page<Trip> getTripsForAccount(Pageable pageable, Account account) {
        return this.tripRepository.findAllRelatedTrips(pageable, account);
    }

    @Override
    public boolean isTripAdmin(Trip trip, Account account) {
        return Objects.equals(trip.getOwner().getId(), account.getId());
    }

    @Override
    public void deleteTrip(String tripId, Account account) {
        Trip tripToDelete = this.tripRepository.findTripByIdWhereAccountIsOwnerOrAdmin(tripId, account)
                .orElseThrow(() -> new ItemNotFound("Trip was not found or you do not have enough permissions"));
        tripToDelete.setDeleted(true);
        this.tripRepository.save(tripToDelete);
    }

    @Override
    public Trip archiveTrip(String tripId, Account account) {
        Trip tripToArchive = this.tripRepository.findTripByIdWhereAccountIsOwnerOrAdmin(tripId, account)
                .orElseThrow(() -> new ItemNotFound("Trip was not found or you do not have enough permissions"));
        tripToArchive.setArchived(true);
        return this.tripRepository.save(tripToArchive);
    }

    @Override
    public Trip duplicateTrip(String tripId, Account account) {
        Trip tripToDuplicate = this.tripRepository.findTripById(tripId, account)
                .orElseThrow(() -> new ItemNotFound("Trip was not found or you do not have enough permissions"));
        Trip duplicatedTrip = new Trip();
        duplicatedTrip.deepCopyFrom(tripToDuplicate);
        duplicatedTrip.setName(duplicatedTrip.getName() + " - duplicated");
        duplicatedTrip.setId(null);
        duplicatedTrip.setCreatedTime(null);
        duplicatedTrip.setCreatedBy(null);
        duplicatedTrip.setLastModifiedTime(null);
        duplicatedTrip.setLastModifiedBy(null);
        duplicatedTrip.setOwner(account);
        duplicatedTrip.setMembers(new ArrayList<>());
        return this.tripRepository.save(duplicatedTrip);
    }

    @Override
    @Transactional
    public boolean removeAccountFromTrip(final String tripId, final Account account, final Account currentAccount) {
        Trip trip = this.tripRepository.findTripByIdWhereAccountIsOwnerOrAdmin(tripId, currentAccount)
                .orElseThrow(() -> new ItemNotFound("Trip was not found or you do not have enough permissions"));
        List<Member> accountAsMember = trip.getMembers().stream()
                .filter(member -> Objects.equals(member.getAccountId(), account.getId()))
                .toList();
        if (!accountAsMember.isEmpty() && trip.getMembers().removeAll(accountAsMember)) {
            this.tripRepository.save(trip);
            return true;
        }
        return false;
    }
}
