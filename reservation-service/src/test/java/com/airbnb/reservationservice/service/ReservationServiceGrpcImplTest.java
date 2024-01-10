package com.airbnb.reservationservice.service;

import com.airbnb.proto.reservations.GetReservationRequest;
import com.airbnb.proto.reservations.ReservationMessage;
import com.airbnb.reservationservice.entity.Reservation;
import com.airbnb.reservationservice.exception.ReservationNotFoundException;
import com.airbnb.reservationservice.repository.ReservationRepository;
import com.airbnb.reservationservice.util.ReservationsTestingUtil;
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

class ReservationServiceGrpcImplTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private StreamObserver<ReservationMessage> responseObserver;

    @InjectMocks
    private ReservationServiceGrpcImpl reservationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetReservation() {
        // Arrange
        long reservationId = 1L;

        GetReservationRequest request = GetReservationRequest.newBuilder().setId(reservationId).build();

        Reservation reservation = ReservationsTestingUtil.getReservation(reservationId);

        when(reservationRepository.findById(anyLong())).thenReturn(Optional.of(reservation));

        // Act
        reservationService.getReservation(request, responseObserver);

        // Assert
        verify(reservationRepository).findById(reservationId);
        verify(responseObserver).onNext(any(ReservationMessage.class));
        verify(responseObserver).onCompleted();
        verify(responseObserver, never()).onError(any());
    }

    @Test
    void testGetReservationNotFound() {
        // Arrange
        long reservationId = 2L;
        GetReservationRequest request = GetReservationRequest.newBuilder().setId(reservationId).build();

        when(reservationRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Assert
        assertThrows(ReservationNotFoundException.class,
                () -> reservationService.getReservation(request, responseObserver));
    }
}