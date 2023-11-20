package com.airbnb.listingservice.service;

import com.airbnb.listingservice.entity.Listing;
import com.airbnb.listingservice.exception.ListingNotFoundException;
import com.airbnb.listingservice.repository.ListingsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.airbnb.listingservice.util.ListingsTestingUtil.getListing;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class ListingsServiceTest {

    @Mock
    private ListingsRepository listingsRepository;

    private ListingsService listingsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize the annotated mocks
        listingsService = new ListingsService(listingsRepository);
    }

    @Test
    void testGetListing() {
        Long listingId = 1L;
        Listing expectedListing = getListing(listingId);
        when(listingsRepository.findById(listingId)).thenReturn(Optional.of(expectedListing));

        Listing actualListing = listingsService.getListing(listingId);
        assertEquals(expectedListing, actualListing);
    }

    @Test
    void testGetListingWhenNotExists() {
        Long nonExistentListingId = 2L;
        when(listingsRepository.findById(nonExistentListingId))
                .thenThrow(new ListingNotFoundException(nonExistentListingId));

        assertThrows(ListingNotFoundException.class,
                () -> listingsService.getListing(nonExistentListingId));
    }

    @Test
    void testGetListings() {
        List<Listing> expectedListings = new ArrayList<>();
        expectedListings.add(getListing(1L));
        expectedListings.add(getListing(2L));

        when(listingsRepository.findAll()).thenReturn(expectedListings);

        List<Listing> actualListings = listingsService.getListings();
        assertEquals(expectedListings, actualListings);
    }

    @Test
    void testCreateListing() {
        Listing newListing = getListing(3L);
        when(listingsRepository.saveAndFlush(newListing)).thenReturn(newListing);

        String response = listingsService.createListing(newListing);
        assertEquals("Listing successfully created with ID: 3", response);
    }

    @Test
    void testDeleteListing() {
        Long listingId = 4L;

        when(listingsRepository.findById(listingId))
                .thenReturn(Optional.of(Listing.builder().id(listingId).build()));
        String result = listingsService.deleteListing(listingId);

        assertEquals("Listing successfully deleted with ID: 4", result);
    }

    @Test
    void testDeleteNonExistingListing() {
        Long listingId = 4L;

        when(listingsRepository.findById(listingId)).thenReturn(Optional.empty());

        assertThrows(ListingNotFoundException.class, () -> listingsService.deleteListing(listingId));
    }

}