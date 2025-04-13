package com.user.billingservice.dto;

import com.user.billingservice.model.PlanType;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SubscriptionRequestDTO {

    @NotBlank(message = "Plan type is required")
    private PlanType planType;
}
