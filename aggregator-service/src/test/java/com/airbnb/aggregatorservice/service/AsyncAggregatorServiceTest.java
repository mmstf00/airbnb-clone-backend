package com.airbnb.aggregatorservice.service;

import com.airbnb.aggregatorservice.client.ListingDTO;
import com.airbnb.aggregatorservice.model.request.Request;
import com.airbnb.aggregatorservice.model.response.AggregatedResponse;
import com.airbnb.aggregatorservice.model.response.FavoriteListingsResponse;
import com.airbnb.aggregatorservice.util.GRPCMapper;
import com.airbnb.proto.listing.GetListingRequest;
import com.airbnb.proto.listing.ListingMessage;
import com.airbnb.proto.listing.ListingServiceGrpc;
import com.airbnb.proto.reservations.ReservationMessage;
import com.airbnb.proto.reservations.ReservationServiceGrpc;
import com.airbnb.proto.users.GetUserRequest;
import com.airbnb.proto.users.UserMessage;
import com.airbnb.proto.users.UserServiceGrpc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AsyncAggregatorServiceTest {

    @Mock
    private UserServiceGrpc.UserServiceBlockingStub userStub;

    @Mock
    private ListingServiceGrpc.ListingServiceBlockingStub listingStub;

    @Mock
    private ReservationServiceGrpc.ReservationServiceBlockingStub reservationStub;

    @Mock
    private ExecutorService executorService;

    @InjectMocks
    private AsyncAggregatorService asyncAggregatorService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        executorService = Executors.newFixedThreadPool(2);
        asyncAggregatorService = new AsyncAggregatorService(executorService);
        asyncAggregatorService.setUserStub(userStub);
        asyncAggregatorService.setListingStub(listingStub);
        asyncAggregatorService.setReservationStub(reservationStub);
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

        // Performing the test
        AggregatedResponse response = asyncAggregatorService.processRequest(mockRequest);

        verify(userStub, times(1)).getUser(any());
        verify(listingStub, times(1)).getListing(any());
        verify(reservationStub, times(1)).getReservation(any());
        assertNotNull(response);
    }

    @Test
    void testGetFavoriteListingsForUser() {
        // Mock the response from the gRPC service
        UserMessage userMessage = UserMessage.newBuilder()
                .addFavorites(1L)
                .addFavorites(2L)
                .build();
        when(userStub.getUser(any(GetUserRequest.class))).thenReturn(userMessage);

        // Mock the response from the getListing method
        ListingMessage listingMessage1 = ListingMessage.newBuilder().setPrice("21").build();
        ListingMessage listingMessage2 = ListingMessage.newBuilder().setPrice("23").build();
        when(listingStub.getListing(any(GetListingRequest.class)))
                .thenReturn(listingMessage1)
                .thenReturn(listingMessage2);

        // Create a sample user ID
        long userId = 1L;

        // Call the method under test
        FavoriteListingsResponse response = asyncAggregatorService.getFavoriteListingsForUser(userId);

        // Verify the result
        List<ListingDTO> expectedListings = Arrays.asList(
                GRPCMapper.toListingDTO(listingMessage1),
                GRPCMapper.toListingDTO(listingMessage2)
        );

        assertEquals(expectedListings, response.getFavorites());
    }
}