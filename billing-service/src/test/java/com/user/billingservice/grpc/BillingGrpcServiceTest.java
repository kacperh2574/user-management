package com.user.billingservice.grpc;

import billing.BillingRequest;
import billing.BillingResponse;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class BillingGrpcServiceTest {

    private BillingGrpcService billingGrpcService;
    private StreamObserver<BillingResponse> responseObserver;

    @BeforeEach
    public void setup() {
        billingGrpcService = new BillingGrpcService();
        responseObserver = mock(StreamObserver.class);
    }

    @Test
    public void createBillingAccount() {
        BillingRequest request = BillingRequest.newBuilder().build();

        billingGrpcService.createBillingAccount(request, responseObserver);

        ArgumentCaptor<BillingResponse> captor = ArgumentCaptor.forClass(BillingResponse.class);

        verify(responseObserver).onNext(captor.capture());
        verify(responseObserver).onCompleted();

        BillingResponse response = captor.getValue();

        assertEquals("12345", response.getAccountId());
        assertEquals("Active", response.getStatus());
    }
}
