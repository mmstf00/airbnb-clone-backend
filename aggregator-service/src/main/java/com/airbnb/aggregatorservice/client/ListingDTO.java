package com.airbnb.aggregatorservice.client;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@Setter
@EqualsAndHashCode
public class ListingDTO {
    private Long id;
    private String title;
    private String description;
    private String imageSrc;
    private String category;
    private BigDecimal price;
    private LocalDateTime createdAt;
    private int roomCount;
    private int bathroomCount;
    private int guestCount;
    private int locationValue;
    private List<Long> reservationIds;
    private Long userId;
}
