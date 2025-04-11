package com.insurance.api.service;

import com.insurance.api.model.User;

import java.util.Optional;
import java.util.UUID;

public interface UserService {

    User createUser(User user);

    Optional<User> getUserById(UUID id);

    Optional<User> getUserByUsername(String username);
}