package com.user.apigateway.integration;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import static com.user.apigateway.integration.util.TestDataUtil.createLoginPayload;

public class UserTest {

    private static WebTestClient webTestClient;

    @BeforeAll
    static void setUp() {
        webTestClient = WebTestClient.bindToServer()
                .baseUrl("http://localhost:4004")
                .build();
    }

    @Test
    void returnsOk_andListOfUsers_whenUserAuthenticated() {
        String token = loginAndGetToken();

        webTestClient.get()
                .uri("/api/users")
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$").isNotEmpty();
    }

    private String loginAndGetToken() {
        String loginPayload = createLoginPayload("user@email.com");

        byte[] responseBody = webTestClient.post()
                .uri("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(loginPayload)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.token").isNotEmpty()
                .returnResult()
                .getResponseBody();

        String responseString = new String(responseBody);

        return JsonPath.read(responseString, "$.token");
    }
}