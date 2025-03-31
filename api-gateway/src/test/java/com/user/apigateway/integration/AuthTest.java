package com.user.apigateway.integration;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import static com.user.apigateway.integration.util.TestDataUtil.createLoginPayload;

public class AuthTest {

    private static WebTestClient webTestClient;

    @BeforeAll
    static void setUp() {
        webTestClient = WebTestClient.bindToServer()
                .baseUrl("http://api-gateway:4004")
                .build();
    }

    @Test
    void returnsOk_andToken_whenLoginPayloadIsValid() {
        String loginPayload = createLoginPayload("user@email.com");

        webTestClient.post()
                .uri("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(loginPayload)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.token").isNotEmpty();
    }

    @Test
    void returnsUnauthorized_whenLoginPayloadIsNotValid() {
        String loginPayload = createLoginPayload("invalid@email.com");

        webTestClient.post()
                .uri("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(loginPayload)
                .exchange()
                .expectStatus().isUnauthorized();
    }
}