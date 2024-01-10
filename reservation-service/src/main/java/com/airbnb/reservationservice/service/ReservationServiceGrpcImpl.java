package com.airbnb.reservationservice.service;

import com.airbnb.proto.reservations.GetReservationRequest;
import com.airbnb.proto.reservations.ReservationMessage;
import com.airbnb.proto.reservations.ReservationServiceGrpc;
import com.airbnb.reservationservice.entity.Reservation;
import com.airbnb.reservationservice.exception.ReservationNotFoundException;
import com.airbnb.reservationservice.repository.ReservationRepository;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.airbnb.reservationservice.util.Mapper;

@GrpcService
@RequiredArgsConstructor
public class ReservationServiceGrpcImpl extends ReservationServiceGrpc.ReservationServiceImplBase {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReservationServiceGrpcImpl.class);
    private final ReservationRepository reservationRepository;

    @Override
    public void getReservation(GetReservationRequest request, StreamObserver<ReservationMessage> responseObserver) {
        LOGGER.info("gRPC call received: {}", request.getId());

        Reservation reservation = reservationRepository.findById(request.getId())
                .orElseThrow(() -> new ReservationNotFoundException(request.getId()));

        responseObserver.onNext(Mapper.entityToGrpcMessage(reservation));
        responseObserver.onCompleted();
    }
}
