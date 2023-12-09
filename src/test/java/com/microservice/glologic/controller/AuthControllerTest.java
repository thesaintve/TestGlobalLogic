package com.microservice.glologic.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservice.glologic.dto.UserDto;
import com.microservice.glologic.entity.User;
import com.microservice.glologic.repository.UserRepository;
import com.microservice.glologic.service.JwtUtil;
import com.microservice.glologic.service.UserService;
import java.util.Date;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
public class AuthControllerTest {
    private static final String TOKEN = "fakeToken";

    private final UserDto requestUser = new UserDto(null,"Nombre Uno", "nombreUno@dominio.com", null, null, null, null, null, null);

    private MockMvc mockMvc;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
    }

    @Test
    void login_WithUser_ReturnsUserDetails() throws Exception {
        Date mockLastLogin = new Date();
        UserDto expectedUser = new UserDto(1L,"Nombre Uno", "nombreUno@dominio.com", "Mmm1nnn2", true, new Date(), mockLastLogin, null, TOKEN);

        User returnedUser = new User("Nombre Uno", "nombreUno@dominio.com", "Mmm1nnn2", new Date(), null);
        returnedUser.setLastLogin(mockLastLogin);
        returnedUser.setActive(true);
        returnedUser.setId(1L);

        when(userService.getUserByEmail(any(String.class))).thenReturn(Optional.of(returnedUser));
        when(userService.updateLastLogin(any(User.class))).thenReturn(returnedUser);
        when(jwtUtil.validateToken(any(String.class), any(String.class))).thenReturn(true);

        mockMvc.perform(post("/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(requestUser))
            .header("Authorization", "Bearer " + TOKEN))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.name").value(expectedUser.getName()))
            .andExpect(jsonPath("$.email").value(expectedUser.getEmail()))
            .andExpect(jsonPath("$.active").value(expectedUser.getActive()));

    }

    @Test
    void login_WithUser_NotUserFound() throws Exception {
        when(jwtUtil.validateToken(any(String.class), any(String.class))).thenReturn(true);

        try {
            mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(requestUser))
                .header("Authorization", "Bearer " + TOKEN))
                .andExpect(status().isBadRequest());
        }
        catch (Exception e) {
            assertThrows(org.springframework.web.util.NestedServletException.class, () -> { throw e; }, "Debe haber una excepción del tipo NestedServletException cuando no se consigue registros");
            org.springframework.web.util.NestedServletException ce = (org.springframework.web.util.NestedServletException) e;
            assertTrue(ce.getMessage().contains("No se encontró registros"), "No se debió encontrar registros");
        }
    }

    @Test
    void login_WithoutToken_BadRequest() throws Exception {
        mockMvc.perform(post("/login")
        .contentType(MediaType.APPLICATION_JSON)
        .content(new ObjectMapper().writeValueAsString(requestUser)))
        .andExpect(status().isBadRequest());
    }

}
