package com.airbnb.userservice.service;

import com.airbnb.proto.users.GetUserRequest;
import com.airbnb.proto.users.UserMessage;
import com.airbnb.proto.users.UserServiceGrpc;
import com.airbnb.userservice.entity.User;
import com.airbnb.userservice.exception.UserNotFoundException;
import com.airbnb.userservice.repository.UserRepository;
import com.airbnb.userservice.util.Mapper;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@GrpcService
@RequiredArgsConstructor
public class UserServiceGrpcImpl extends UserServiceGrpc.UserServiceImplBase {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceGrpcImpl.class);
    private final UserRepository userRepository;

    @Override
    public void getUser(GetUserRequest request, StreamObserver<UserMessage> responseObserver) {
        LOGGER.info("gRPC call received: {}", request.getId());

        User user = userRepository.findById(request.getId())
                .orElseThrow(() -> new UserNotFoundException(request.getId()));

        responseObserver.onNext(Mapper.entityToGpcMessage(user));
        responseObserver.onCompleted();
    }
}
