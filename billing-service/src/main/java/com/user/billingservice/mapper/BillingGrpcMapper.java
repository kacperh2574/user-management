package com.user.billingservice.mapper;

import billing.BillingResponse;
import com.user.billingservice.dto.SubscriptionResponseDTO;

public class BillingGrpcMapper {

    public static BillingResponse toBillingResponse(SubscriptionResponseDTO subscriptionResponseDTO) {
        return BillingResponse.newBuilder()
                .setSubscriptionId(subscriptionResponseDTO.getId())
                .setPlanType(subscriptionResponseDTO.getPlanType().toString())
                .build();
    }
}
