package com.example.tripmanager.trip.controller;

import com.example.tripmanager.account.model.Account;
import com.example.tripmanager.trip.model.Trip;
import com.example.tripmanager.trip.model.TripDto;
import com.example.tripmanager.trip.service.TripService;
import com.example.tripmanager.account.service.AccountService;
import com.example.tripmanager.shared.controller.support.PageParams;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.security.Principal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TripControllerTest {

    @Mock
    private TripService tripService;

    @Mock
    private AccountService accountService;

    // Używamy spy, aby nadpisać metody dziedziczone z AbstractController.
    @InjectMocks
    @Spy
    private TripController tripController;

    private Account dummyAccount;
    private Principal dummyPrincipal;

    @BeforeEach
    void setUp() {
        dummyAccount = new Account();
        dummyAccount.setId("acc1");

        dummyPrincipal = () -> "user@example.com";

        when(accountService.getCurrentAccount(any(Principal.class))).thenReturn(dummyAccount);
    }

    @Test
    void testPostTrip() {
        TripDto inputTripDto = new TripDto();
        inputTripDto.setName("Test Trip");

        Trip createdTrip = new Trip();
        createdTrip.setId("trip1");
        createdTrip.setName("Test Trip");
        when(tripService.createTrip(eq(inputTripDto), eq(dummyAccount))).thenReturn(createdTrip);

        TripDto expectedTripDto = new TripDto();
        expectedTripDto.setId("trip1");
        expectedTripDto.setName("Test Trip");
        doReturn(expectedTripDto).when(tripController).toDto(createdTrip);

        TripDto result = tripController.postTrip(dummyPrincipal, inputTripDto);

        // Weryfikacja wyniku
        assertNotNull(result);
        assertEquals("trip1", result.getId());
        assertEquals("Test Trip", result.getName());
        verify(tripService, times(1)).createTrip(eq(inputTripDto), eq(dummyAccount));
    }

    @Test
    void testGetTrips() {
        PageParams pageParams = new PageParams();
        Pageable pageable = pageParams.asPageable();

        Trip trip1 = new Trip();
        trip1.setId("trip1");
        trip1.setName("Trip One");
        Trip trip2 = new Trip();
        trip2.setId("trip2");
        trip2.setName("Trip Two");
        Page<Trip> tripPage = new PageImpl<>(List.of(trip1, trip2));
        when(tripService.getTripsForAccount(eq(pageable), eq(dummyAccount))).thenReturn(tripPage);

        TripDto dto1 = new TripDto();
        dto1.setId("trip1");
        dto1.setName("Trip One");
        TripDto dto2 = new TripDto();
        dto2.setId("trip2");
        dto2.setName("Trip Two");
        Page<TripDto> dtoPage = new PageImpl<>(List.of(dto1, dto2));
        doReturn(dtoPage).when(tripController).toDto(tripPage);

        Page<TripDto> result = tripController.getTrips(dummyPrincipal, pageParams);

        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        assertEquals("trip1", result.getContent().get(0).getId());
        assertEquals("trip2", result.getContent().get(1).getId());
        verify(tripService, times(1)).getTripsForAccount(eq(pageable), eq(dummyAccount));
    }

    @Test
    void testDeleteTrip() {
        String tripId = "trip1";
        // Wywołanie metody delete – metoda void, więc weryfikujemy interakcję z serwisem
        tripController.deleteTrip(dummyPrincipal, tripId);
        verify(tripService, times(1)).deleteTrip(eq(tripId), eq(dummyAccount));
    }

    @Test
    void testArchiveTrip() {
        String tripId = "trip1";
        Trip archivedTrip = new Trip();
        archivedTrip.setId("trip1");
        archivedTrip.setName("Archived Trip");
        when(tripService.archiveTrip(eq(tripId), eq(dummyAccount))).thenReturn(archivedTrip);

        // Nadpisanie mapowania do DTO
        TripDto expectedDto = new TripDto();
        expectedDto.setId("trip1");
        expectedDto.setName("Archived Trip");
        doReturn(expectedDto).when(tripController).toDto(archivedTrip);

        TripDto result = tripController.archiveTrip(dummyPrincipal, tripId);
        assertNotNull(result);
        assertEquals("trip1", result.getId());
        assertEquals("Archived Trip", result.getName());
        verify(tripService, times(1)).archiveTrip(eq(tripId), eq(dummyAccount));
    }

    @Test
    void testDuplicateTrip() {
        String tripId = "trip1";
        Trip duplicatedTrip = new Trip();
        duplicatedTrip.setId("trip2");
        duplicatedTrip.setName("Trip - duplicated");
        when(tripService.duplicateTrip(eq(tripId), eq(dummyAccount))).thenReturn(duplicatedTrip);

        // Nadpisanie mapowania do DTO
        TripDto expectedDto = new TripDto();
        expectedDto.setId("trip2");
        expectedDto.setName("Trip - duplicated");
        doReturn(expectedDto).when(tripController).toDto(duplicatedTrip);

        TripDto result = tripController.duplicateTrip(dummyPrincipal, tripId);
        assertNotNull(result);
        assertEquals("trip2", result.getId());
        assertEquals("Trip - duplicated", result.getName());
        verify(tripService, times(1)).duplicateTrip(eq(tripId), eq(dummyAccount));
    }

    @Test
    void testLeaveTripAsMember() {
        String tripId = "trip1";
        // Metoda leaveTripAsMember nie zwraca wartości, więc tylko weryfikujemy wywołanie serwisu.
        tripController.leaveTripAsMember(dummyPrincipal, tripId);
        verify(tripService, times(1))
                .removeAccountFromTrip(eq(tripId), eq(dummyAccount), eq(dummyAccount));
    }
}
