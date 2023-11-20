package com.airbnb.reservationservice.controller;

import com.airbnb.reservationservice.entity.Reservation;
import com.airbnb.reservationservice.exception.ReservationNotFoundException;
import com.airbnb.reservationservice.service.ReservationsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.util.Arrays;

import static com.airbnb.reservationservice.util.ReservationsTestingUtil.getReservation;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@WebMvcTest(ReservationsController.class)
class ReservationsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReservationsService reservationsService;

    @Autowired
    private ObjectMapper objectMapper;

    private final String API_ENDPOINT = "/api/v1/";

    @Test
    void testGetAllReservations() throws Exception {
        when(reservationsService.getReservations()).thenReturn(
                Arrays.asList(
                        getReservation(1L, BigDecimal.valueOf(123)),
                        getReservation(2L, BigDecimal.valueOf(345))
                )
        );

        mockMvc.perform(MockMvcRequestBuilders.get(API_ENDPOINT + "/reservations"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].totalPrice").value(123))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].totalPrice").value(345));
    }

    @Test
    void testGetReservationById() throws Exception {
        when(reservationsService.getReservation(1L))
                .thenReturn(getReservation(1L, BigDecimal.valueOf(123)));
        mockMvc.perform(MockMvcRequestBuilders.get(API_ENDPOINT + "/reservations/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.totalPrice").value(123));

        when(reservationsService.getReservation(2L))
                .thenReturn(getReservation(2L, BigDecimal.valueOf(345)));
        mockMvc.perform(MockMvcRequestBuilders.get(API_ENDPOINT + "/reservations/2"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.totalPrice").value(345));

        when(reservationsService.getReservation(3L))
                .thenThrow(new ReservationNotFoundException(3L));
        mockMvc.perform(MockMvcRequestBuilders.get(API_ENDPOINT + "/reservations/3"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testMakeReservation() throws Exception {
        Reservation newReservation = getReservation(3L, BigDecimal.valueOf(678));
        String successResponse = "Reservation successfully created with ID: 3";

        when(reservationsService.makeReservation(any(Reservation.class))).thenReturn(successResponse);
        mockMvc.perform(MockMvcRequestBuilders.post(API_ENDPOINT + "/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newReservation)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Reservation successfully created with ID: 3"));
    }

    @Test
    void testDeleteReservation() throws Exception {
        String successResponse = "Reservation successfully deleted with ID: 4";

        when(reservationsService.deleteReservation(4L)).thenReturn(successResponse);
        mockMvc.perform(MockMvcRequestBuilders.delete(API_ENDPOINT + "/reservations/4"))
                .andExpect(status().isOk())
                .andExpect(content().string("Reservation successfully deleted with ID: 4"));

        // Verify that the deleteReservation method was called
        verify(reservationsService, times(1)).deleteReservation(4L);
    }

    @Test
    void testDeleteNonExistingReservation() throws Exception {
        when(reservationsService.deleteReservation(45L))
                .thenThrow(new ReservationNotFoundException(45L));

        mockMvc.perform(MockMvcRequestBuilders.delete(API_ENDPOINT + "/reservations/45"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.message").value("Reservation not found"));

        // Verify that the deleteReservation method was called
        verify(reservationsService, times(1)).deleteReservation(45L);
    }
}