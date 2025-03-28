package com.user.authservice.service;

import com.user.authservice.dto.LoginRequestDTO;
import com.user.authservice.model.User;
import com.user.authservice.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
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

    private LoginRequestDTO loginRequest;
    private User user;

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
    void authenticate_returnsValidTokenOptional() {
        when(userService.findByEmail(loginRequest.getEmail()))
                .thenReturn(Optional.of(user));
        when(passwordEncoder.matches(loginRequest.getPassword(), user.getPassword()))
                .thenReturn(true);
        when(jwtUtil.generateToken(user.getEmail(), user.getRole()))
                .thenReturn("token");

        Optional<String> token = authService.authenticate(loginRequest);

        assertTrue(token.isPresent());
        assertEquals("token", token.get());
    }

    @Test
    void authenticate_returnsEmptyOptional_whenUserDoesNotExist() {
        when(userService.findByEmail(loginRequest.getEmail()))
                .thenReturn(Optional.empty());

        Optional<String> token = authService.authenticate(loginRequest);

        assertFalse(token.isPresent());
    }

    @Test
    void authenticate_returnsEmptyOptional_whenPasswordDoesNotMatch() {
        when(userService.findByEmail(loginRequest.getEmail()))
                .thenReturn(Optional.of(user));
        when(passwordEncoder.matches(loginRequest.getPassword(), user.getPassword()))
                .thenReturn(false);

        Optional<String> token = authService.authenticate(loginRequest);

        assertFalse(token.isPresent());
    }
}