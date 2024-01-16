package com.airbnb.aggregatorservice.service;

import com.airbnb.aggregatorservice.model.request.Request;
import com.airbnb.aggregatorservice.model.response.AggregatedResponse;
import com.airbnb.aggregatorservice.model.response.FavoriteListingsResponse;
import com.airbnb.proto.listing.ListingMessage;
import com.airbnb.proto.listing.ListingServiceGrpc;
import com.airbnb.proto.reservations.ReservationMessage;
import com.airbnb.proto.reservations.ReservationServiceGrpc;
import com.airbnb.proto.users.UserMessage;
import com.airbnb.proto.users.UserServiceGrpc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class SyncAggregatorServiceTest {

    @Mock
    private UserServiceGrpc.UserServiceBlockingStub userStub;

    @Mock
    private ListingServiceGrpc.ListingServiceBlockingStub listingStub;

    @Mock
    private ReservationServiceGrpc.ReservationServiceBlockingStub reservationStub;

    @InjectMocks
    private SyncAggregatorService aggregatorService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testProcessRequest() {
        // Mocking
        Request mockRequest = Request.builder()
                .userId(1L)
                .listingId(2L)
                .reservationId(3L)
                .build();
        UserMessage mockUser = UserMessage.newBuilder().build();
        ListingMessage mockListing = ListingMessage.newBuilder()
                .setPrice("20")
                .build();
        ReservationMessage mockReservation = ReservationMessage.newBuilder()
                .setTotalPrice("20")
                .build();

        when(userStub.getUser(any())).thenReturn(mockUser);
        when(listingStub.getListing(any())).thenReturn(mockListing);
        when(reservationStub.getReservation(any())).thenReturn(mockReservation);

        // Actual test
        AggregatedResponse result = aggregatorService.processRequest(mockRequest);

        // Verify calls and assert the result
        verify(userStub, times(1)).getUser(any());
        verify(listingStub, times(1)).getListing(any());
        verify(reservationStub, times(1)).getReservation(any());
        assertNotNull(result);
    }

    @Test
    void testGetFavoriteListingsForUser() {
        // Mocking
        long userId = 1L;
        UserMessage mockUser = UserMessage.newBuilder().setId(userId).build();
        when(userStub.getUser(any())).thenReturn(mockUser);

        // Actual test
        FavoriteListingsResponse result = aggregatorService.getFavoriteListingsForUser(userId);

        // Verify calls and assert the result
        verify(userStub, times(1)).getUser(any());
        assertNotNull(result);
    }
}