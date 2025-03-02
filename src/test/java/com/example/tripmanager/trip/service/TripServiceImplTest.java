package com.example.tripmanager.trip.service;

import com.example.tripmanager.shared.exception.ItemNotFound;
import com.example.tripmanager.trip.mapper.TripMapper;
import com.example.tripmanager.shared.model.common.Member;
import com.example.tripmanager.trip.model.Trip;
import com.example.tripmanager.trip.model.TripDto;
import com.example.tripmanager.account.model.Account;
import com.example.tripmanager.trip.repository.TripRepository;
import com.example.tripmanager.account.service.AccountService;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TripServiceImplTest {

    @Mock
    private TripRepository tripRepository;
    @Mock
    private AccountService accountService;
    @Mock
    private Pageable pageable;
    @InjectMocks
    private TripServiceImpl tripServiceImpl;

    @Test
    void testCreateTrip_Success() {
        TripDto tripDto = new TripDto();
        Account currentAccount = new Account();
        currentAccount.setId("account1");

        Trip tripFromDto = new Trip();
        tripFromDto.setName("TripName");
        try (var mapperMock = mockStatic(TripMapper.class)) {
            mapperMock.when(() -> TripMapper.createFromDto(tripDto, currentAccount, accountService))
                    .thenReturn(tripFromDto);
            when(tripRepository.save(tripFromDto)).thenReturn(tripFromDto);

            Trip result = tripServiceImpl.createTrip(tripDto, currentAccount);
            assertEquals(tripFromDto, result);
            mapperMock.verify(() -> TripMapper.createFromDto(tripDto, currentAccount, accountService), times(1));
            verify(tripRepository, times(1)).save(tripFromDto);
        }
    }

    @Test
    void testGetTripById_Success() {
        String tripId = "trip1";
        Trip trip = new Trip();
        when(tripRepository.findById(tripId)).thenReturn(Optional.of(trip));

        Optional<Trip> result = tripServiceImpl.getTripById(tripId);
        assertTrue(result.isPresent());
        assertEquals(trip, result.get());
        verify(tripRepository, times(1)).findById(tripId);
    }

    @Test
    void testGetTripsForAccount() {
        Account account = new Account();
        account.setId("account1");
        List<Trip> trips = List.of(new Trip(), new Trip());
        Page<Trip> page = new PageImpl<>(trips);
        when(tripRepository.findAllRelatedTrips(pageable, account)).thenReturn(page);

        Page<Trip> result = tripServiceImpl.getTripsForAccount(pageable, account);
        assertEquals(2, result.getContent().size());
        verify(tripRepository, times(1)).findAllRelatedTrips(pageable, account);
    }

    @Test
    void testIsTripAdmin() {
        Account account = new Account();
        account.setId("account1");

        Trip trip = new Trip();
        Account owner = new Account();
        owner.setId("account1");
        trip.setOwner(owner);

        assertTrue(tripServiceImpl.isTripAdmin(trip, account));

        Account anotherAccount = new Account();
        anotherAccount.setId("account2");
        assertFalse(tripServiceImpl.isTripAdmin(trip, anotherAccount));
    }

    @Test
    void testDeleteTrip_Success() {
        String tripId = "trip1";
        Account account = new Account();
        account.setId("account1");

        Trip trip = new Trip();
        when(tripRepository.findTripByIdWhereAccountIsOwnerOrAdmin(tripId, account))
                .thenReturn(Optional.of(trip));
        when(tripRepository.save(trip)).thenReturn(trip);

        tripServiceImpl.deleteTrip(tripId, account);
        assertTrue(trip.isDeleted());
        verify(tripRepository, times(1)).save(trip);
    }

    @Test
    void testDeleteTrip_NotFound() {
        String tripId = "trip1";
        Account account = new Account();
        account.setId("account1");

        when(tripRepository.findTripByIdWhereAccountIsOwnerOrAdmin(tripId, account))
                .thenReturn(Optional.empty());

        ItemNotFound exception = assertThrows(ItemNotFound.class, () ->
                tripServiceImpl.deleteTrip(tripId, account));
        assertEquals("Trip was not found or you do not have enough permissions", exception.getMessage());
        verify(tripRepository, times(1)).findTripByIdWhereAccountIsOwnerOrAdmin(tripId, account);
        verify(tripRepository, never()).save(any(Trip.class));
    }

    @Test
    void testArchiveTrip_Success() {
        String tripId = "trip1";
        Account account = new Account();
        account.setId("account1");

        Trip trip = new Trip();
        when(tripRepository.findTripByIdWhereAccountIsOwnerOrAdmin(tripId, account))
                .thenReturn(Optional.of(trip));
        when(tripRepository.save(trip)).thenReturn(trip);

        Trip result = tripServiceImpl.archiveTrip(tripId, account);
        assertTrue(trip.isArchived());
        assertEquals(trip, result);
        verify(tripRepository, times(1)).save(trip);
    }

    @Test
    void testDuplicateTrip_Success() {
        String tripId = "trip1";
        Account account = new Account();
        account.setId("account1");

        Trip originalTrip = new Trip();
        originalTrip.setName("OriginalTrip");
        when(tripRepository.findTripById(tripId, account)).thenReturn(Optional.of(originalTrip));
        when(tripRepository.save(any(Trip.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Trip result = tripServiceImpl.duplicateTrip(tripId, account);
        assertNotNull(result);
        assertTrue(result.getName().endsWith(" - duplicated"));
        assertNull(result.getId());
        assertNull(result.getCreatedTime());
        assertNull(result.getCreatedBy());
        assertNull(result.getLastModifiedTime());
        assertNull(result.getLastModifiedBy());
        assertEquals(account, result.getOwner());
        // Lista członków powinna być pusta
        assertNotNull(result.getMembers());
        assertTrue(result.getMembers().isEmpty());

        verify(tripRepository, times(1)).findTripById(tripId, account);
        verify(tripRepository, times(1)).save(any(Trip.class));
    }

    @Test
    void testDuplicateTrip_NotFound() {
        String tripId = "trip1";
        Account account = new Account();
        account.setId("account1");

        when(tripRepository.findTripById(tripId, account)).thenReturn(Optional.empty());
        ItemNotFound exception = assertThrows(ItemNotFound.class, () ->
                tripServiceImpl.duplicateTrip(tripId, account));
        assertEquals("Trip was not found or you do not have enough permissions", exception.getMessage());
        verify(tripRepository, times(1)).findTripById(tripId, account);
        verify(tripRepository, never()).save(any(Trip.class));
    }

    @Test
    void testRemoveAccountFromTrip_Success() {
        String tripId = new ObjectId().toHexString();
        Account accountToRemove = new Account();
        String accountToRemoveId = new ObjectId().toHexString();
        accountToRemove.setId(accountToRemoveId);

        Account currentAccount = new Account();
        String currentAccountId = new ObjectId().toHexString();
        currentAccount.setId(currentAccountId);

        Trip trip = new Trip();

        Member member = new Member();
        member.setAccountId(accountToRemoveId);

        List<Member> members = new ArrayList<>();
        members.add(member);
        trip.setMembers(members);

        when(tripRepository.findTripByIdWhereAccountIsOwnerOrAdmin(tripId, currentAccount))
                .thenReturn(Optional.of(trip));
        when(tripRepository.save(trip)).thenReturn(trip);

        boolean result = tripServiceImpl.removeAccountFromTrip(tripId, accountToRemove, currentAccount);

        assertTrue(result);
        assertTrue(trip.getMembers().isEmpty());
        verify(tripRepository, times(1)).save(trip);
    }


    @Test
    void testRemoveAccountFromTrip_NoMemberFound() {
        String tripId = "trip1";
        Account accountToRemove = new Account();
        accountToRemove.setId("remove1");
        Account currentAccount = new Account();
        currentAccount.setId("current1");

        Trip trip = new Trip();
        trip.setMembers(new ArrayList<>());

        when(tripRepository.findTripByIdWhereAccountIsOwnerOrAdmin(tripId, currentAccount))
                .thenReturn(Optional.of(trip));

        boolean result = tripServiceImpl.removeAccountFromTrip(tripId, accountToRemove, currentAccount);
        assertFalse(result);
        verify(tripRepository, never()).save(any(Trip.class));
    }

    @Test
    void testRemoveAccountFromTrip_TripNotFound() {
        String tripId = "trip1";
        Account accountToRemove = new Account();
        accountToRemove.setId("remove1");
        Account currentAccount = new Account();
        currentAccount.setId("current1");

        when(tripRepository.findTripByIdWhereAccountIsOwnerOrAdmin(tripId, currentAccount))
                .thenReturn(Optional.empty());

        ItemNotFound exception = assertThrows(ItemNotFound.class, () ->
                tripServiceImpl.removeAccountFromTrip(tripId, accountToRemove, currentAccount));
        assertEquals("Trip was not found or you do not have enough permissions", exception.getMessage());
        verify(tripRepository, times(1)).findTripByIdWhereAccountIsOwnerOrAdmin(tripId, currentAccount);
        verify(tripRepository, never()).save(any(Trip.class));
    }
}
