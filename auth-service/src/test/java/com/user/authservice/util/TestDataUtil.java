package com.user.authservice.util;

import com.user.authservice.dto.LoginRequestDTO;
import com.user.authservice.model.User;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.password.PasswordEncoder;

public class TestDataUtil {

    public static LoginRequestDTO createValidLoginRequestDTO() {
        return LoginRequestDTO.builder()
                .email("test@email.com")
                .password("password")
                .build();
    }

    public static LoginRequestDTO createInvalidLoginRequestDTO() {
        return LoginRequestDTO.builder()
                .email("test@email.com")
                .password("invalid-password")
                .build();
    }

    public static User createUser(final PasswordEncoder passwordEncoder) {
        return User.builder()
                .email("test@email.com")
                .password(passwordEncoder.encode("password"))
                .role("USER")
                .build();
    }

    public static HttpEntity<Void> createRequestEntity(final String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        return new HttpEntity<>(headers);
    }
}