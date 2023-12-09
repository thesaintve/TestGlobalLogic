package com.microservice.glologic.controller;

import com.microservice.glologic.dto.SignUpUserDto;
import com.microservice.glologic.dto.CreatedUserDto;
import com.microservice.glologic.entity.User;
import com.microservice.glologic.exceptios.CustomException;
import com.microservice.glologic.service.JwtUtil;
import com.microservice.glologic.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.stream.Collectors;

@RestController
@RequestMapping(value="/sign-up")
@Validated
public class UserController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<CreatedUserDto> signUp(@Validated @RequestBody SignUpUserDto signUpUserDto, BindingResult bindingResult) {
        try {
            if (!bindingResult.hasErrors()) {
                User uCreated = userService.createUser(signUpUserDto);
                CreatedUserDto userResponse = new CreatedUserDto(uCreated.getId(), uCreated.getName(), uCreated.getEmail(), uCreated.getActive(), uCreated.getCreated(), uCreated.getLastLogin(), jwtUtil.generateToken(uCreated.getName()));

                return ResponseEntity.ok(userResponse);
            }
            String errorMessage = bindingResult.getFieldErrors()
                    .stream()
                    .map(error -> error.getField() + ": " + error.getDefaultMessage())
                    .collect(Collectors.joining(", ", "Fallo en validación [ ", " ] "));
            throw new CustomException(errorMessage, HttpStatus.BAD_REQUEST);
        }
        catch (org.springframework.dao.DataIntegrityViolationException e) {
            throw new CustomException("Ya existe otro usuario con el mismo correo", HttpStatus.BAD_REQUEST);
        }
        catch (CustomException e) {
            throw e;
        }
        catch (Exception e) {
            throw new CustomException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
