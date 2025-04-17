package com.user.billingservice.controller.integration;

import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.model.checkout.Session;
import com.user.billingservice.integration.stripe.StripeWebhookHandler;
import com.user.billingservice.service.SubscriptionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Optional;
import java.util.UUID;

import static com.user.billingservice.util.TestDataUtil.createRequestEntity;
import static com.user.billingservice.util.TestDataUtil.createSession;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class StripeWebhookControllerTest {

    @Autowired
    TestRestTemplate restTemplate;

    @MockitoBean
    StripeWebhookHandler webhookHandler;

    @MockitoBean
    SubscriptionService subscriptionService;

    UUID userId = UUID.fromString("123e4567-e89b-12d3-a456-426614174001");
    String payload = "{}";
    String validSignature = "valid-signature";
    String path = "/stripe/webhook";

    @Test
    void returnsOk_andUpgradesSubscription_whenValidCheckoutSessionCompletedEvent() throws SignatureVerificationException {
        Session session = createSession(userId.toString());

        EventDataObjectDeserializer deserializer = mock(EventDataObjectDeserializer.class);
        when(deserializer.getObject()).thenReturn(Optional.of(session));

        Event event = mock(Event.class);
        when(event.getType()).thenReturn("checkout.session.completed");
        when(event.getDataObjectDeserializer()).thenReturn(deserializer);

        when(webhookHandler.parseEvent(payload, validSignature)).thenReturn(event);

        HttpEntity<String> request = createRequestEntity(payload, validSignature);

        ResponseEntity<String> response = restTemplate.postForEntity(path, request, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("Webhook received");

        verify(subscriptionService).upgradeSubscriptionToPRO(userId);
    }

    @Test
    void returnsBadRequest_whenSignatureIsInvalid() throws SignatureVerificationException {
        String signature = "invalid-signature";

        when(webhookHandler.parseEvent(payload, signature))
                .thenThrow(new SignatureVerificationException("Exception", null));

        HttpEntity<String> request = createRequestEntity(payload, signature);

        ResponseEntity<String> response = restTemplate.postForEntity(path, request, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isEqualTo("Invalid signature");
    }

    @Test
    void returnsInternalServerError_whenFailedToHandleWebhook() throws SignatureVerificationException {
        when(webhookHandler.parseEvent(payload, validSignature))
                .thenThrow(new RuntimeException("Exception"));

        HttpEntity<String> request = createRequestEntity(payload, validSignature);

        ResponseEntity<String> response = restTemplate.postForEntity(path, request, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isEqualTo("Webhook processing error");
    }
}