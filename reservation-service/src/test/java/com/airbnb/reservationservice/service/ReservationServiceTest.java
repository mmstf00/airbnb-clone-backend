package com.airbnb.reservationservice.service;

import com.airbnb.reservationservice.entity.Reservation;
import com.airbnb.reservationservice.exception.ReservationNotFoundException;
import com.airbnb.reservationservice.repository.ReservationRepository;
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

class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    private ReservationService reservationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize the annotated mocks
        reservationService = new ReservationService(reservationRepository);
    }

    @Test
    void testGetReservation() {
        Long reservationId = 1L;
        Reservation expectedReservation = getReservation(reservationId);
        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(expectedReservation));

        Reservation actualReservation = reservationService.getReservation(reservationId);
        assertEquals(expectedReservation, actualReservation);
    }

    @Test
    void testGetReservationWhenNotExists() {
        Long nonExistentReservationId = 2L;
        when(reservationRepository.findById(nonExistentReservationId))
                .thenThrow(new ReservationNotFoundException(nonExistentReservationId));

        assertThrows(ReservationNotFoundException.class,
                () -> reservationService.getReservation(nonExistentReservationId));
    }

    @Test
    void testGetReservations() {
        List<Reservation> expectedReservations = new ArrayList<>();
        expectedReservations.add(getReservation(1L));
        expectedReservations.add(getReservation(2L));

        when(reservationRepository.findAll()).thenReturn(expectedReservations);

        List<Reservation> actualReservations = reservationService.getReservations();
        assertEquals(expectedReservations, actualReservations);
    }

    @Test
    void testCreateReservation() {
        Reservation newReservation = getReservation(3L);
        when(reservationRepository.saveAndFlush(newReservation)).thenReturn(newReservation);

        String response = reservationService.makeReservation(newReservation);
        assertEquals("Reservation successfully created with ID: 3", response);
    }

    @Test
    void testDeleteReservation() {
        Long ReservationId = 4L;

        when(reservationRepository.findById(ReservationId))
                .thenReturn(Optional.of(Reservation.builder().id(ReservationId).build()));
        String result = reservationService.deleteReservation(ReservationId);

        assertEquals("Reservation successfully deleted with ID: 4", result);
    }

    @Test
    void testDeleteNonExistingReservation() {
        Long ReservationId = 4L;

        when(reservationRepository.findById(ReservationId)).thenReturn(Optional.empty());

        assertThrows(ReservationNotFoundException.class,
                () -> reservationService.deleteReservation(ReservationId));
    }
}