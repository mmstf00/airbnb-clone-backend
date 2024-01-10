package com.airbnb.listingservice.service;

import com.airbnb.listingservice.entity.Listing;
import com.airbnb.listingservice.exception.ListingNotFoundException;
import com.airbnb.listingservice.repository.ListingRepository;
import com.airbnb.listingservice.util.Mapper;
import com.airbnb.proto.listing.GetListingRequest;
import com.airbnb.proto.listing.ListingMessage;
import com.airbnb.proto.listing.ListingServiceGrpc;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@GrpcService
@RequiredArgsConstructor
public class ListingServiceGrpcImpl extends ListingServiceGrpc.ListingServiceImplBase {

    private static final Logger LOGGER = LoggerFactory.getLogger(ListingServiceGrpcImpl.class);
    private final ListingRepository listingRepository;

    @Override
    public void getListing(GetListingRequest request, StreamObserver<ListingMessage> responseObserver) {
        LOGGER.info("gRPC call received: {}", request.getId());

        Listing listing = listingRepository.findById(request.getId())
                .orElseThrow(() -> new ListingNotFoundException(request.getId()));

        responseObserver.onNext(Mapper.entityToGrpcMessage(listing));
        responseObserver.onCompleted();
    }

}
