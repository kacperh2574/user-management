package com.user.billingservice.dto;

import com.user.billingservice.model.Status;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class ProDetailsDTO {

    private Status status;
    private LocalDate startDate;
    private LocalDate endDate;
}