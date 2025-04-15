package com.user.billingservice.grpc;

import billing.BillingRequest;
import billing.BillingResponse;
import com.user.billingservice.dto.SubscriptionRequestDTO;
import com.user.billingservice.dto.SubscriptionResponseDTO;
import com.user.billingservice.model.PlanType;
import com.user.billingservice.model.SubscriptionStatus;
import com.user.billingservice.service.SubscriptionService;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class BillingGrpcServiceTest {

    private BillingGrpcService billingGrpcService;
    private SubscriptionService subscriptionService;
    private StreamObserver<BillingResponse> responseObserver;

    @BeforeEach
    public void setup() {
        subscriptionService = mock(SubscriptionService.class);
        billingGrpcService = new BillingGrpcService(subscriptionService);
        responseObserver = mock(StreamObserver.class);
    }

    @Test
    public void createSubscription() {
        UUID userId = UUID.randomUUID();
        UUID subscriptionId = UUID.randomUUID();

        BillingRequest request = BillingRequest.newBuilder()
                .setUserId(userId.toString())
                .setName("Name")
                .setEmail("test@email.com")
                .setPlanType("FREE")
                .build();

        SubscriptionResponseDTO responseDTO = SubscriptionResponseDTO.builder()
                .id(subscriptionId.toString())
                .status(SubscriptionStatus.ACTIVE)
                .plan(PlanType.FREE)
                .build();

        when(subscriptionService.createSubscription(eq(userId), any(SubscriptionRequestDTO.class)))
                .thenReturn(responseDTO);

        billingGrpcService.createSubscription(request, responseObserver);

        ArgumentCaptor<BillingResponse> captor = ArgumentCaptor.forClass(BillingResponse.class);

        verify(responseObserver).onNext(captor.capture());
        verify(responseObserver).onCompleted();

        BillingResponse response = captor.getValue();

        assertEquals(subscriptionId.toString(), response.getSubscriptionId());
        assertEquals("ACTIVE", response.getStatus());
    }
}
