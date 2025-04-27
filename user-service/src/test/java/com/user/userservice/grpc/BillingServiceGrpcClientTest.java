package com.user.userservice.grpc;

import billing.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class BillingServiceGrpcClientTest {

    private BillingServiceGrpc.BillingServiceBlockingStub blockingStub;
    private BillingServiceGrpcClient billingServiceGrpcClient;

    @BeforeEach
    void setUp() {
        blockingStub = mock(BillingServiceGrpc.BillingServiceBlockingStub.class);
        billingServiceGrpcClient = new BillingServiceGrpcClient(blockingStub);
    }

    @Test
    void createSubscription_requestsWithCreateSubscriptionRequest_receivesCreateSubscriptionResponse() {
        CreateSubscriptionResponse expectedResponse = CreateSubscriptionResponse.newBuilder()
                .setSubscriptionId("12345")
                .setPlanType("FREE")
                .build();

        when(blockingStub.createSubscription(any(CreateSubscriptionRequest.class)))
                .thenReturn(expectedResponse);

        CreateSubscriptionResponse actualResponse = billingServiceGrpcClient
                .createSubscription("user ID", "name", "email@example.com");

        assertEquals(expectedResponse, actualResponse);

        verify(blockingStub, times(1))
                .createSubscription(any(CreateSubscriptionRequest.class));
    }

    @Test
    void cancelSubscription_requestsWithCancelSubscriptionRequest_receivesCancelSubscriptionResponse() {
        CancelSubscriptionResponse expectedResponse = CancelSubscriptionResponse.newBuilder()
                .setSubscriptionId("12345")
                .setPlanType("FREE")
                .setProStatus("CANCELLED")
                .build();

        when(blockingStub.cancelSubscription(any(CancelSubscriptionRequest.class)))
                .thenReturn(expectedResponse);

        CancelSubscriptionResponse actualResponse = billingServiceGrpcClient
                .cancelSubscription("user ID");

        assertEquals(expectedResponse, actualResponse);

        verify(blockingStub, times(1))
                .cancelSubscription(any(CancelSubscriptionRequest.class));
    }
}
