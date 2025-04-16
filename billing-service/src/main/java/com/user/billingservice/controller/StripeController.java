package com.user.billingservice.controller;

import com.user.billingservice.service.StripeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payments")
public class StripeController {

    private final StripeService stripeService;

    public StripeController(StripeService stripeService) {
        this.stripeService = stripeService;
    }

    @PostMapping("/create-checkout-session")
    public ResponseEntity<String> createCheckoutSession(@RequestParam String userId) {
        try {
            String sessionUrl = stripeService.createCheckoutSession(userId);

            return ResponseEntity.ok(sessionUrl);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error creating Stripe checkout session");
        }
    }
}
