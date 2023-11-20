package com.airbnb.reservationservice.exception;

public class ReservationNotFoundException extends RuntimeException {
    public ReservationNotFoundException(Long id) {
        super(String.format("Reservation with Id %d not found", id));
    }
}
