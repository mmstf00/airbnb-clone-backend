package com.airbnb.listingservice.util;

import com.airbnb.listingservice.entity.Listing;
import com.airbnb.proto.listing.ListingMessage;
import com.google.protobuf.Timestamp;

import java.time.ZoneOffset;

public class Mapper {

    private Mapper() {
    }

    public static ListingMessage entityToGrpcMessage(Listing listing) {
        return ListingMessage.newBuilder()
                .setId(listing.getId())
                .setTitle(listing.getTitle())
                .setDescription(listing.getDescription())
                .setImageSrc(listing.getImageSrc())
                .setCategory(listing.getCategory())
                .setPrice(listing.getPrice().toString())
                .setCreatedAt(Timestamp.newBuilder()
                        .setSeconds(listing.getCreatedAt().toEpochSecond(ZoneOffset.UTC)).build())
                .setRoomCount(listing.getRoomCount())
                .setBathroomCount(listing.getBathroomCount())
                .setGuestCount(listing.getGuestCount())
                .setLocationValue(listing.getLocationValue())
                .addAllReservationIds(listing.getReservationIds())
                .setUserId(listing.getUserId())
                .build();
    }
}
