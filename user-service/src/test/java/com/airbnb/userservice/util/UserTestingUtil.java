package com.airbnb.userservice.util;

import com.airbnb.userservice.entity.Account;
import com.airbnb.userservice.entity.User;

import java.time.LocalDateTime;
import java.util.List;

public class UserTestingUtil {

    public static User getUser(Long id) {
        return getUser(id, "john.doe@example.com");
    }

    public static User getUser(Long id, String email) {
        return User.builder()
                .id(id)
                .name("John Doe")
                .email(email)
                .emailVerifiedAt(LocalDateTime.now())
                .imageSrc("cdn.image.com/images/1")
                .hashedPassword("testPassword")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now().plusHours(1))
                .favorites(List.of(1L, 2L, 3L))
                .listings(List.of(12L, 22L, 32L))
                .reservations(List.of(123L, 223L, 323L))
                .accounts(List.of(getAccount(1L), getAccount(2L)))
                .build();
    }

    public static Account getAccount(Long id) {
        return Account.builder()
                .id(id)
                .type("oauth")
                .provider("google")
                .providerAccountId("google123")
                .refreshToken("refresh_token_123")
                .accessToken("access_token_123")
                .tokenType("Bearer")
                .scope("read write")
                .idToken("id_token_123")
                .sessionState("session_state_123")
                .expiresAt(1640995200)
                .build();
    }
}
