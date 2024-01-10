package com.airbnb.reservationservice.util;

import com.airbnb.proto.reservations.ReservationMessage;
import com.airbnb.reservationservice.entity.Reservation;
import com.google.protobuf.Timestamp;

import java.time.ZoneOffset;

public class Mapper {

    private Mapper() {
    }

    public static ReservationMessage entityToGrpcMessage(Reservation reservation) {
        return ReservationMessage.newBuilder()
                .setId(reservation.getId())
                .setCreatedAt(Timestamp.newBuilder()
                        .setSeconds(reservation.getCreatedAt().toEpochSecond(ZoneOffset.UTC)).build())
                .setStartDate(Timestamp.newBuilder()
                        .setSeconds(reservation.getStartDate().toEpochSecond(ZoneOffset.UTC)).build())
                .setEndDate(Timestamp.newBuilder()
                        .setSeconds(reservation.getEndDate().toEpochSecond(ZoneOffset.UTC)).build())
                .setTotalPrice(reservation.getTotalPrice().toString())
                .setListingId(reservation.getListingId())
                .setUserId(reservation.getUserId())
                .build();
    }
}
