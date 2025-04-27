package com.user.billingservice.grpc;

import billing.CancelSubscriptionRequest;
import billing.CancelSubscriptionResponse;
import billing.CreateSubscriptionRequest;
import billing.CreateSubscriptionResponse;
import com.user.billingservice.dto.ProDetailsDTO;
import com.user.billingservice.dto.SubscriptionResponseDTO;
import com.user.billingservice.model.PlanType;
import com.user.billingservice.model.Status;
import com.user.billingservice.service.SubscriptionService;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class BillingGrpcServiceTest {

    private BillingGrpcService billingGrpcService;
    private SubscriptionService subscriptionService;

    UUID userId = UUID.randomUUID();
    UUID subscriptionId = UUID.randomUUID();

    @BeforeEach
    public void setup() {
        subscriptionService = mock(SubscriptionService.class);
        billingGrpcService = new BillingGrpcService(subscriptionService);
    }

    @Test
    public void createSubscription_receivesCreateSubscriptionRequest_respondsWithCreateSubscriptionResponse() {
        StreamObserver<CreateSubscriptionResponse> responseObserver = mock(StreamObserver.class);

        CreateSubscriptionRequest request = CreateSubscriptionRequest.newBuilder()
                .setUserId(userId.toString())
                .setName("Name")
                .setEmail("test@email.com")
                .build();

        SubscriptionResponseDTO responseDTO = SubscriptionResponseDTO.builder()
                .id(subscriptionId.toString())
                .planType(PlanType.FREE)
                .proDetails(null)
                .createdAt(LocalDate.now())
                .build();

        when(subscriptionService.createSubscription(eq(userId)))
                .thenReturn(responseDTO);

        billingGrpcService.createSubscription(request, responseObserver);

        ArgumentCaptor<CreateSubscriptionResponse> captor = ArgumentCaptor.forClass(CreateSubscriptionResponse.class);

        verify(responseObserver).onNext(captor.capture());
        verify(responseObserver).onCompleted();

        CreateSubscriptionResponse response = captor.getValue();

        assertEquals(subscriptionId.toString(), response.getSubscriptionId());
        assertEquals(PlanType.FREE.toString(), response.getPlanType());
    }

    @Test
    public void cancelSubscription_receivesCancelSubscriptionRequest_respondsWithCancelSubscriptionResponse() {
        StreamObserver<CancelSubscriptionResponse> responseObserver = mock(StreamObserver.class);

        CancelSubscriptionRequest request = CancelSubscriptionRequest.newBuilder()
                .setUserId(userId.toString())
                .build();

        SubscriptionResponseDTO responseDTO = SubscriptionResponseDTO.builder()
                .id(subscriptionId.toString())
                .planType(PlanType.FREE)
                .proDetails(ProDetailsDTO.builder()
                        .status(Status.CANCELLED)
                        .build())
                .createdAt(LocalDate.now())
                .build();

        when(subscriptionService.cancelProSubscription(eq(userId)))
                .thenReturn(responseDTO);

        billingGrpcService.cancelSubscription(request, responseObserver);

        ArgumentCaptor<CancelSubscriptionResponse> captor = ArgumentCaptor.forClass(CancelSubscriptionResponse.class);

        verify(responseObserver).onNext(captor.capture());
        verify(responseObserver).onCompleted();

        CancelSubscriptionResponse response = captor.getValue();

        assertEquals(subscriptionId.toString(), response.getSubscriptionId());
        assertEquals(PlanType.FREE.toString(), response.getPlanType());
        assertEquals(Status.CANCELLED.toString(), response.getProStatus());
    }
}
