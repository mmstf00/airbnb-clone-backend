package com.airbnb.reservationservice.listener;

import com.airbnb.reservationservice.event.ReservationCompletedEvent;
import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReservationCompletedListener {

    private final KafkaTemplate<String, ReservationCompletedEvent> kafkaTemplate;
    private final ObservationRegistry observationRegistry;

    @EventListener
    public void handleReservationEvent(ReservationCompletedEvent event) {
        log.info("Reservation Completed Event Received, Sending ReservationCompletedListener to notificationTopic: {}", event.getReservationId());

        // Create Observation for Kafka Template
        try {
            Observation.createNotStarted("notification-topic", observationRegistry).observeChecked(() -> {
                CompletableFuture<SendResult<String, ReservationCompletedEvent>> future = kafkaTemplate.send("notificationTopic",
                        new ReservationCompletedEvent(event.getReservationId()));
                return future.handle((result, throwable) -> CompletableFuture.completedFuture(result));
            }).get();
        } catch (InterruptedException | ExecutionException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Error while sending message to Kafka", e);
        }
    }
}
