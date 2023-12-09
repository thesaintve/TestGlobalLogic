package com.microservice.glologic.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservice.glologic.dto.SignUpUserDto;
import com.microservice.glologic.entity.User;
import com.microservice.glologic.repository.UserRepository;
import com.microservice.glologic.service.JwtUtil;
import com.microservice.glologic.service.UserService;
import java.util.Date;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserService userService;

    @Autowired
    private UserController userController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    void signUp_WithValidUser_ReturnsOk() throws Exception {
        SignUpUserDto signUpUser = new SignUpUserDto();
        signUpUser.setName("Nombre Uno");
        signUpUser.setEmail("nombreUno@dominio.com");
        signUpUser.setPassword("Mmm1nnn2");

        User expectedUser = new User("Nombre Uno", "nombreUno@dominio.com", "Mmm1nnn2", new Date(), null);

        when(userService.createUser(any(SignUpUserDto.class))).thenReturn(expectedUser);

        mockMvc.perform(post("/sign-up")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(signUpUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value(expectedUser.getName()))
                .andExpect(jsonPath("$.email").value(expectedUser.getEmail()))
                .andExpect(jsonPath("$.active").value(expectedUser.getActive()));
    }

    @Test
    void signUp_WithEmptyUser_ReturnsError() throws Exception {
        SignUpUserDto emptySignUpUser = new SignUpUserDto();

        try {
            mockMvc.perform(post("/sign-up")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(new ObjectMapper().writeValueAsString(emptySignUpUser)))
                    .andExpect(status().isBadRequest());
        }
        catch (Exception e) {
            assertThrows(org.springframework.web.util.NestedServletException.class, () -> { throw e; }, "Debe haber una excepción del tipo NestedServletException cuando el sigUp viene vacio");
            org.springframework.web.util.NestedServletException ce = (org.springframework.web.util.NestedServletException) e;
            assertTrue(ce.getMessage().contains("Fallo en validación"), "Debe haber mensaje de validación cuando el sigUp viene vacio");
        }

    }

}
