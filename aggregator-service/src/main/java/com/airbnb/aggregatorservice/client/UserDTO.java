package com.airbnb.aggregatorservice.client;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@Setter
public class UserDTO {
    private Long id;
    private String name;
    private String email;
    private LocalDateTime emailVerifiedAt;
    private String imageSrc;
    private String hashedPassword;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private List<Long> favorites;
    private List<Long> listings;
    private List<Long> reservations;

    @JsonManagedReference
    private List<AccountDTO> accounts;
}
