package com.airbnb.reservationservice.service;

import com.airbnb.reservationservice.entity.Reservation;
import com.airbnb.reservationservice.exception.ReservationNotFoundException;
import com.airbnb.reservationservice.repository.ReservationsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.airbnb.reservationservice.util.ReservationsTestingUtil.getReservation;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class ReservationsServiceTest {

    @Mock
    private ReservationsRepository reservationsRepository;

    private ReservationsService reservationsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize the annotated mocks
        reservationsService = new ReservationsService(reservationsRepository);
    }

    @Test
    void testGetReservation() {
        Long reservationId = 1L;
        Reservation expectedReservation = getReservation(reservationId);
        when(reservationsRepository.findById(reservationId)).thenReturn(Optional.of(expectedReservation));

        Reservation actualReservation = reservationsService.getReservation(reservationId);
        assertEquals(expectedReservation, actualReservation);
    }

    @Test
    void testGetReservationWhenNotExists() {
        Long nonExistentReservationId = 2L;
        when(reservationsRepository.findById(nonExistentReservationId))
                .thenThrow(new ReservationNotFoundException(nonExistentReservationId));

        assertThrows(ReservationNotFoundException.class,
                () -> reservationsService.getReservation(nonExistentReservationId));
    }

    @Test
    void testGetReservations() {
        List<Reservation> expectedReservations = new ArrayList<>();
        expectedReservations.add(getReservation(1L));
        expectedReservations.add(getReservation(2L));

        when(reservationsRepository.findAll()).thenReturn(expectedReservations);

        List<Reservation> actualReservations = reservationsService.getReservations();
        assertEquals(expectedReservations, actualReservations);
    }

    @Test
    void testCreateReservation() {
        Reservation newReservation = getReservation(3L);
        when(reservationsRepository.saveAndFlush(newReservation)).thenReturn(newReservation);

        String response = reservationsService.makeReservation(newReservation);
        assertEquals("Reservation successfully created with ID: 3", response);
    }

    @Test
    void testDeleteReservation() {
        Long ReservationId = 4L;

        when(reservationsRepository.findById(ReservationId))
                .thenReturn(Optional.of(Reservation.builder().id(ReservationId).build()));
        String result = reservationsService.deleteReservation(ReservationId);

        assertEquals("Reservation successfully deleted with ID: 4", result);
    }

    @Test
    void testDeleteNonExistingReservation() {
        Long ReservationId = 4L;

        when(reservationsRepository.findById(ReservationId)).thenReturn(Optional.empty());

        assertThrows(ReservationNotFoundException.class,
                () -> reservationsService.deleteReservation(ReservationId));
    }
}