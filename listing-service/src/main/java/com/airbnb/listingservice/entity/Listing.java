package com.airbnb.listingservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Listing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    private String imageSrc;
    private String category;
    private BigDecimal price;
    @CreationTimestamp
    private LocalDateTime createdAt;
    private int roomCount;
    private int bathroomCount;
    private int guestCount;
    private int locationValue;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<Long> reservationIds = new ArrayList<>();
    private Long userId;
}
