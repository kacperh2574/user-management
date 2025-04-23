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
import java.util.Objects;
import java.util.UUID;

public class TestDataUtil {

    public static Subscription createSubscription(UUID subscriptionId, UUID userId, PlanType planType) {
        return Subscription.builder()
                .id(subscriptionId)
                .userId(userId)
                .planType(planType)
                .proDetails(Objects.equals(planType, PlanType.FREE) ? null : ProDetails.builder()
                        .status(Status.ACTIVE)
                        .startDate(LocalDate.now().minusMonths(2))
                        .endDate(LocalDate.now().minusMonths(1))
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
