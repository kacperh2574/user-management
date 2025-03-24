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
    void createBillingAccount() {
        BillingResponse expectedResponse = BillingResponse.newBuilder()
                .setAccountId("12345")
                .setStatus("Active")
                .build();

        when(blockingStub.createBillingAccount(any(BillingRequest.class)))
                .thenReturn(expectedResponse);

        BillingResponse actualResponse = billingServiceGrpcClient
                .createBillingAccount("user ID", "name", "email@example.com");

        assertEquals(expectedResponse, actualResponse);

        verify(blockingStub, times(1))
                .createBillingAccount(any(BillingRequest.class));
    }
}
