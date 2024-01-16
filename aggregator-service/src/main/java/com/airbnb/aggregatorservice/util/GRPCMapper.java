package com.airbnb.aggregatorservice.util;

import com.airbnb.aggregatorservice.client.AccountDTO;
import com.airbnb.aggregatorservice.client.ListingDTO;
import com.airbnb.aggregatorservice.client.ReservationDTO;
import com.airbnb.aggregatorservice.client.UserDTO;
import com.airbnb.aggregatorservice.model.response.AggregatedResponse;
import com.airbnb.proto.listing.ListingMessage;
import com.airbnb.proto.reservations.ReservationMessage;
import com.airbnb.proto.users.AccountMessage;
import com.airbnb.proto.users.UserMessage;
import com.google.protobuf.Timestamp;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * Utility class used for mapping gRPC message to Java DTO.
 */
public class GRPCMapper {

    private GRPCMapper() {
        throw new UnsupportedOperationException("Cannot create instance of utility class");
    }

    public static UserDTO toUserDto(UserMessage message) {
        return UserDTO.builder()
                .id(message.getId())
                .name(message.getName())
                .email(message.getEmail())
                .emailVerifiedAt(getLocalDateTime(message.getEmailVerifiedAt()))
                .imageSrc(message.getImageSrc())
                .hashedPassword(message.getHashedPassword())
                .createdAt(getLocalDateTime(message.getCreatedAt()))
                .updatedAt(getLocalDateTime(message.getUpdatedAt()))
                .favorites(message.getFavoritesList())
                .listings(message.getListingsList())
                .reservations(message.getReservationsList())
                .accounts(message.getAccountsList()
                        .stream()
                        .map(GRPCMapper::toAccountDto)
                        .toList())
                .build();
    }

    public static AccountDTO toAccountDto(AccountMessage message) {
        return AccountDTO.builder()
                .id(message.getId())
                .type(message.getType())
                .provider(message.getProvider())
                .providerAccountId(message.getProviderAccountId())
                .refreshToken(message.getRefreshToken())
                .accessToken(message.getAccessToken())
                .tokenType(message.getTokenType())
                .scope(message.getScope())
                .idToken(message.getIdToken())
                .sessionState(message.getSessionState())
                .expiresAt(message.getExpiresAt())
                .user(toUserDto(message.getUser()))
                .build();
    }

    public static ReservationDTO toReservationDTO(ReservationMessage message) {
        return ReservationDTO.builder()
                .id(message.getId())
                .createdAt(getLocalDateTime(message.getCreatedAt()))
                .startDate(getLocalDateTime(message.getStartDate()))
                .endDate(getLocalDateTime(message.getEndDate()))
                .totalPrice(new BigDecimal(message.getTotalPrice()))
                .listingId(message.getListingId())
                .userId(message.getUserId())
                .build();
    }

    public static ListingDTO toListingDTO(ListingMessage message) {
        return ListingDTO.builder()
                .id(message.getId())
                .title(message.getTitle())
                .description(message.getDescription())
                .imageSrc(message.getImageSrc())
                .category(message.getCategory())
                .price(new BigDecimal(message.getPrice()))
                .createdAt(getLocalDateTime(message.getCreatedAt()))
                .roomCount(message.getRoomCount())
                .bathroomCount(message.getBathroomCount())
                .guestCount(message.getGuestCount())
                .locationValue(message.getLocationValue())
                .reservationIds(message.getReservationIdsList())
                .userId(message.getUserId())
                .build();
    }

    public static AggregatedResponse getParsedResponse(UserMessage user, ListingMessage listing,
                                                       ReservationMessage reservation) {
        return new AggregatedResponse(
                toUserDto(user),
                toListingDTO(listing),
                toReservationDTO(reservation)
        );
    }

    /**
     * @param timestamp The gRPC Timestamp
     * @return LocalDateTime from gRPC Timestamp
     */
    private static LocalDateTime getLocalDateTime(Timestamp timestamp) {
        Instant instant = Instant.ofEpochSecond(timestamp.getSeconds(), timestamp.getNanos());
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }
}
