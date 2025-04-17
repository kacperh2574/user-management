package com.user.billingservice.service;

import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import com.user.billingservice.config.StripeConfig;
import com.user.billingservice.integration.stripe.StripeSessionHandler;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StripeService {

    private final StripeConfig stripeConfig;
    private final StripeSessionHandler stripeSessionHandler;

    public StripeService(StripeConfig stripeConfig, StripeSessionHandler stripeClient) {
        this.stripeConfig = stripeConfig;
        this.stripeSessionHandler = stripeClient;
    }

    public String createCheckoutSession(String userId) throws StripeException {
        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.SUBSCRIPTION)
                .setSuccessUrl(stripeConfig.getSuccessUrl() + "?session_id={CHECKOUT_SESSION_ID}")
                .setCancelUrl(stripeConfig.getCancelUrl())
                .addAllLineItem(List.of(
                        SessionCreateParams.LineItem.builder()
                                .setQuantity(1L)
                                .setPrice("price_1REDoePcO39cq1Zooh1iAEjs")
                                .build()
                ))
                .putMetadata("userId", userId)
                .build();

        Session session = stripeSessionHandler.createSession(params);

        return session.getUrl();
    }
}
