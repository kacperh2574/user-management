package com.user.authservice.service;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.user.authservice.dto.LoginRequestDTO;
import com.user.authservice.model.User;
import com.user.authservice.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthService authService;

    String validToken = "token";
    String invalidToken = "invalid-token";
    LoginRequestDTO loginRequest;
    User user;

    @Nested
    class Authenticate {

        @BeforeEach
        void setUp() {
            loginRequest = LoginRequestDTO.builder()
                    .email("test@email.com")
                    .password("password")
                    .build();

            user = User.builder()
                    .email("test@email.com")
                    .password("encoded-password")
                    .role("USER")
                    .build();
        }

        @Test
        void returnsValidTokenOptional() {
            when(userService.findByEmail(loginRequest.getEmail()))
                    .thenReturn(Optional.of(user));
            when(passwordEncoder.matches(loginRequest.getPassword(), user.getPassword()))
                    .thenReturn(true);
            when(jwtUtil.generateToken(user.getEmail(), user.getRole()))
                    .thenReturn(validToken);

            Optional<String> result = authService.authenticate(loginRequest);

            assertTrue(result.isPresent());
            assertEquals(validToken, result.get());
        }

        @Test
        void returnsEmptyOptional_whenUserDoesNotExist() {
            when(userService.findByEmail(loginRequest.getEmail()))
                    .thenReturn(Optional.empty());

            Optional<String> result = authService.authenticate(loginRequest);

            assertFalse(result.isPresent());
        }

        @Test
        void returnsEmptyOptional_whenPasswordDoesNotMatch() {
            when(userService.findByEmail(loginRequest.getEmail()))
                    .thenReturn(Optional.of(user));
            when(passwordEncoder.matches(loginRequest.getPassword(), user.getPassword()))
                    .thenReturn(false);

            Optional<String> result = authService.authenticate(loginRequest);

            assertFalse(result.isPresent());
        }
    }

    @Nested
    class ValidateToken {
        @Test
        void returnsTrue_whenTokenIsValid() {
            when(jwtUtil.verifyToken(validToken))
                    .thenReturn(mock(DecodedJWT.class));

            boolean result = authService.validateToken(validToken);

            assertTrue(result);
        }

        @Test
        void returnsFalse_whenTokenIsNotValid() {
            doThrow(new JWTVerificationException("Invalid JWT"))
                    .when(jwtUtil)
                    .verifyToken(invalidToken);

            boolean result = authService.validateToken(invalidToken);

            assertFalse(result);
        }
    }
}