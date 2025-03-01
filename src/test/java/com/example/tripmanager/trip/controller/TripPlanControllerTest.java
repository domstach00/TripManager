package com.example.tripmanager.trip.controller;

import com.example.tripmanager.account.model.Account;
import com.example.tripmanager.trip.model.tripPlan.TripPlan;
import com.example.tripmanager.trip.model.tripPlan.TripPlanDto;
import com.example.tripmanager.trip.service.TripPlanService;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TripPlanControllerTest {

    @Mock
    private TripPlanService tripPlanService;

    @Mock
    private AccountService accountService;

    // We use spy to be able to override protected methods (e.g. getCurrentAccount, toDto, createFromDto and validation)
    @InjectMocks
    @Spy
    private TripPlanController tripPlanController;

    private Account dummyAccount;
    private Principal dummyPrincipal;

    @BeforeEach
    void setUp() {
        dummyAccount = new Account();
        dummyAccount.setId("acc1");

        lenient().when(accountService.getCurrentAccount(any(Principal.class))).thenReturn(dummyAccount);

        dummyPrincipal = () -> "user@example.com";
    }

    @Test
    void testGetAllTripPlan() {
        String tripId = "trip1";
        PageParams pageParams = new PageParams();
        Pageable pageable = pageParams.asPageable();

        TripPlan tp1 = new TripPlan();
        tp1.setId("tp1");
        TripPlan tp2 = new TripPlan();
        tp2.setId("tp2");
        Page<TripPlan> tripPlanPage = new PageImpl<>(List.of(tp1, tp2));

        when(tripPlanService.getAllTripPlansForTrip(eq(pageable), eq(tripId), eq(dummyAccount)))
                .thenReturn(tripPlanPage);

        TripPlanDto dto1 = new TripPlanDto();
        dto1.setId("tp1");
        TripPlanDto dto2 = new TripPlanDto();
        dto2.setId("tp2");
        Page<TripPlanDto> expectedPage = new PageImpl<>(List.of(dto1, dto2));
        doReturn(expectedPage).when(tripPlanController).toDto(tripPlanPage);

        Page<TripPlanDto> result = tripPlanController.getAllTripPlan(dummyPrincipal, pageParams, tripId);

        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        assertEquals("tp1", result.getContent().get(0).getId());
        verify(tripPlanService, times(1))
                .getAllTripPlansForTrip(eq(pageable), eq(tripId), eq(dummyAccount));
    }

    @Test
    void testGetAllTripPlansWithMapElement() {
        String tripId = "trip1";
        PageParams pageParams = new PageParams();
        Pageable pageable = pageParams.asPageable();

        TripPlan tp1 = new TripPlan();
        tp1.setId("tp1");
        Page<TripPlan> tripPlanPage = new PageImpl<>(List.of(tp1));
        when(tripPlanService.getAllTripPlansWithMapElement(eq(pageable), eq(tripId), eq(dummyAccount)))
                .thenReturn(tripPlanPage);

        TripPlanDto dto1 = new TripPlanDto();
        dto1.setId("tp1");
        Page<TripPlanDto> expectedPage = new PageImpl<>(List.of(dto1));
        doReturn(expectedPage).when(tripPlanController).toDto(tripPlanPage);

        Page<TripPlanDto> result = tripPlanController.getAllTripPlansWithMapElement(dummyPrincipal, pageParams, tripId);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals("tp1", result.getContent().get(0).getId());
        verify(tripPlanService, times(1))
                .getAllTripPlansWithMapElement(eq(pageable), eq(tripId), eq(dummyAccount));
    }

    @Test
    void testPostTripPlan() {
        String tripId = "trip1";
        TripPlanDto inputDto = new TripPlanDto();
        inputDto.setTripId(tripId);

        lenient().doNothing().when(tripPlanController).throwErrorOnValidateIdsFromUrlAndBody(eq(tripId), eq(tripId));

        TripPlan insertedTripPlan = new TripPlan();
        insertedTripPlan.setId("tp1");
        when(tripPlanService.insertTripPlan(eq(inputDto), eq(tripId))).thenReturn(insertedTripPlan);

        // Nadpisujemy mapowanie z TripPlan na DTO
        TripPlanDto expectedDto = new TripPlanDto();
        expectedDto.setId("tp1");
        doReturn(expectedDto).when(tripPlanController).toDto(insertedTripPlan);

        TripPlanDto result = tripPlanController.postTripPlan(tripId, inputDto);

        assertNotNull(result);
        assertEquals("tp1", result.getId());
        verify(tripPlanService, times(1)).insertTripPlan(eq(inputDto), eq(tripId));
        verify(tripPlanController, times(1))
                .throwErrorOnValidateIdsFromUrlAndBody(eq(tripId), eq(tripId));
    }

    @Test
    void testPatchTripPlan() {
        String tripId = "trip1";
        TripPlanDto inputDto = new TripPlanDto();
        inputDto.setTripId(tripId);
        inputDto.setId("tp1");

        lenient().doNothing().when(tripPlanController).throwErrorOnValidateIdsFromUrlAndBody(eq(tripId), eq(tripId));

        TripPlan convertedTripPlan = new TripPlan();
        convertedTripPlan.setId("tp1");
        doReturn(convertedTripPlan).when(tripPlanController).createFromDto(inputDto);

        TripPlan patchedTripPlan = new TripPlan();
        patchedTripPlan.setId("tp1");
        when(tripPlanService.patchTripPlan(eq(convertedTripPlan), eq(tripId))).thenReturn(patchedTripPlan);

        TripPlanDto expectedDto = new TripPlanDto();
        expectedDto.setId("tp1");
        doReturn(expectedDto).when(tripPlanController).toDto(patchedTripPlan);

        TripPlanDto result = tripPlanController.patchTripPlan(tripId, inputDto);

        assertNotNull(result);
        assertEquals("tp1", result.getId());
        verify(tripPlanService, times(1)).patchTripPlan(eq(convertedTripPlan), eq(tripId));
        verify(tripPlanController, times(1))
                .throwErrorOnValidateIdsFromUrlAndBody(eq(tripId), eq(tripId));
    }

    @Test
    void testDeleteTripPlan() {
        String tripId = "trip1";
        String tripPlanId = "tp1";

        tripPlanController.deleteTripPlan(dummyPrincipal, tripId, tripPlanId);
        verify(tripPlanService, times(1)).deleteTripPlan(eq(tripPlanId), eq(dummyAccount));
    }
}
