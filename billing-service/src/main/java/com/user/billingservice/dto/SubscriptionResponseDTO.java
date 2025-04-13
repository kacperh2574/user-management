package com.user.billingservice.dto;

import com.user.billingservice.model.PlanType;
import com.user.billingservice.model.SubscriptionStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class SubscriptionResponseDTO {

    private String id;
    private PlanType plan;
    private SubscriptionStatus status;
    private LocalDate startDate;
    private LocalDate endDate;
}
