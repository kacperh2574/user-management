package com.user.billingservice.service;

import com.stripe.model.checkout.Session;
import com.user.billingservice.config.StripeConfig;
import com.user.billingservice.integration.stripe.StripeClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StripeServiceTest {

    @Mock
    private StripeConfig stripeConfig;

    @Mock
    private StripeClient stripeClient;

    @InjectMocks
    private StripeService stripeService;

    private Session session;
    private String sessionUrl;

    @BeforeEach
    void setUp() {
        when(stripeConfig.getSuccessUrl()).thenReturn("http://localhost:8080/success");
        when(stripeConfig.getCancelUrl()).thenReturn("http://localhost:8080/cancel");

        session = mock(Session.class);

        sessionUrl = "http://session.com";
    }

    @Test
    void createCheckoutSession_returnsSessionUrl() throws Exception {
        when(session.getUrl()).thenReturn(sessionUrl);
        when(stripeClient.createSession(any())).thenReturn(session);

        String result = stripeService.createCheckoutSession("user-id");

        assertEquals(sessionUrl, result);

        verify(stripeClient).createSession(any());
    }
}