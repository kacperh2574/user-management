package com.user.billingservice.grpc;

import billing.BillingRequest;
import billing.BillingResponse;
import billing.BillingServiceGrpc.BillingServiceImplBase;
import com.user.billingservice.dto.SubscriptionRequestDTO;
import com.user.billingservice.dto.SubscriptionResponseDTO;
import com.user.billingservice.model.PlanType;
import com.user.billingservice.service.SubscriptionService;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

@GrpcService
public class BillingGrpcService extends BillingServiceImplBase {

    private static final Logger log = LoggerFactory.getLogger(BillingGrpcService.class);

    private final SubscriptionService subscriptionService;

    public BillingGrpcService(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @Override
    public void createSubscription(BillingRequest request, StreamObserver<BillingResponse> responseObserver) {
        log.info("Received createSubscription request: {}", request);

        UUID userId = UUID.fromString(request.getUserId());

        SubscriptionRequestDTO subscriptionRequest = SubscriptionRequestDTO.builder()
                .planType(PlanType.valueOf(request.getPlanType()))
                .build();

        SubscriptionResponseDTO subscription = subscriptionService.createSubscription(userId, subscriptionRequest);

        BillingResponse response = BillingResponse.newBuilder()
                .setSubscriptionId(subscription.getId())
                .setStatus(subscription.getStatus().toString())
                .build();

        log.info("Subscription created: {}", response);

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
