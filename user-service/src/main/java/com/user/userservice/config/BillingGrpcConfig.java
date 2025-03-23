package com.user.userservice.config;

import billing.BillingServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static billing.BillingServiceGrpc.newBlockingStub;

@Configuration
class BillingGrpcConfig {

    @Bean
    public ManagedChannel managedChannel(@Value("${billing.service.address:localhost}") String serverAddress,
                                         @Value("${billing.service.grpc.port:9001}") int serverPort) {

        return ManagedChannelBuilder.forAddress(serverAddress, serverPort)
                .usePlaintext()
                .build();
    }

    @Bean
    public BillingServiceGrpc.BillingServiceBlockingStub billingServiceBlockingStub(ManagedChannel channel) {
        return newBlockingStub(channel);
    }
}
