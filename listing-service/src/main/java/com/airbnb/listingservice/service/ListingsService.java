package com.airbnb.listingservice.service;

import com.airbnb.listingservice.entity.Listing;
import com.airbnb.listingservice.exception.ListingNotFoundException;
import com.airbnb.listingservice.repository.ListingsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ListingsService {

    private final ListingsRepository listingsRepository;

    public ListingsService(ListingsRepository listingsRepository) {
        this.listingsRepository = listingsRepository;
    }

    public Listing getListing(Long id) {
        return listingsRepository.findById(id)
                .orElseThrow(() -> new ListingNotFoundException(id));
    }

    public List<Listing> getListings() {
        return listingsRepository.findAll();
    }

    public String createListing(Listing listing) {
        Long listingId = listingsRepository.saveAndFlush(listing).getId();
        return "Listing successfully created with ID: " + listingId;
    }

    public String deleteListing(Long id) {
        Listing listing = listingsRepository.findById(id)
                .orElseThrow(() -> new ListingNotFoundException(id));
        listingsRepository.deleteById(listing.getId());
        return "Listing successfully deleted with ID: " + listing.getId();
    }
}
