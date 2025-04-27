package com.user.billingservice.util;

import com.stripe.model.checkout.Session;
import com.user.billingservice.model.PlanType;
import com.user.billingservice.model.ProDetails;
import com.user.billingservice.model.Subscription;
import com.user.billingservice.model.Status;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

public class TestDataUtil {

    public static UUID USER_ID = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
    public static UUID SUBSCRIPTION_ID = UUID.fromString("123e4567-e89b-12d3-a456-426614174001");
    public static LocalDate NOW = LocalDate.now();

    public static Subscription createFreeSubscription(UUID subscriptionId) {
        return Subscription.builder()
                .id(subscriptionId)
                .userId(USER_ID)
                .planType(PlanType.FREE)
                .proDetails(null)
                .build();
    }

    public static Subscription createProSubscription() {
        return Subscription.builder()
                .id(SUBSCRIPTION_ID)
                .userId(USER_ID)
                .planType(PlanType.PRO)
                .proDetails(ProDetails.builder()
                        .status(Status.ACTIVE)
                        .startDate(NOW)
                        .endDate(NOW.plusMonths(1))
                        .build())
                .build();
    }

    public static Subscription createCancelledSubscription() {
        return Subscription.builder()
                .id(SUBSCRIPTION_ID)
                .userId(USER_ID)
                .planType(PlanType.FREE)
                .proDetails(ProDetails.builder()
                        .status(Status.CANCELLED)
                        .startDate(NOW.minusWeeks(1))
                        .endDate(NOW)
                        .build())
                .build();
    }

    public static Subscription createExpiredSubscription() {
        return Subscription.builder()
                .id(SUBSCRIPTION_ID)
                .userId(USER_ID)
                .planType(PlanType.PRO)
                .proDetails(ProDetails.builder()
                        .status(Status.ACTIVE)
                        .startDate(NOW.minusMonths(1))
                        .endDate(NOW)
                        .build())
                .build();
    }

    public static Session createSession(final String userId) {
        Session session = new Session();
        session.setMetadata(Map.of("userId", userId));

        return session;
    }

    public static HttpEntity<String> createRequestEntity(final String payload, final String signature) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Stripe-Signature", signature);

        return new HttpEntity<>(payload, headers);
    }
}
