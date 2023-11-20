package com.airbnb.reservationservice.service;

import com.airbnb.reservationservice.entity.Reservation;
import com.airbnb.reservationservice.exception.ReservationNotFoundException;
import com.airbnb.reservationservice.repository.ReservationsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationsService {

    private final ReservationsRepository reservationsRepository;

    public Reservation getReservation(Long id) {
        return reservationsRepository.findById(id)
                .orElseThrow(() -> new ReservationNotFoundException(id));
    }

    public List<Reservation> getReservations() {
        return reservationsRepository.findAll();
    }

    public String makeReservation(Reservation reservation) {
        Long reservationId = reservationsRepository.saveAndFlush(reservation).getId();
        return "Reservation successfully created with ID: " + reservationId;
    }

    public String deleteReservation(Long id) {
        Reservation reservation = reservationsRepository.findById(id)
                .orElseThrow(() -> new ReservationNotFoundException(id));
        reservationsRepository.deleteById(reservation.getId());
        return "Reservation successfully deleted with ID: " + reservation.getId();
    }
}
