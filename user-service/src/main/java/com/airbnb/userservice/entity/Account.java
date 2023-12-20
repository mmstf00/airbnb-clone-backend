package com.airbnb.userservice.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String type;
    private String provider;
    private String providerAccountId;
    private String refreshToken;
    private String accessToken;
    private String tokenType;
    private String scope;
    private String idToken;
    private String sessionState;
    private int expiresAt;

    @ManyToOne
    @JsonBackReference
    private User user;

}
