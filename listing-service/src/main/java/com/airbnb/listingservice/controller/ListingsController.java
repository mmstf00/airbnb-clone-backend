package com.airbnb.listingservice.controller;

import com.airbnb.listingservice.entity.Listing;
import com.airbnb.listingservice.service.ListingsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/listings")
public class ListingsController {

    private final ListingsService listingsService;

    @GetMapping
    public List<Listing> getAll() {
        return listingsService.getListings();
    }

    @GetMapping("/{id}")
    public Listing getById(@PathVariable Long id) {
        return listingsService.getListing(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String createListing(@RequestBody Listing listing) {
        return listingsService.createListing(listing);
    }

    @DeleteMapping("/{id}")
    public String deleteListing(@PathVariable Long id) {
        return listingsService.deleteListing(id);
    }
}
