package com.airbnb.aggregatorservice.service;

import com.airbnb.aggregatorservice.client.ListingDTO;
import com.airbnb.aggregatorservice.model.request.Request;
import com.airbnb.aggregatorservice.model.response.AggregatedResponse;
import com.airbnb.aggregatorservice.model.response.FavoriteListingsResponse;
import com.airbnb.aggregatorservice.util.GRPCMapper;
import com.airbnb.proto.listing.GetListingRequest;
import com.airbnb.proto.listing.ListingMessage;
import com.airbnb.proto.listing.ListingServiceGrpc;
import com.airbnb.proto.reservations.GetReservationRequest;
import com.airbnb.proto.reservations.ReservationMessage;
import com.airbnb.proto.reservations.ReservationServiceGrpc;
import com.airbnb.proto.users.GetUserRequest;
import com.airbnb.proto.users.UserMessage;
import com.airbnb.proto.users.UserServiceGrpc;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

import static com.airbnb.aggregatorservice.handler.GrpcCallHandler.handleAsync;
import static com.airbnb.aggregatorservice.util.GRPCMapper.getParsedResponse;

@Service
@Setter
@RequiredArgsConstructor
public class AsyncAggregatorService implements AggregatorService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncAggregatorService.class);

    @GrpcClient("user-service")
    private UserServiceGrpc.UserServiceBlockingStub userStub;

    @GrpcClient("listing-service")
    private ListingServiceGrpc.ListingServiceBlockingStub listingStub;

    @GrpcClient("reservation-service")
    private ReservationServiceGrpc.ReservationServiceBlockingStub reservationStub;

    private final ExecutorService executorService;

    /**
     * Asynchronously processes a request by fetching information from the microservices.
     * <br>
     * Because all requests are made in separate Threads, the response time is 2x faster than
     * the {@link SyncAggregatorService}
     *
     * @param request The input request with the requested data.
     * @return AggregatedResponse upon completion.
     */
    @Override
    public AggregatedResponse processRequest(Request request) {
        CompletableFuture<UserMessage> user = getUser(request.getUserId());
        CompletableFuture<ListingMessage> listing = getListing(request.getListingId());
        CompletableFuture<ReservationMessage> reservation = getReservation(request.getReservationId());

        LOGGER.info("Successfully generated aggregated async response");
        // Implement custom logic to aggregate the response and send to client
        return CompletableFuture.allOf(user, listing, reservation)
                .thenApply(v -> getParsedResponse(user.join(), listing.join(), reservation.join()))
                .join();
    }

    /**
     * Retrieves the favorite listings for a given user asynchronously.
     *
     * @param userId the ID of the user for whom to retrieve favorite listings
     * @return a {@link FavoriteListingsResponse} containing the list of favorite {@link ListingDTO}s
     */
    @Override
    public FavoriteListingsResponse getFavoriteListingsForUser(long userId) {
        // Retrieve the CompletableFuture list of ListingDTO for each favorite listing asynchronously
        List<CompletableFuture<ListingDTO>> listingFutures = getUser(userId)
                .thenApplyAsync(user -> user.getFavoritesList().stream()
                        .map(listingId -> CompletableFuture.supplyAsync(() -> getListing(listingId).join(), executorService)
                                .thenApply(GRPCMapper::toListingDTO))
                        .toList(), executorService).join();

        // Wait for all CompletableFuture to complete and collect the results into a list of ListingDTO
        List<ListingDTO> favoriteListings = listingFutures.stream()
                .map(CompletableFuture::join)
                .toList();

        return new FavoriteListingsResponse(favoriteListings);
    }

    private CompletableFuture<UserMessage> getUser(long userId) {
        return CompletableFuture.supplyAsync(() ->
                        userStub.getUser(GetUserRequest.newBuilder()
                                .setId(userId)
                                .build()), executorService)
                .handle((result, throwable) -> handleAsync(result, throwable, "User"));
    }

    private CompletableFuture<ListingMessage> getListing(long listingId) {
        return CompletableFuture.supplyAsync(() ->
                        listingStub.getListing(GetListingRequest.newBuilder()
                                .setId(listingId)
                                .build()), executorService)
                .handle((result, throwable) -> handleAsync(result, throwable, "Listing"));
    }

    private CompletableFuture<ReservationMessage> getReservation(long registrationId) {
        return CompletableFuture.supplyAsync(() ->
                        reservationStub.getReservation(GetReservationRequest.newBuilder()
                                .setId(registrationId)
                                .build()), executorService)
                .handle((result, throwable) -> handleAsync(result, throwable, "Reservation"));
    }

}
