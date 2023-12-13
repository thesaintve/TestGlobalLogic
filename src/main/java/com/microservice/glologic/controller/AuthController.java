package com.microservice.glologic.controller;

import com.microservice.glologic.dto.SignUpUserPhoneDto;
import com.microservice.glologic.dto.UserDto;
import com.microservice.glologic.entity.User;
import com.microservice.glologic.exceptios.CustomException;
import com.microservice.glologic.service.JwtUtil;
import com.microservice.glologic.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/login")
public class AuthController {
    private static final String TYPE_BEARER = "Bearer ";
    private static final String TYPE_AUTH_HEADER = "Authorization";
    private static final String MSG_IVALID_TOKEN = "Token no válido";
    private static final String MSG_NOT_RECORD_FOUND = "No se encontró registros";

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<UserDto> login(@RequestHeader(TYPE_AUTH_HEADER) String authorizationHeader, @RequestBody UserDto userParams) {
        try {
            Optional<String> jwtTokenOptional = extractTokenFromHeader(authorizationHeader);
            String jwtToken = jwtTokenOptional.orElseThrow(() -> new CustomException(MSG_IVALID_TOKEN, HttpStatus.BAD_REQUEST ));
            if (!jwtUtil.validateToken(userParams.getEmail(), jwtToken)) throw new CustomException(MSG_IVALID_TOKEN, HttpStatus.BAD_REQUEST );

            User u = userService.getUserByEmail(userParams.getEmail())
                    .orElseThrow(() -> new CustomException(MSG_NOT_RECORD_FOUND, HttpStatus.NOT_FOUND));

            List<SignUpUserPhoneDto> phonesDto = Optional.ofNullable(u.getPhones())
                    .orElse(Collections.emptyList())
                    .stream()
                    .map(p -> new SignUpUserPhoneDto(p.getNumber(), p.getCitycode(), p.getCountrycode()))
                    .collect(Collectors.toList());

            User lstLoginU = userService.updateLastLogin(u);

            UserDto userToResponse = new UserDto(u.getId(), u.getName(), u.getEmail(), u.getPassword(), u.getActive(), u.getCreated(), lstLoginU.getLastLogin(), phonesDto, jwtUtil.generateToken(u.getName()));

            return ResponseEntity.ok(userToResponse);
        }
        catch (CustomException e) {
            throw e;
        }
        catch (Exception e) {
            throw new CustomException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    private Optional<String> extractTokenFromHeader(String authorizationHeader) {
        return Optional.ofNullable(authorizationHeader)
                .filter(header -> header.startsWith(TYPE_BEARER))
                .map(header -> header.substring(TYPE_BEARER.length()));
    }

}
