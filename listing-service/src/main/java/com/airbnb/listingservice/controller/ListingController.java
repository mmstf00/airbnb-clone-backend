package com.airbnb.listingservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/listing")
public class ListingController {

    @GetMapping
    public String getAll() {
        return "Listing1, Listing2";
    }
}
