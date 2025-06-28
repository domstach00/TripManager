package com.example.tripmanager.trip.service;

import com.example.tripmanager.account.service.AccountService;
import com.example.tripmanager.shared.controller.support.PageParams;
import com.example.tripmanager.shared.exception.ItemNotFound;
import com.example.tripmanager.trip.mapper.TripPlanMapper;
import com.example.tripmanager.account.model.Account;
import com.example.tripmanager.trip.model.Trip;
import com.example.tripmanager.trip.model.tripPlan.TripPlan;
import com.example.tripmanager.trip.model.tripPlan.TripPlanDto;
import com.example.tripmanager.trip.repository.TripPlanRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TripPlanServiceImpl implements TripPlanService{
    private static final Logger log = LoggerFactory.getLogger(TripPlanServiceImpl.class);
    @Autowired
    private TripPlanRepository tripPlanRepository;
    @Autowired
    private TripService tripService;
    @Autowired
    protected AccountService accountService;

    protected TripPlan fromDto(TripPlanDto tripPlanDto) {
        return TripPlanMapper.fromDto(tripPlanDto);
    }

    @Override
    public Page<TripPlan> getAllTripPlansForTrip(Pageable pageable, String tripId, Account currentAccount) {
        log.debug("Fetching trip plans for tripId={} and accountId={}", tripId, currentAccount.getId());
        Trip trip = tripService.getTripById(tripId)
                .orElseThrow(() -> {
                    log.warn("Trip not found - id={}", tripId);
                    return new ItemNotFound("Trip not found - id=" + tripId);
                });
        Page<TripPlan> tripPlans = this.tripPlanRepository.findAllByTripId(pageable, trip.getId(), currentAccount.getId());
        log.info("Retrieved {} tripPlans for tripId: {}", tripPlans.getTotalElements(), tripId);
        return tripPlans;
    }

    @Override
    public TripPlan insertTripPlan(TripPlanDto tripPlanDto, String tripId) {
        log.debug("Inserting new trip plan for tripId={}", tripId);
        Trip trip = tripService.getTripById(tripId)
                .orElseThrow(() -> new ItemNotFound("Trip not found - id=" + tripId));
        TripPlan tripPlan = fromDto(tripPlanDto);

        TripPlan savedTripPlan = this.tripPlanRepository.save(tripPlan);
        log.info("Successfully inserted trip plan with id={}", savedTripPlan.getId());
        return savedTripPlan;
    }

    @Override
    public void deleteTripPlan(String tripPlanId, Account currentAccount) {
        log.debug("Deleting trip plan with id={} by accountId={}", tripPlanId, currentAccount.getId());
        TripPlan tripPlanToDelete = this.tripPlanRepository.findByIdWhereUserIsAdmin(tripPlanId, currentAccount.getId())
                .orElseThrow(() -> {
                    log.warn("TripPlan not found or insufficient permissions - id={}", tripPlanId);
                    return new ItemNotFound("TripPlan not found - id=" + tripPlanId);
                });
        this.tripPlanRepository.delete(tripPlanToDelete);
        log.info("Successfully deleted trip plan with id={} by accountId={}", tripPlanId, currentAccount.getId());
    }

    @Override
    @Transactional
    public void deleteAllTripPlansForTrip(String tripId, Account currentAccount) {
        log.debug("Deleting all trip plans for tripId={} by accountId={}", tripId, currentAccount.getId());
        Trip trip = tripService.getTripById(tripId)
                .orElseThrow(() -> new ItemNotFound("Trip not found - id=" + tripId));

        int pageNumber = 0;
        int pageSize = 500;
        Page<TripPlan> page;

        do {
            PageParams pageParams = new PageParams(pageNumber, pageSize);
            Pageable pageable = pageParams.asPageable();
            page = getAllTripPlansForTrip(pageable, trip.getId(), currentAccount);

            if (page.isEmpty()) {
                if (pageNumber == 0) {
                    log.info("No active trip plans found for tripId={}", tripId);
                }
                break;
            }

            List<TripPlan> toDelete = page.getContent();
            toDelete.forEach(tp -> {
                tp.setDeleted(true);
                tripPlanRepository.save(tp);
            });

            log.info("Marked {} trip plans as deleted on page {} for tripId={}",
                    toDelete.size(), pageNumber, tripId);
            pageNumber++;
        } while (page.hasNext());

        log.info("Finished deleting {} trip plans for tripId={} by accountId={}", page.getTotalElements(), tripId, currentAccount.getId());
    }


    @Override
    public TripPlan patchTripPlan(TripPlan updatedTripPlan, String tripId) {
        log.debug("Patching trip plan with id={} for tripId={}", updatedTripPlan.getId(), tripId);

        TripPlan existingTripPlan = tripPlanRepository.findById(updatedTripPlan.getId())
                .orElseThrow(() -> {
                    log.warn("TripPlan not found for patching - id={}", updatedTripPlan.getId());
                    return new ItemNotFound("TripPlan not found for patching - id=" + updatedTripPlan.getId());
                });

        if (updatedTripPlan.getName() != null) {
            existingTripPlan.setName(updatedTripPlan.getName());
        }
        existingTripPlan.setDay(updatedTripPlan.getDay());
        existingTripPlan.setCost(updatedTripPlan.getCost());

        if (updatedTripPlan.getDesc() != null) {
            existingTripPlan.setDesc(updatedTripPlan.getDesc());
        }
        if (updatedTripPlan.getLink() != null) {
            existingTripPlan.setLink(updatedTripPlan.getLink());
        }

        if (updatedTripPlan.getMapElement() != null) {
            existingTripPlan.setMapElement(updatedTripPlan.getMapElement());
        }

        TripPlan savedTripPlan = tripPlanRepository.save(existingTripPlan);
        log.info("Successfully patched trip plan with id={}", savedTripPlan.getId());
        return savedTripPlan;
    }

    @Override
    public Page<TripPlan> getAllTripPlansWithMapElement(Pageable pageable, String tripId, Account currentAccount) {
        log.debug("Fetching trip plans with map elements for tripId={} and accountId={}", tripId, currentAccount.getId());
        Page<TripPlan> tripPlans = tripPlanRepository.findAllTripPlansWithMapElementForGivenTripId(pageable, tripId, currentAccount.getId());
        log.info("Retrieved {} tripPlans with map elements for tripId={} and accountId={}", tripPlans.getTotalElements(), tripId, currentAccount.getId());
        return tripPlans;
    }
}
