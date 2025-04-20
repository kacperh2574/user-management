package com.user.billingservice.repository;

import com.user.billingservice.model.PlanType;
import com.user.billingservice.model.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SubscriptionRepository extends JpaRepository<Subscription, UUID> {
    Optional<Subscription> findByUserId(UUID userId);
    List<Subscription> findAllByEndDateBeforeAndPlan(LocalDate date, PlanType type);
}
