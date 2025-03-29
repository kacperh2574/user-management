package com.user.authservice.controller.integration;

import com.user.authservice.dto.LoginRequestDTO;
import com.user.authservice.dto.LoginResponseDTO;
import com.user.authservice.model.User;
import com.user.authservice.repository.UserRepository;
import com.user.authservice.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthControllerTest {

    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtUtil jwtUtil;

    String validToken;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();

        User user = User.builder()
                .email("test@email.com")
                .password(passwordEncoder.encode("password"))
                .role("USER")
                .build();
        userRepository.save(user);
        validToken = jwtUtil.generateToken(user.getEmail(), user.getRole());
    }

    @Nested
    class Login {

        @Test
        void returnsOk_whenUserAuthenticated() {
            LoginRequestDTO request = LoginRequestDTO.builder()
                    .email("test@email.com")
                    .password("password")
                    .build();

            ResponseEntity<LoginResponseDTO> response = restTemplate.postForEntity("/login", request, LoginResponseDTO.class);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertNotNull(response.getBody().getToken());
        }

        @Test
        void returnsUnauthorized_whenUserNotAuthenticated() {
            LoginRequestDTO request = LoginRequestDTO.builder()
                    .email("test@email.com")
                    .password("invalid-password")
                    .build();

            ResponseEntity<Void> response = restTemplate.postForEntity("/login", request, Void.class);

            assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        }
    }

    @Nested
    class Validate {

        @Test
        void returnsOk_whenTokenIsValid() {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + validToken);
            HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

            ResponseEntity<Void> response = restTemplate.exchange("/validate", HttpMethod.GET, requestEntity, Void.class);

            assertEquals(HttpStatus.OK, response.getStatusCode());
        }

        @Test
        void returnsUnauthorized_whenTokenIsNotValid() {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer invalid-token");
            HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

            ResponseEntity<Void> response = restTemplate.exchange("/validate", HttpMethod.GET, requestEntity, Void.class);

            assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        }

        @Test
        void returnsUnauthorized_whenTokenIsMissing() {
            ResponseEntity<Void> response = restTemplate.getForEntity("/validate", Void.class);

            assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        }
    }
}
