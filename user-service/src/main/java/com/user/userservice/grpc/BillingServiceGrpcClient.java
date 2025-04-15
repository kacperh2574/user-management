package com.user.userservice.grpc;

import billing.BillingRequest;
import billing.BillingResponse;
import billing.BillingServiceGrpc.BillingServiceBlockingStub;
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

    public BillingResponse createSubscription(String userId, String name, String email) {
        BillingRequest request = BillingRequest.newBuilder()
                .setUserId(userId)
                .setName(name)
                .setEmail(email)
                .setPlanType("FREE")
                .build();

        BillingResponse response = blockingStub.createSubscription(request);

        log.info("Received response from Billing Service gRPC: {}", response);

        return response;
    }
}

