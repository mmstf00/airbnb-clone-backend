package com.airbnb.notificationservice.service;

import com.airbnb.notificationservice.event.ReservationCompletedEvent;
import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import io.micrometer.tracing.Tracer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationService.class);
    private final Tracer tracer;
    private final ObservationRegistry observationRegistry;

    public NotificationService(Tracer tracer, ObservationRegistry observationRegistry) {
        this.tracer = tracer;
        this.observationRegistry = observationRegistry;
    }

    @KafkaListener(topics = "notificationTopic")
    public void handleNotification(ReservationCompletedEvent reservationCompletedEvent) {
        Observation.createNotStarted("on-message", observationRegistry).observe(() -> {
            LOGGER.info("Got message <{}>", reservationCompletedEvent);
            LOGGER.info("TraceId- {}, Received Notification for Reservation - {}",
                    tracer.currentSpan().context().traceId(),
                    reservationCompletedEvent.getReservationId());
        });
        // send out an email notification
    }
}
