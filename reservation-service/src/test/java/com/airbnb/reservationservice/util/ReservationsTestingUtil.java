package com.airbnb.reservationservice.util;

import com.airbnb.reservationservice.entity.Reservation;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ReservationsTestingUtil {

    public static Reservation getReservation(Long id) {
        return getReservation(id, BigDecimal.valueOf(12));
    }

    public static Reservation getReservation(Long id, BigDecimal totalPrice) {
        return Reservation.builder()
                .id(id)
                .createdAt(LocalDateTime.now())
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now())
                .totalPrice(totalPrice)
                .listingId(13L)
                .userId(1L)
                .build();
    }
}
