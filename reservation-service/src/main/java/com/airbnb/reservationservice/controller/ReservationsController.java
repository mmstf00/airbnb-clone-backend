package com.airbnb.reservationservice.controller;

import com.airbnb.reservationservice.entity.Reservation;
import com.airbnb.reservationservice.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/reservations")
public class ReservationsController {

    private final ReservationService reservationService;

    @GetMapping
    public List<Reservation> getAll() {
        return reservationService.getReservations();
    }

    @GetMapping("/{id}")
    public Reservation getById(@PathVariable Long id) {
        return reservationService.getReservation(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String makeReservation(@RequestBody Reservation reservation) {
        return reservationService.makeReservation(reservation);
    }

    @DeleteMapping("/{id}")
    public String deleteReservation(@PathVariable Long id) {
        return reservationService.deleteReservation(id);
    }
}
