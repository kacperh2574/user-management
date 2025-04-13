package com.user.billingservice.mapper;

import com.user.billingservice.dto.SubscriptionRequestDTO;
import com.user.billingservice.dto.SubscriptionResponseDTO;
import com.user.billingservice.model.Subscription;
import com.user.billingservice.model.SubscriptionStatus;

import java.time.LocalDate;

public class SubscriptionMapper {

    public static SubscriptionResponseDTO toDTO(Subscription subscription) {
        return SubscriptionResponseDTO.builder()
                .id(subscription.getId().toString())
                .plan(subscription.getPlan())
                .status(subscription.getStatus())
                .startDate(subscription.getStartDate())
                .endDate(subscription.getEndDate())
                .build();
    }

    public static Subscription toModel(SubscriptionRequestDTO subscriptionRequestDTO) {
        return Subscription.builder()
                .plan(subscriptionRequestDTO.getPlanType())
                .startDate(LocalDate.now())
                .status(SubscriptionStatus.ACTIVE)
                .build();
    }
}
