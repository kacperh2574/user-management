package com.user.billingservice.mapper;

import billing.CancelSubscriptionResponse;
import billing.CreateSubscriptionResponse;
import com.user.billingservice.dto.SubscriptionResponseDTO;

public class BillingGrpcMapper {

    public static CreateSubscriptionResponse toCreateSubscriptionResponse(SubscriptionResponseDTO subscriptionResponseDTO) {
        return CreateSubscriptionResponse.newBuilder()
                .setSubscriptionId(subscriptionResponseDTO.getId())
                .setPlanType(subscriptionResponseDTO.getPlanType().toString())
                .build();
    }

    public static CancelSubscriptionResponse toCancelSubscriptionResponse(SubscriptionResponseDTO subscriptionResponseDTO) {
        return CancelSubscriptionResponse.newBuilder()
                .setSubscriptionId(subscriptionResponseDTO.getId())
                .setPlanType(subscriptionResponseDTO.getPlanType().toString())
                .setProStatus(subscriptionResponseDTO.getProDetails().getStatus().toString())
                .build();
    }
}
