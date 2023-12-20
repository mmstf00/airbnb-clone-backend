package com.airbnb.userservice.controller;

import com.airbnb.userservice.entity.User;
import com.airbnb.userservice.exception.UserNotFoundException;
import com.airbnb.userservice.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;

import static com.airbnb.userservice.util.UserTestingUtil.getUser;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService usersService;

    @Autowired
    private ObjectMapper objectMapper;

    private final String API_ENDPOINT = "/api/v1/";

    @Test
    void testGetAllUsers() throws Exception {
        when(usersService.getUsers()).thenReturn(
                Arrays.asList(
                        getUser(1L, "test_email1@gmail.com"),
                        getUser(2L, "test_email2@gmail.com")
                )
        );

        mockMvc.perform(MockMvcRequestBuilders.get(API_ENDPOINT + "/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].email").value("test_email1@gmail.com"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].email").value("test_email2@gmail.com"));
    }

    @Test
    void testGetUserById() throws Exception {
        when(usersService.getUser(1L))
                .thenReturn(getUser(1L, "test_email1@gmail.com"));
        mockMvc.perform(MockMvcRequestBuilders.get(API_ENDPOINT + "/users/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("test_email1@gmail.com"));

        when(usersService.getUser(2L))
                .thenReturn(getUser(2L, "test_email2@gmail.com"));
        mockMvc.perform(MockMvcRequestBuilders.get(API_ENDPOINT + "/users/2"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.email").value("test_email2@gmail.com"));

        when(usersService.getUser(3L))
                .thenThrow(new UserNotFoundException(3L));
        mockMvc.perform(MockMvcRequestBuilders.get(API_ENDPOINT + "/users/3"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateUser() throws Exception {
        User newuser = getUser(3L, "test_email3@gmail.com");
        String successResponse = "User successfully created with ID: 3";

        when(usersService.createUser(any(User.class))).thenReturn(successResponse);
        mockMvc.perform(MockMvcRequestBuilders.post(API_ENDPOINT + "/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newuser)))
                .andExpect(status().isCreated())
                .andExpect(content().string("User successfully created with ID: 3"));
    }

    @Test
    void testDeleteUser() throws Exception {
        String successResponse = "User successfully deleted with ID: 4";

        when(usersService.deleteUser(4L)).thenReturn(successResponse);
        mockMvc.perform(MockMvcRequestBuilders.delete(API_ENDPOINT + "/users/4"))
                .andExpect(status().isOk())
                .andExpect(content().string("User successfully deleted with ID: 4"));

        // Verify that the deleteUser method was called
        verify(usersService, times(1)).deleteUser(4L);
    }

    @Test
    void testDeleteNonExistingUser() throws Exception {
        when(usersService.deleteUser(45L))
                .thenThrow(new UserNotFoundException(45L));

        mockMvc.perform(MockMvcRequestBuilders.delete(API_ENDPOINT + "/users/45"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.message").value("User not found"));

        // Verify that the deleteUser method was called
        verify(usersService, times(1)).deleteUser(45L);
    }
}