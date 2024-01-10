package com.airbnb.listingservice.service;

import com.airbnb.listingservice.entity.Listing;
import com.airbnb.listingservice.exception.ListingNotFoundException;
import com.airbnb.listingservice.repository.ListingRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListingService {

    private final ListingRepository listingRepository;

    public ListingService(ListingRepository listingRepository) {
        this.listingRepository = listingRepository;
    }

    public Listing getListing(Long id) {
        return listingRepository.findById(id)
                .orElseThrow(() -> new ListingNotFoundException(id));
    }

    public List<Listing> getListings() {
        return listingRepository.findAll();
    }

    public String createListing(Listing listing) {
        Long listingId = listingRepository.saveAndFlush(listing).getId();
        return "Listing successfully created with ID: " + listingId;
    }

    public String deleteListing(Long id) {
        Listing listing = listingRepository.findById(id)
                .orElseThrow(() -> new ListingNotFoundException(id));
        listingRepository.deleteById(listing.getId());
        return "Listing successfully deleted with ID: " + listing.getId();
    }
}
