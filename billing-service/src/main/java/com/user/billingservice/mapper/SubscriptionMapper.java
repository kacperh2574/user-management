package com.user.billingservice.mapper;

import com.user.billingservice.dto.SubscriptionRequestDTO;
import com.user.billingservice.dto.SubscriptionResponseDTO;
import com.user.billingservice.model.Subscription;
import com.user.billingservice.model.SubscriptionStatus;

import java.time.LocalDate;
import java.util.UUID;

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

    public static Subscription toModel(UUID userID, SubscriptionRequestDTO subscriptionRequestDTO) {
        return Subscription.builder()
                .userId(userID)
                .plan(subscriptionRequestDTO.getPlanType())
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusMonths(1))
                .status(SubscriptionStatus.ACTIVE)
                .build();
    }
}
