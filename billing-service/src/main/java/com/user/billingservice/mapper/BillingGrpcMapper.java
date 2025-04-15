package com.user.billingservice.mapper;

import billing.BillingRequest;
import billing.BillingResponse;
import com.user.billingservice.dto.SubscriptionRequestDTO;
import com.user.billingservice.dto.SubscriptionResponseDTO;
import com.user.billingservice.model.PlanType;

public class BillingGrpcMapper {

    public static SubscriptionRequestDTO toSubscriptionRequestDTO(BillingRequest request) {
        return SubscriptionRequestDTO.builder()
                .planType(PlanType.valueOf(request.getPlanType()))
                .build();
    }

    public static BillingResponse toBillingResponse(SubscriptionResponseDTO subscriptionResponseDTO) {
        return BillingResponse.newBuilder()
                .setSubscriptionId(subscriptionResponseDTO.getId())
                .setStatus(subscriptionResponseDTO.getStatus().toString())
                .build();
    }
}
