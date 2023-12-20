package com.airbnb.userservice.service;

import com.airbnb.userservice.entity.User;
import com.airbnb.userservice.exception.UserNotFoundException;
import com.airbnb.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User getUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public String createUser(User user) {
        Long userId = userRepository.saveAndFlush(user).getId();
        return "User successfully created with ID: " + userId;
    }

    public String deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        userRepository.deleteById(user.getId());
        return "User successfully deleted with ID: " + user.getId();
    }

}
