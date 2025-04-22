package com.user.billingservice.model;

import jakarta.validation.constraints.NotNull;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "subscriptions")
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NotNull
    @Column(nullable = false)
    private UUID userId;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PlanType planType;

    @Embedded
    private ProDetails proDetails;

    @Column(nullable = false)
    private LocalDate createdAt;
}