package com.airbnb.userservice.service;

import com.airbnb.proto.users.GetUserRequest;
import com.airbnb.proto.users.UserMessage;
import com.airbnb.userservice.entity.User;
import com.airbnb.userservice.exception.UserNotFoundException;
import com.airbnb.userservice.repository.UserRepository;
import com.airbnb.userservice.util.UserTestingUtil;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class UserServiceGrpcImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private StreamObserver<UserMessage> responseObserver;

    @InjectMocks
    private UserServiceGrpcImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetUser() {
        // Arrange
        long userId = 1L;

        GetUserRequest request = GetUserRequest.newBuilder().setId(userId).build();

        User user = UserTestingUtil.getUser(userId);

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        // Act
        userService.getUser(request, responseObserver);

        // Assert
        verify(userRepository).findById(userId);
        verify(responseObserver).onNext(any(UserMessage.class));
        verify(responseObserver).onCompleted();
        verify(responseObserver, never()).onError(any());
    }

    @Test
    void testGetUserNotFound() {
        // Arrange
        long userId = 2L;
        GetUserRequest request = GetUserRequest.newBuilder().setId(userId).build();

        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Assert
        assertThrows(UserNotFoundException.class,
                () -> userService.getUser(request, responseObserver));
    }
}