package com.user.userservice.grpc;

import billing.BillingRequest;
import billing.BillingResponse;
import billing.BillingServiceGrpc;
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
    void createSubscription() {
        BillingResponse expectedResponse = BillingResponse.newBuilder()
                .setSubscriptionId("12345")
                .setStatus("ACTIVE")
                .build();

        when(blockingStub.createSubscription(any(BillingRequest.class)))
                .thenReturn(expectedResponse);

        BillingResponse actualResponse = billingServiceGrpcClient
                .createSubscription("user ID", "name", "email@example.com");

        assertEquals(expectedResponse, actualResponse);

        verify(blockingStub, times(1))
                .createSubscription(any(BillingRequest.class));
    }
}
