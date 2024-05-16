package com.airbnb.reservationservice.event;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class ReservationCompletedEvent extends ApplicationEvent {
    private String reservationId;

    public ReservationCompletedEvent(Object source, String reservationId) {
        super(source);
        this.reservationId = reservationId;
    }

    public ReservationCompletedEvent(String reservationId) {
        super(reservationId);
        this.reservationId = reservationId;
    }
}
