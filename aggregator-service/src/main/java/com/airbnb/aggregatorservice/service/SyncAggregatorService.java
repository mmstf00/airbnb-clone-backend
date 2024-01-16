package com.airbnb.aggregatorservice.service;

import com.airbnb.aggregatorservice.client.ListingDTO;
import com.airbnb.aggregatorservice.handler.GrpcCallHandler;
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
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.airbnb.aggregatorservice.util.GRPCMapper.getParsedResponse;

@Service
public class SyncAggregatorService implements AggregatorService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SyncAggregatorService.class);

    @GrpcClient("user-service")
    private UserServiceGrpc.UserServiceBlockingStub userStub;

    @GrpcClient("listing-service")
    private ListingServiceGrpc.ListingServiceBlockingStub listingStub;

    @GrpcClient("reservation-service")
    private ReservationServiceGrpc.ReservationServiceBlockingStub reservationStub;

    /**
     * Makes synchronous request to corresponding microservices based on user need.
     * <br>
     * The Synchronous way, waits for each microservice to return the response, before continuing
     * with the next request.
     *
     * @return Aggregated response containing all required data for the client.
     */
    @Override
    public AggregatedResponse processRequest(Request request) {
        // Make custom Microservice calls, based on user request
        UserMessage user = getUser(request.getUserId());
        ListingMessage listing = getListing(request.getListingId());
        ReservationMessage reservation = getReservation(request.getReservationId());

        LOGGER.info("Successfully generated aggregated sync response");
        // Implement custom logic to aggregate the response and send to client
        // For example return list of reservations which specific user has
        return getParsedResponse(user, listing, reservation);
    }

    @Override
    public FavoriteListingsResponse getFavoriteListingsForUser(long userId) {
        List<ListingDTO> favoriteListings = getUser(userId).getFavoritesList().stream()
                .map(this::getListing)
                .map(GRPCMapper::toListingDTO)
                .toList();
        return new FavoriteListingsResponse(favoriteListings);
    }

    private UserMessage getUser(long userId) {
        return GrpcCallHandler.handleSync("User", () ->
                userStub.getUser(GetUserRequest.newBuilder()
                        .setId(userId)
                        .build()
                ));
    }

    private ListingMessage getListing(long listingId) {
        return GrpcCallHandler.handleSync("Listing", () ->
                listingStub.getListing(GetListingRequest.newBuilder()
                        .setId(listingId)
                        .build()
                ));
    }

    private ReservationMessage getReservation(long registrationId) {
        return GrpcCallHandler.handleSync("Reservation", () ->
                reservationStub.getReservation(GetReservationRequest.newBuilder()
                        .setId(registrationId)
                        .build()
                ));
    }
}
