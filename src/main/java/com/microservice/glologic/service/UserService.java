package com.microservice.glologic.service;

import com.microservice.glologic.dto.SignUpUserDto;
import com.microservice.glologic.entity.User;

import java.util.Optional;

public interface UserService {
    public User createUser(SignUpUserDto user);
    public Optional<User> getUserByEmail(String email);
    public User updateLastLogin(User user);
}
