package com.microservice.glologic.service;

import com.microservice.glologic.dto.SignUpUserDto;
import com.microservice.glologic.entity.User;
import com.microservice.glologic.entity.UserPhone;
import com.microservice.glologic.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Override
    public User createUser(SignUpUserDto uDto) {

        List<UserPhone> userPhones = Optional.ofNullable(uDto.getPhones())
                .orElse(Collections.emptyList())
                .stream()
                .map(phoneDto -> new UserPhone(phoneDto.getNumber(), phoneDto.getCitycode(), phoneDto.getCountrycode()))
                .collect(Collectors.toList());

        User newUser = new User(uDto.getName(), uDto.getEmail(), uDto.getPassword(), new Date(), userPhones);
        userPhones.stream().forEach(phone -> phone.setUser(newUser));

        return userRepository.save(newUser);
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User updateLastLogin(User loginUser) {
        loginUser.setLastLogin(new Date());
        return userRepository.save(loginUser);
    }

}
