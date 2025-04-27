package com.user.billingservice.service;

import com.user.billingservice.dto.SubscriptionResponseDTO;
import com.user.billingservice.exception.SubscriptionNotFoundException;
import com.user.billingservice.mapper.SubscriptionMapper;
import com.user.billingservice.model.PlanType;
import com.user.billingservice.model.ProDetails;
import com.user.billingservice.model.Subscription;
import com.user.billingservice.model.Status;
import com.user.billingservice.repository.SubscriptionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;

    public SubscriptionService(SubscriptionRepository subscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository;
    }

    public SubscriptionResponseDTO createSubscription(UUID userId) {
        Subscription newSubscription = subscriptionRepository.save(SubscriptionMapper.toModel(userId));

        return SubscriptionMapper.toDTO(newSubscription);
    }

    public SubscriptionResponseDTO getSubscription(UUID userId) {
        Subscription subscription = findSubscriptionByUserId(userId);

        return SubscriptionMapper.toDTO(subscription);
    }

    public List<SubscriptionResponseDTO> getSubscriptions() {
        return subscriptionRepository
                .findAll()
                .stream()
                .map(SubscriptionMapper::toDTO)
                .toList();
    }

    public SubscriptionResponseDTO cancelProSubscription(UUID userId) {
        Subscription subscription = findSubscriptionByUserId(userId);

        subscription.setPlanType(PlanType.FREE);
        subscription.setProDetails(ProDetails.builder()
                .status(Status.CANCELLED)
                .build()
        );

        subscriptionRepository.save(subscription);

        return SubscriptionMapper.toDTO(subscription);
    }

    public SubscriptionResponseDTO upgradeSubscriptionToPRO(UUID userId) {
        Subscription subscription = findSubscriptionByUserId(userId);

        subscription.setPlanType(PlanType.PRO);
        subscription.setProDetails(ProDetails.builder()
                .status(Status.ACTIVE)
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusMonths(1))
                .build()
        );

        subscriptionRepository.save(subscription);

        return SubscriptionMapper.toDTO(subscription);
    }

    public List<String> downgradeExpiredProSubscriptions() {
        List<Subscription> expired = findSubscriptionsByEndDateAndPlan(LocalDate.now(), PlanType.PRO);

        List<String> expiredIds = new ArrayList<>();

        expired.forEach(subscription -> {
            Subscription downgraded = subscription.toBuilder()
                    .planType(PlanType.FREE)
                    .proDetails(ProDetails.builder()
                            .status(Status.EXPIRED)
                            .build())
                    .build();

            subscriptionRepository.save(downgraded);

            expiredIds.add(downgraded.getId().toString());
        });

        return expiredIds;
    }

    private Subscription findSubscriptionByUserId(UUID userId) {
        return subscriptionRepository
                .findByUserId(userId)
                .orElseThrow(() -> new SubscriptionNotFoundException("A subscription for a user with this ID not found: " + userId));
    }

    private List<Subscription> findSubscriptionsByEndDateAndPlan(LocalDate endDate, PlanType plan) {
        return subscriptionRepository.findAllByProDetails_EndDateBeforeAndPlanType(endDate, plan);
    }
}