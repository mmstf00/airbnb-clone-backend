package com.airbnb.listingservice.service;

import com.airbnb.listingservice.entity.Listing;
import com.airbnb.listingservice.exception.ListingNotFoundException;
import com.airbnb.listingservice.repository.ListingRepository;
import com.airbnb.listingservice.util.ListingsTestingUtil;
import com.airbnb.proto.listing.GetListingRequest;
import com.airbnb.proto.listing.ListingMessage;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class ListingServiceGrpcImplTest {

    @Mock
    private ListingRepository listingRepository;

    @Mock
    private StreamObserver<ListingMessage> responseObserver;

    @InjectMocks
    private ListingServiceGrpcImpl listingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetListing() {
        // Arrange
        long listingId = 1L;

        GetListingRequest request = GetListingRequest.newBuilder().setId(listingId).build();

        Listing listing = ListingsTestingUtil.getListing(listingId);

        when(listingRepository.findById(anyLong())).thenReturn(Optional.of(listing));

        // Act
        listingService.getListing(request, responseObserver);

        // Assert
        verify(listingRepository).findById(listingId);
        verify(responseObserver).onNext(any(ListingMessage.class));
        verify(responseObserver).onCompleted();
        verify(responseObserver, never()).onError(any());
    }

    @Test
    void testGetListingNotFound() {
        // Arrange
        long listingId = 2L;
        GetListingRequest request = GetListingRequest.newBuilder().setId(listingId).build();

        when(listingRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Assert
        assertThrows(ListingNotFoundException.class,
                () -> listingService.getListing(request, responseObserver));
    }
}