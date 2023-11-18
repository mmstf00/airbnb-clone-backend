package com.airbnb.listingservice.exception;

public class ListingNotFoundException extends RuntimeException {
    public ListingNotFoundException(Long id) {
        super(String.format("Listing with Id %d not found", id));
    }
}
