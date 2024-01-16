package com.airbnb.aggregatorservice.controller;

import com.airbnb.aggregatorservice.model.request.Request;
import com.airbnb.aggregatorservice.model.response.AggregatedResponse;
import com.airbnb.aggregatorservice.model.response.FavoriteListingsResponse;
import com.airbnb.aggregatorservice.service.AggregatorService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/aggregator")
public class SyncAggregatorServiceController {

    private final AggregatorService aggregatorService;

    public SyncAggregatorServiceController(@Qualifier("syncAggregatorService") AggregatorService aggregatorService) {
        this.aggregatorService = aggregatorService;
    }

    @GetMapping
    public ResponseEntity<AggregatedResponse> getAllDataSync(@RequestBody Request request) {
        return ResponseEntity.ok(aggregatorService.processRequest(request));
    }

    @GetMapping("/favorites/{userId}")
    public ResponseEntity<FavoriteListingsResponse> getFavorites(@PathVariable long userId) {
        return ResponseEntity.ok(aggregatorService.getFavoriteListingsForUser(userId));
    }
}
