package com.user.billingservice.controller.integration;

import com.stripe.exception.ApiException;
import com.stripe.exception.StripeException;
import com.user.billingservice.service.StripeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StripeControllerTest {

    @Autowired
    TestRestTemplate restTemplate;

    @MockitoBean
    StripeService stripeService;

    String userId = "user-id";
    String pathWithUserIdParam = "/payments/create-checkout-session?userId=" + userId;

    @Test
    void returnsOk_whenCheckoutSessionCreated() throws StripeException {
        String sessionUrl = "http://stripe-checkout-session.com";

        when(stripeService.createCheckoutSession(userId)).thenReturn(sessionUrl);

        ResponseEntity<String> response = restTemplate.postForEntity(pathWithUserIdParam, null, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(sessionUrl);
    }

    @Test
    void returnsInternalServerError_whenFailedToCreateCheckoutSession() throws StripeException {
        when(stripeService.createCheckoutSession(userId)).thenThrow(ApiException.class);

        ResponseEntity<String> response = restTemplate.postForEntity(pathWithUserIdParam, null, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isEqualTo("Error creating Stripe checkout session");
    }
}
