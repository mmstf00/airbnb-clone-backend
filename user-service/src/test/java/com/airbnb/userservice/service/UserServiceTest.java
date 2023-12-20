package com.airbnb.userservice.service;

import com.airbnb.userservice.entity.User;
import com.airbnb.userservice.exception.UserNotFoundException;
import com.airbnb.userservice.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.airbnb.userservice.util.UserTestingUtil.getUser;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize the annotated mocks
        userService = new UserService(userRepository);
    }

    @Test
    void testGetUser() {
        Long userId = 1L;
        User expecteduser = getUser(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(expecteduser));

        User actualuser = userService.getUser(userId);
        assertEquals(expecteduser, actualuser);
    }

    @Test
    void testGetUserWhenNotExists() {
        Long nonExistentUserId = 2L;
        when(userRepository.findById(nonExistentUserId))
                .thenThrow(new UserNotFoundException(nonExistentUserId));

        assertThrows(UserNotFoundException.class,
                () -> userService.getUser(nonExistentUserId));
    }

    @Test
    void testGetUsers() {
        List<User> expectedUsers = new ArrayList<>();
        expectedUsers.add(getUser(1L));
        expectedUsers.add(getUser(2L));

        when(userRepository.findAll()).thenReturn(expectedUsers);

        List<User> actualUsers = userService.getUsers();
        assertEquals(expectedUsers, actualUsers);
    }

    @Test
    void testCreateUser() {
        User newUser = getUser(3L);
        when(userRepository.saveAndFlush(newUser)).thenReturn(newUser);

        String response = userService.createUser(newUser);
        assertEquals("User successfully created with ID: 3", response);
    }

    @Test
    void testDeleteUser() {
        Long userId = 4L;

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(User.builder().id(userId).build()));
        String result = userService.deleteUser(userId);

        assertEquals("User successfully deleted with ID: 4", result);
    }

    @Test
    void testDeleteNonExistingUser() {
        Long userId = 4L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> userService.deleteUser(userId));
    }
}