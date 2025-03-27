package com.user.authservice.service;

import com.user.authservice.model.User;
import com.user.authservice.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User user;
    private String email;
    private String unknownEmail;

    @BeforeEach
    void setUp() {
        email = "test@email.com";
        unknownEmail = "unknown@email.com";

        user = User.builder()
                .email(email)
                .build();
    }

    @Test
    void findByEmail_returnsExistingUser() {
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        Optional<User> result = userService.findByEmail(email);

        assertTrue(result.isPresent());
        assertEquals(email, result.get().getEmail());

        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    void findByEmail_returnsEmptyOptional() {
        when(userRepository.findByEmail(unknownEmail)).thenReturn(Optional.empty());

        Optional<User> result = userService.findByEmail(unknownEmail);

        assertFalse(result.isPresent());

        verify(userRepository, times(1)).findByEmail(unknownEmail);
    }
}