package com.user.billingservice.dto;

import com.user.billingservice.model.PlanType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class SubscriptionResponseDTO {

    private String id;
    private PlanType planType;
    private ProDetailsDTO proDetails;
    private LocalDate createdAt;
}
