package com.user.authservice.util;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    String secret = Base64.getEncoder().encodeToString("secret-key".getBytes());
    String email = "test@email.com";
    String role = "USER";

    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil(secret);
    }

    @Test
    void generateToken_returnsValidToken() {
        String result = jwtUtil.generateToken(email, role);

        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    void verifyToken_returnsDecodedJWT_whenTokenIsValid() {
        String validToken = jwtUtil.generateToken(email, role);
        DecodedJWT result = jwtUtil.verifyToken(validToken);

        assertNotNull(result);
        assertEquals(email, result.getSubject());
        assertEquals(role, result.getClaim("role").asString());
    }

    @Test
    void verifyToken_throwsException_whenTokenIsNotValid() {
        String invalidToken = "invalid-token";

        JWTVerificationException exception = assertThrows(JWTVerificationException.class, () ->
                jwtUtil.verifyToken(invalidToken)
        );

        assertEquals("Invalid JWT", exception.getMessage());
    }
}