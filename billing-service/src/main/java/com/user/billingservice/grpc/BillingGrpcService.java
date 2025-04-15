package com.user.billingservice.grpc;

import billing.BillingRequest;
import billing.BillingResponse;
import billing.BillingServiceGrpc.BillingServiceImplBase;
import com.user.billingservice.dto.SubscriptionRequestDTO;
import com.user.billingservice.dto.SubscriptionResponseDTO;
import com.user.billingservice.service.SubscriptionService;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

import static com.user.billingservice.mapper.BillingGrpcMapper.toBillingResponse;
import static com.user.billingservice.mapper.BillingGrpcMapper.toSubscriptionRequestDTO;

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

        SubscriptionRequestDTO subscriptionRequest = toSubscriptionRequestDTO(request);

        UUID userId = UUID.fromString(request.getUserId());
        SubscriptionResponseDTO subscriptionResponse = subscriptionService.createSubscription(userId, subscriptionRequest);

        BillingResponse response = toBillingResponse(subscriptionResponse);

        log.info("Subscription created: {}", response);

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
