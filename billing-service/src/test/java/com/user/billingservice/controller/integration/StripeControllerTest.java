package com.user.billingservice.controller.integration;

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

    @Test
    void createCheckoutSession() throws StripeException {
        String userId = "user-id";
        String sessionUrl = "http://stripe-checkout-session.com";

        when(stripeService.createCheckoutSession(userId)).thenReturn(sessionUrl);

        ResponseEntity<String> response = restTemplate.postForEntity(
                "/payments/create-checkout-session?userId=" + userId, null, String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(sessionUrl);
    }
}
