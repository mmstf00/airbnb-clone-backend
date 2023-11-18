package com.airbnb.listingservice.util;

import com.airbnb.listingservice.entity.Listing;

import java.math.BigDecimal;
import java.util.List;

public class TestingUtil {

    public static Listing getListing(Long id) {
        return getListing(id, "Sample Listing");
    }

    public static Listing getListing(Long id, String title) {
        return Listing.builder()
                .id(id)
                .title(title)
                .description("A sample description")
                .imageSrc("sample.jpg")
                .category("Sample Category")
                .price(new BigDecimal("100.00"))
                .roomCount(2)
                .bathroomCount(1)
                .guestCount(4)
                .locationValue(5)
                .userId("user123")
                .reservationIds(List.of("RES001", "RES002"))
                .build();
    }
}
