package com.user.billingservice.mapper;

import com.user.billingservice.dto.ProDetailsDTO;
import com.user.billingservice.dto.SubscriptionRequestDTO;
import com.user.billingservice.dto.SubscriptionResponseDTO;
import com.user.billingservice.model.ProDetails;
import com.user.billingservice.model.Subscription;

import java.time.LocalDate;
import java.util.UUID;

public class SubscriptionMapper {

    public static SubscriptionResponseDTO toDTO(Subscription subscription) {
        return SubscriptionResponseDTO.builder()
                .id(subscription.getId().toString())
                .planType(subscription.getPlanType())
                .createdAt(subscription.getCreatedAt())
                .proDetails(toProDetailsDTO(subscription.getProDetails()))
                .build();
    }

    public static Subscription toModel(UUID userId, SubscriptionRequestDTO subscriptionRequestDTO) {
        return Subscription.builder()
                .userId(userId)
                .planType(subscriptionRequestDTO.getPlanType())
                .createdAt(LocalDate.now())
                .proDetails(null)
                .build();
    }

    private static ProDetailsDTO toProDetailsDTO(ProDetails proDetails) {
        return proDetails == null ? null : ProDetailsDTO.builder()
                .status(proDetails.getStatus())
                .startDate(proDetails.getStartDate())
                .endDate(proDetails.getEndDate())
                .build();
    }
}
