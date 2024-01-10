package com.airbnb.listingservice.controller;

import com.airbnb.listingservice.entity.Listing;
import com.airbnb.listingservice.service.ListingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/listings")
public class ListingsController {

    private final ListingService listingService;

    @GetMapping
    public List<Listing> getAll() {
        return listingService.getListings();
    }

    @GetMapping("/{id}")
    public Listing getById(@PathVariable Long id) {
        return listingService.getListing(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String createListing(@RequestBody Listing listing) {
        return listingService.createListing(listing);
    }

    @DeleteMapping("/{id}")
    public String deleteListing(@PathVariable Long id) {
        return listingService.deleteListing(id);
    }
}
