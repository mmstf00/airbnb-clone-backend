package com.airbnb.userservice.util;

import com.airbnb.proto.users.AccountMessage;
import com.airbnb.proto.users.UserMessage;
import com.airbnb.userservice.entity.Account;
import com.airbnb.userservice.entity.User;
import com.google.protobuf.Timestamp;

import java.time.ZoneOffset;
import java.util.List;

public class Mapper {

    private Mapper() {
    }

    public static UserMessage entityToGpcMessage(User user) {
        return UserMessage.newBuilder()
                .setId(user.getId())
                .setName(user.getName())
                .setEmail(user.getEmail())
                .setEmailVerifiedAt(Timestamp.newBuilder()
                        .setSeconds(user.getEmailVerifiedAt().toEpochSecond(ZoneOffset.UTC)).build())
                .setImageSrc(user.getImageSrc())
                .setHashedPassword(user.getHashedPassword())
                .setCreatedAt(Timestamp.newBuilder()
                        .setSeconds(user.getCreatedAt().toEpochSecond(ZoneOffset.UTC)).build())
                .setUpdatedAt(Timestamp.newBuilder()
                        .setSeconds(user.getUpdatedAt().toEpochSecond(ZoneOffset.UTC)).build())
                .addAllFavorites(user.getFavorites())
                .addAllListings(user.getListings())
                .addAllReservations(user.getReservations())
                .addAllAccounts(addAccountsToUserMessage(user.getAccounts()))
                .build();
    }

    public static List<AccountMessage> addAccountsToUserMessage(List<Account> accounts) {
        return accounts.stream()
                .map(Mapper::accountEntityToMessage)
                .toList();
    }

    public static AccountMessage accountEntityToMessage(Account accountEntity) {
        return AccountMessage.newBuilder()
                .setId(accountEntity.getId())
                .setType(accountEntity.getType())
                .setProvider(accountEntity.getProvider())
                .setProviderAccountId(accountEntity.getProviderAccountId())
                .setRefreshToken(accountEntity.getRefreshToken())
                .setAccessToken(accountEntity.getAccessToken())
                .setTokenType(accountEntity.getTokenType())
                .setScope(accountEntity.getScope())
                .setIdToken(accountEntity.getIdToken())
                .setSessionState(accountEntity.getSessionState())
                .setExpiresAt(accountEntity.getExpiresAt())
                .build();
    }
}
