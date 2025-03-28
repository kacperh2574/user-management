package com.user.authservice.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class JwtUtilTest {

    private JwtUtil jwtUtil;

    private final String email = "test@email.com";
    private final String role = "USER";

    @BeforeEach
    void setUp() {
        String testSecret = "gY3pRoizllPk8P2VuvwtNdyaojyMg61oSiofQEQ8N2Q=";

        jwtUtil = new JwtUtil(testSecret);
    }

    @Test
    void generateToken_returnsToken() {
        String token = jwtUtil.generateToken(email, role);

        assertNotNull(token);
    }
}