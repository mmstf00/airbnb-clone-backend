package com.airbnb.listingservice.controller;

import com.airbnb.listingservice.entity.Listing;
import com.airbnb.listingservice.exception.ListingNotFoundException;
import com.airbnb.listingservice.service.ListingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;

import static com.airbnb.listingservice.util.ListingsTestingUtil.getListing;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@WebMvcTest(ListingsController.class)
class ListingsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ListingService listingService;

    @Autowired
    private ObjectMapper objectMapper;

    private final String API_ENDPOINT = "/api/v1/";

    @Test
    void testGetAllListings() throws Exception {
        when(listingService.getListings()).thenReturn(
                Arrays.asList(
                        getListing(1L, "Listing 1"),
                        getListing(2L, "Listing 2")
                )
        );

        mockMvc.perform(MockMvcRequestBuilders.get(API_ENDPOINT + "/listings"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("Listing 1"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].title").value("Listing 2"));
    }

    @Test
    void testGetListingById() throws Exception {
        when(listingService.getListing(1L)).thenReturn(getListing(1L, "Listing 1"));
        mockMvc.perform(MockMvcRequestBuilders.get(API_ENDPOINT + "/listings/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Listing 1"));

        when(listingService.getListing(2L)).thenReturn(getListing(2L, "Listing 2"));
        mockMvc.perform(MockMvcRequestBuilders.get(API_ENDPOINT + "/listings/2"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.title").value("Listing 2"));

        when(listingService.getListing(3L))
                .thenThrow(new ListingNotFoundException(3L));
        mockMvc.perform(MockMvcRequestBuilders.get(API_ENDPOINT + "/listings/3"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateListing() throws Exception {
        Listing newListing = getListing(3L, "Listing 3");
        String successResponse = "Listing successfully created with ID: 3";

        when(listingService.createListing(any(Listing.class))).thenReturn(successResponse);
        mockMvc.perform(MockMvcRequestBuilders.post(API_ENDPOINT + "/listings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newListing)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Listing successfully created with ID: 3"));
    }

    @Test
    void testDeleteListing() throws Exception {
        String successResponse = "Listing successfully deleted with ID: 4";

        when(listingService.deleteListing(4L)).thenReturn(successResponse);
        mockMvc.perform(MockMvcRequestBuilders.delete(API_ENDPOINT + "/listings/4"))
                .andExpect(status().isOk())
                .andExpect(content().string("Listing successfully deleted with ID: 4"));

        // Verify that the deleteListing method was called
        verify(listingService, times(1)).deleteListing(4L);
    }

    @Test
    void testDeleteNonExistingListing() throws Exception {
        when(listingService.deleteListing(45L))
                .thenThrow(new ListingNotFoundException(45L));

        mockMvc.perform(MockMvcRequestBuilders.delete(API_ENDPOINT + "/listings/45"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.message").value("Listing not found"));

        // Verify that the deleteListing method was called
        verify(listingService, times(1)).deleteListing(45L);
    }

}