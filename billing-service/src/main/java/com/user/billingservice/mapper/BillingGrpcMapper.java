package com.user.billingservice.mapper;

import billing.CreateSubscriptionResponse;
import com.user.billingservice.dto.SubscriptionResponseDTO;

public class BillingGrpcMapper {

    public static CreateSubscriptionResponse toCreateSubscriptionResponse(SubscriptionResponseDTO subscriptionResponseDTO) {
        return CreateSubscriptionResponse.newBuilder()
                .setSubscriptionId(subscriptionResponseDTO.getId())
                .setPlanType(subscriptionResponseDTO.getPlanType().toString())
                .build();
    }
}
