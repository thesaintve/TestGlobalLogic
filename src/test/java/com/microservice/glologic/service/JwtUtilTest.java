package com.microservice.glologic.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class JwtUtilTest {

    @Autowired
    private JwtUtil jwtUtil;

    @Test
    public void testGenerateAndValidateToken() {
        String username = "testUser";

        String token = jwtUtil.generateToken(username);
        assertTrue(jwtUtil.validateToken(username, token));

        String extractedUserName = jwtUtil.extractUsername(token);
        assertEquals(username, extractedUserName);
    }

}
