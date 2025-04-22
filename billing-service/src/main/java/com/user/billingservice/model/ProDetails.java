package com.user.billingservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class ProDetails {

    @Enumerated(EnumType.STRING)
    @Column(name = "pro_status")
    private Status status;

    @Column(name = "pro_start_date")
    private LocalDate startDate;

    @Column(name = "pro_end_date")
    private LocalDate endDate;
}
