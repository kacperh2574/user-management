package com.user.billingservice.util;

import com.stripe.model.checkout.Session;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.Map;

public class TestDataUtil {

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
