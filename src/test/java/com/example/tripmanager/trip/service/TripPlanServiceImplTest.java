package com.example.tripmanager.trip.service;

import com.example.tripmanager.account.service.AccountService;
import com.example.tripmanager.shared.exception.ItemNotFound;
import com.example.tripmanager.trip.mapper.TripPlanMapper;
import com.example.tripmanager.account.model.Account;
import com.example.tripmanager.trip.model.Trip;
import com.example.tripmanager.trip.model.tripPlan.TripPlan;
import com.example.tripmanager.trip.model.tripPlan.TripPlanDto;
import com.example.tripmanager.trip.repository.TripPlanRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TripPlanServiceImplTest {

    @Mock
    private TripPlanRepository tripPlanRepository;
    @Mock
    private TripService tripService;
    @Mock
    private AccountService accountService;
    @InjectMocks
    private TripPlanServiceImpl tripPlanService;

    @Mock
    private Pageable pageable;

    @Test
    void testGetAllTripPlansForTrip_Success() {
        String tripId = "trip1";
        Account account = new Account();
        account.setId("account1");

        Trip trip = new Trip();
        trip.setId(tripId);

        when(tripService.getTripById(tripId)).thenReturn(Optional.of(trip));
        TripPlan tripPlan = new TripPlan();
        Page<TripPlan> page = new PageImpl<>(List.of(tripPlan));
        when(tripPlanRepository.findAllByTripId(pageable, tripId, account.getId())).thenReturn(page);

        Page<TripPlan> result = tripPlanService.getAllTripPlansForTrip(pageable, tripId, account);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        verify(tripService, times(1)).getTripById(tripId);
        verify(tripPlanRepository, times(1)).findAllByTripId(pageable, tripId, account.getId());
    }

    @Test
    void testGetAllTripPlansForTrip_TripNotFound() {
        String tripId = "trip1";
        Account account = new Account();

        when(tripService.getTripById(tripId)).thenReturn(Optional.empty());

        ItemNotFound exception = assertThrows(ItemNotFound.class, () ->
                tripPlanService.getAllTripPlansForTrip(pageable, tripId, account)
        );
        assertEquals("Trip not found - id=" + tripId, exception.getMessage());
        verify(tripService, times(1)).getTripById(tripId);
        verifyNoInteractions(tripPlanRepository);
    }

    @Test
    void testInsertTripPlan_Success() {
        String tripId = "trip1";
        TripPlanDto dto = new TripPlanDto();

        Trip trip = new Trip();
        trip.setId(tripId);
        when(tripService.getTripById(tripId)).thenReturn(Optional.of(trip));

        TripPlan mappedTripPlan = new TripPlan();
        try (var mapperMock = mockStatic(TripPlanMapper.class)) {
            mapperMock.when(() -> TripPlanMapper.fromDto(dto)).thenReturn(mappedTripPlan);
            when(tripPlanRepository.save(mappedTripPlan)).thenReturn(mappedTripPlan);

            TripPlan result = tripPlanService.insertTripPlan(dto, tripId);

            assertEquals(mappedTripPlan, result);
            mapperMock.verify(() -> TripPlanMapper.fromDto(dto), times(1));
            verify(tripPlanRepository, times(1)).save(mappedTripPlan);
        }
    }

    @Test
    void testInsertTripPlan_TripNotFound() {
        String tripId = "trip1";
        TripPlanDto dto = new TripPlanDto();
        when(tripService.getTripById(tripId)).thenReturn(Optional.empty());

        ItemNotFound exception = assertThrows(ItemNotFound.class, () ->
                tripPlanService.insertTripPlan(dto, tripId)
        );
        assertEquals("Trip not found - id=" + tripId, exception.getMessage());
        verify(tripService, times(1)).getTripById(tripId);
        verifyNoInteractions(tripPlanRepository);
    }

    @Test
    void testDeleteTripPlan_Success() {
        String tripPlanId = "plan1";
        Account account = new Account();
        account.setId("account1");

        TripPlan tripPlan = new TripPlan();
        when(tripPlanRepository.findByIdWhereUserIsAdmin(tripPlanId, account.getId()))
                .thenReturn(Optional.of(tripPlan));

        tripPlanService.deleteTripPlan(tripPlanId, account);

        verify(tripPlanRepository, times(1)).delete(tripPlan);
    }

    @Test
    void testDeleteTripPlan_NotFound() {
        String tripPlanId = "plan1";
        Account account = new Account();
        account.setId("account1");

        when(tripPlanRepository.findByIdWhereUserIsAdmin(tripPlanId, account.getId()))
                .thenReturn(Optional.empty());

        ItemNotFound exception = assertThrows(ItemNotFound.class, () ->
                tripPlanService.deleteTripPlan(tripPlanId, account)
        );
        assertEquals("TripPlan not found - id=" + tripPlanId, exception.getMessage());
        verify(tripPlanRepository, times(1)).findByIdWhereUserIsAdmin(tripPlanId, account.getId());
        verify(tripPlanRepository, never()).delete(any());
    }

    @Test
    void testPatchTripPlan_Success() {
        String tripId = "trip1";
        TripPlan updatedTripPlan = new TripPlan();
        when(tripPlanRepository.save(updatedTripPlan)).thenReturn(updatedTripPlan);

        TripPlan result = tripPlanService.patchTripPlan(updatedTripPlan, tripId);

        assertEquals(updatedTripPlan, result);
        verify(tripPlanRepository, times(1)).save(updatedTripPlan);
    }

    @Test
    void testGetAllTripPlansWithMapElement_Success() {
        String tripId = "trip1";
        Account account = new Account();
        account.setId("account1");

        TripPlan tripPlan = new TripPlan();
        Page<TripPlan> page = new PageImpl<>(List.of(tripPlan));
        when(tripPlanRepository.findAllTripPlansWithMapElementForGivenTripId(pageable, tripId, account.getId()))
                .thenReturn(page);

        Page<TripPlan> result = tripPlanService.getAllTripPlansWithMapElement(pageable, tripId, account);

        assertEquals(page, result);
        verify(tripPlanRepository, times(1))
                .findAllTripPlansWithMapElementForGivenTripId(pageable, tripId, account.getId());
    }
}
