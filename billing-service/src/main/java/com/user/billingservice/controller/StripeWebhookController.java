package com.user.billingservice.controller;

import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import com.user.billingservice.integration.stripe.StripeWebhookHandler;
import com.user.billingservice.service.SubscriptionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/stripe/webhook")
public class StripeWebhookController {

    private final StripeWebhookHandler webhookHandler;
    private final SubscriptionService subscriptionService;

    public StripeWebhookController(StripeWebhookHandler webhookHandler, SubscriptionService subscriptionService) {
        this.webhookHandler = webhookHandler;
        this.subscriptionService = subscriptionService;
    }

    @PostMapping
    public ResponseEntity<String> handleStripeWebhook(@RequestBody String payload,
                                                      @RequestHeader("Stripe-Signature") String signature) {
        try {
            Event event = webhookHandler.parseEvent(payload, signature);
            processEvent(event);

            return ResponseEntity.ok("Webhook received");
        } catch (SignatureVerificationException e) {
            return ResponseEntity.badRequest().body("Invalid signature");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Webhook processing error");
        }
    }

    private void processEvent(Event event) {
        if ("checkout.session.completed".equals(event.getType())) {
            Optional<Session> sessionOptional = event.getDataObjectDeserializer().getObject().map(Session.class::cast);

            sessionOptional.ifPresent(session -> {
                String userId = session.getMetadata().get("userId");

                subscriptionService.upgradeSubscriptionToPRO(UUID.fromString(userId));
            });
        }
    }
}
