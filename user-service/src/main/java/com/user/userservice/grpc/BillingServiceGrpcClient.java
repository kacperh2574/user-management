package com.user.userservice.grpc;

import billing.BillingServiceGrpc.BillingServiceBlockingStub;
import billing.CancelSubscriptionRequest;
import billing.CancelSubscriptionResponse;
import billing.CreateSubscriptionRequest;
import billing.CreateSubscriptionResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class BillingServiceGrpcClient {

    private static final Logger log = LoggerFactory.getLogger(BillingServiceGrpcClient.class);

    private final BillingServiceBlockingStub blockingStub;

    public BillingServiceGrpcClient(BillingServiceBlockingStub blockingStub) {
        this.blockingStub = blockingStub;
    }

    public CreateSubscriptionResponse createSubscription(String userId, String name, String email) {
        CreateSubscriptionRequest request = CreateSubscriptionRequest.newBuilder()
                .setUserId(userId)
                .setName(name)
                .setEmail(email)
                .build();

        CreateSubscriptionResponse response = blockingStub.createSubscription(request);

        log.info("Received CreateSubscriptionResponse from Billing Service gRPC: {}", response);

        return response;
    }

    public CancelSubscriptionResponse cancelSubscription(String userId) {
        CancelSubscriptionRequest request = CancelSubscriptionRequest.newBuilder()
                .setUserId(userId)
                .build();

        CancelSubscriptionResponse response = blockingStub.cancelSubscription(request);

        log.info("Received CancelSubscriptionResponse from Billing Service gRPC: {}", response);

        return response;
    }
}

