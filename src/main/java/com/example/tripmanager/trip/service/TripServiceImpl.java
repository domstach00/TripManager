package com.example.tripmanager.trip.service;

import com.example.tripmanager.account.service.AccountService;
import com.example.tripmanager.shared.exception.ItemNotFound;
import com.example.tripmanager.trip.mapper.TripMapper;
import com.example.tripmanager.shared.model.common.Member;
import com.example.tripmanager.trip.model.Trip;
import com.example.tripmanager.trip.model.TripDto;
import com.example.tripmanager.account.model.Account;
import com.example.tripmanager.trip.repository.TripRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger log = LoggerFactory.getLogger(TripServiceImpl.class);
    @Autowired
    private TripRepository tripRepository;
    @Autowired
    private AccountService accountService;

    protected Trip createFromDto(TripDto tripDto, Account currentAccount) {
        log.debug("Creating trip from DTO for accountId={}", currentAccount.getId());
        return TripMapper.createFromDto(tripDto, currentAccount, accountService);
    }

    @Override
    public Trip createTrip(TripDto tripDto, Account currentAccount) {
        log.debug("Creating new trip for accountId={}", currentAccount.getId());
        Trip tripToSave = createFromDto(tripDto, currentAccount);
        Trip savedTrip = this.tripRepository.save(tripToSave);
        log.debug("Trip created with ID: {} by accountId={}", savedTrip.getId(), currentAccount.getId());
        return savedTrip;
    }

    @Override
    public Optional<Trip> getTripById(String tripId) {
        log.debug("Fetching trip with Id={}", tripId);
        Optional<Trip> tripOpt = this.tripRepository.findById(tripId);
        if (tripOpt.isPresent()) {
            log.info("Fetched tripId={}", tripId);
        } else {
            log.info("Trip with Id={} was not found", tripId);
        }
        return tripOpt;
    }

    @Override
    public Page<Trip> getTripsForAccount(Pageable pageable, Account account) {
        log.debug("Fetching trips for account: {}", account.getId());
        Page<Trip> trips = this.tripRepository.findAllRelatedTrips(pageable, account);
        log.info("Retrieved {} trips for account={}", trips.getTotalElements(), account.getId());
        return trips;
    }

    @Override
    public boolean isTripAdmin(Trip trip, Account account) {
        boolean isAdmin = Objects.equals(trip.getOwner().getId(), account.getId());
        log.info("Checking if account {} is admin for trip {}: {}", account.getId(), trip.getId(), isAdmin);
        return isAdmin;
    }

    @Override
    public void deleteTrip(String tripId, Account account) {
        log.debug("Attempting to delete trip with ID: {} by account: {}", tripId, account.getId());
        Trip tripToDelete = this.tripRepository.findTripByIdWhereAccountIsOwnerOrAdmin(tripId, account)
                .orElseThrow(() -> {
                    log.warn("Trip {} not found or insufficient permissions for account: {}", tripId, account.getId());
                    return new ItemNotFound("Trip was not found or you do not have enough permissions");
                });
        tripToDelete.setDeleted(true);
        this.tripRepository.save(tripToDelete);
        log.info("Trip with Id={} marked as deleted", tripId);
    }

    @Override
    public Trip archiveTrip(String tripId, Account account) {
        log.debug("Attempting to archive trip with ID: {} by account: {}", tripId, account.getId());
        Trip tripToArchive = this.tripRepository.findTripByIdWhereAccountIsOwnerOrAdmin(tripId, account)
                .orElseThrow(() -> {
                    log.warn("Trip {} not found or insufficient permissions for account: {}", tripId, account.getId());
                    return new ItemNotFound("Trip was not found or you do not have enough permissions");
                });
        tripToArchive.setArchived(true);
        Trip archivedTrip = this.tripRepository.save(tripToArchive);
        log.info("Trip with Id={} archived", tripId);
        return archivedTrip;
    }

    @Override
    public Trip duplicateTrip(String tripId, Account account) {
        log.debug("Attempting to duplicate trip with Id={} by accountId={}", tripId, account.getId());
        Trip tripToDuplicate = this.tripRepository.findTripById(tripId, account)
                .orElseThrow(() -> {
                    log.warn("Trip {} not found or insufficient permissions for accountId={}", tripId, account.getId());
                    return new ItemNotFound("Trip was not found or you do not have enough permissions");
                });
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
        Trip savedTrip = this.tripRepository.save(duplicatedTrip);
        log.info("Trip duplicated with new Id={} by accountId={}", savedTrip.getId(), account.getId());
        return savedTrip;
    }

    @Override
    @Transactional
    public boolean removeAccountFromTrip(final String tripId, final Account account, final Account currentAccount) {
        log.debug("Attempting to remove account {} from trip {} by accountId={}", account.getId(), tripId, currentAccount.getId());
        Trip trip = this.tripRepository.findTripByIdWhereAccountIsOwnerOrAdmin(tripId, currentAccount)
                .orElseThrow(() -> {
                    log.warn("Trip {} not found or insufficient permissions for accountId={}", tripId, currentAccount.getId());
                    return new ItemNotFound("Trip was not found or you do not have enough permissions");
                });
        List<Member> accountAsMember = trip.getMembers().stream()
                .filter(member -> Objects.equals(member.getAccountId(), account.getId()))
                .toList();
        if (!accountAsMember.isEmpty() && trip.getMembers().removeAll(accountAsMember)) {
            this.tripRepository.save(trip);
            log.info("Account {} successfully removed from trip {}", account.getId(), tripId);
            return true;
        }
        log.info("Account {} was not a member of trip {} or removal failed", account.getId(), tripId);
        return false;
    }
}
