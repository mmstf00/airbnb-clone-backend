package com.airbnb.reservationservice.service;

import com.airbnb.reservationservice.entity.Reservation;
import com.airbnb.reservationservice.event.ReservationCompletedEvent;
import com.airbnb.reservationservice.exception.ReservationNotFoundException;
import com.airbnb.reservationservice.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    public Reservation getReservation(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new ReservationNotFoundException(id));
    }

    public List<Reservation> getReservations() {
        return reservationRepository.findAll();
    }

    public String makeReservation(Reservation reservation) {
        Long reservationId = reservationRepository.saveAndFlush(reservation).getId();
        sendNotification(reservationId);
        return "Reservation successfully created with ID: " + reservationId;
    }

    public String deleteReservation(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ReservationNotFoundException(id));
        reservationRepository.deleteById(reservation.getId());
        return "Reservation successfully deleted with ID: " + reservation.getId();
    }

    private void sendNotification(Long reservationId) {
        applicationEventPublisher.publishEvent(
                new ReservationCompletedEvent(this, Long.toString(reservationId))
        );
    }
}
