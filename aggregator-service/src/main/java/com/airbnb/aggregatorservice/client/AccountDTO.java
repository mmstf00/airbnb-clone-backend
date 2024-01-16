package com.airbnb.aggregatorservice.client;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class AccountDTO {
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

    @JsonBackReference
    private UserDTO user;
}
