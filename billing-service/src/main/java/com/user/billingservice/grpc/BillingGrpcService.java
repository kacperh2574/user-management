package com.user.billingservice.grpc;

import billing.CancelSubscriptionRequest;
import billing.CancelSubscriptionResponse;
import billing.CreateSubscriptionRequest;
import billing.CreateSubscriptionResponse;
import billing.BillingServiceGrpc.BillingServiceImplBase;
import com.user.billingservice.dto.SubscriptionResponseDTO;
import com.user.billingservice.service.SubscriptionService;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

import static com.user.billingservice.mapper.BillingGrpcMapper.toCancelSubscriptionResponse;
import static com.user.billingservice.mapper.BillingGrpcMapper.toCreateSubscriptionResponse;

@GrpcService
public class BillingGrpcService extends BillingServiceImplBase {

    private static final Logger log = LoggerFactory.getLogger(BillingGrpcService.class);

    private final SubscriptionService subscriptionService;

    public BillingGrpcService(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @Override
    public void createSubscription(CreateSubscriptionRequest request, StreamObserver<CreateSubscriptionResponse> responseObserver) {
        log.info("Received CreateSubscription request from User Service gRPC: {}", request);

        UUID userId = UUID.fromString(request.getUserId());
        SubscriptionResponseDTO subscriptionResponse = subscriptionService.createSubscription(userId);

        CreateSubscriptionResponse response = toCreateSubscriptionResponse(subscriptionResponse);

        log.info("Subscription created: {}", response);

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void cancelSubscription(CancelSubscriptionRequest request, StreamObserver<CancelSubscriptionResponse> responseObserver) {
        log.info("Received CancelSubscription request from User Service gRPC: {}", request);

        UUID userId = UUID.fromString(request.getUserId());
        SubscriptionResponseDTO subscriptionResponse = subscriptionService.cancelProSubscription(userId);

        CancelSubscriptionResponse response = toCancelSubscriptionResponse(subscriptionResponse);

        log.info("Subscription cancelled: {}", response);

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
