package com.user.billingservice.service;

import com.user.billingservice.dto.SubscriptionResponseDTO;
import com.user.billingservice.exception.SubscriptionNotFoundException;
import com.user.billingservice.mapper.SubscriptionMapper;
import com.user.billingservice.model.PlanType;
import com.user.billingservice.model.Status;
import com.user.billingservice.model.Subscription;
import com.user.billingservice.repository.SubscriptionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;
import java.util.List;

import static com.user.billingservice.util.TestDataUtil.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SubscriptionServiceTest {

    @Mock
    SubscriptionRepository subscriptionRepository;

    @InjectMocks
    SubscriptionService subscriptionService;

    Subscription SUBSCRIPTION = createFreeSubscription(UUID.randomUUID());

    @BeforeEach
    void setUp() {
        subscriptionService = new SubscriptionService(subscriptionRepository);
    }

    @Test
    void createSubscription_returnsCreatedSubscription() {
        when(subscriptionRepository.save(any(Subscription.class))).thenReturn(SUBSCRIPTION);

        SubscriptionResponseDTO subscriptionResponse = subscriptionService.createSubscription(USER_ID);

        assertThat(subscriptionResponse)
                .usingRecursiveComparison()
                .isEqualTo(SubscriptionMapper.toDTO(SUBSCRIPTION));
    }

    @Test
    void getSubscription_returnsSubscription_whenSubscriptionExists() {
        when(subscriptionRepository.findByUserId(USER_ID)).thenReturn(Optional.of(SUBSCRIPTION));

        SubscriptionResponseDTO subscriptionResponse = subscriptionService.getSubscription(USER_ID);

        assertThat(subscriptionResponse)
                .usingRecursiveComparison()
                .isEqualTo(SubscriptionMapper.toDTO(SUBSCRIPTION));
    }

    @Test
    void getSubscription_throwsException_whenSubscriptionDoesNotExist() {
        when(subscriptionRepository.findByUserId(USER_ID)).thenReturn(Optional.empty());

        assertThrows(SubscriptionNotFoundException.class, () -> subscriptionService.getSubscription(USER_ID));
    }

    @Test
    void getSubscriptions_returnsAllSubscriptions() {
        List<Subscription> subscriptions = List.of(SUBSCRIPTION);
        when(subscriptionRepository.findAll()).thenReturn(subscriptions);

        List<SubscriptionResponseDTO> subscriptionsResponse = subscriptionService.getSubscriptions();

        assertThat(subscriptionsResponse)
                .usingRecursiveComparison()
                .isEqualTo(subscriptions.stream().map(SubscriptionMapper::toDTO).toList());
    }

    @Test
    void cancelProSubscription_returnsSubscriptionWithFreePlan_andCancelledStatus() {
        Subscription cancelledSubscription = createCancelledSubscription();

        when(subscriptionRepository.findByUserId(USER_ID)).thenReturn(Optional.of(SUBSCRIPTION));
        when(subscriptionRepository.save(any(Subscription.class))).thenReturn(cancelledSubscription);

        SubscriptionResponseDTO subscriptionResponse = subscriptionService.cancelProSubscription(USER_ID);

        assertEquals(PlanType.FREE, subscriptionResponse.getPlanType());
        assertEquals(Status.CANCELLED, subscriptionResponse.getProDetails().getStatus());
    }

    @Test
    void cancelProSubscription_throwsException_whenSubscriptionDoesNotExist() {
        when(subscriptionRepository.findByUserId(USER_ID)).thenReturn(Optional.empty());

        assertThrows(SubscriptionNotFoundException.class, () -> subscriptionService.cancelProSubscription(USER_ID));
    }

    @Test
    void upgradeSubscriptionToPRO_returnsSubscriptionWithProPlan_andActiveStatus() {
        Subscription proSubscription = createProSubscription();

        when(subscriptionRepository.findByUserId(USER_ID)).thenReturn(Optional.of(SUBSCRIPTION));
        when(subscriptionRepository.save(any(Subscription.class))).thenReturn(proSubscription);

        SubscriptionResponseDTO subscriptionResponse = subscriptionService.upgradeSubscriptionToPRO(USER_ID);

        assertEquals(PlanType.PRO, subscriptionResponse.getPlanType());
        assertEquals(Status.ACTIVE, subscriptionResponse.getProDetails().getStatus());
    }

    @Test
    void upgradeSubscriptionToPRO_throwsException_whenSubscriptionDoesNotExist() {
        when(subscriptionRepository.findByUserId(USER_ID)).thenReturn(Optional.empty());

        assertThrows(SubscriptionNotFoundException.class, () -> subscriptionService.upgradeSubscriptionToPRO(USER_ID));
    }

    @Test
    void downgradeExpiredProSubscription_returnsIdsOfSavedSubscriptionsWithFreePlan_andExpiredStatus() {
        Subscription expiredSubscription = createExpiredSubscription();

        when(subscriptionRepository.findAllByProDetails_EndDateBeforeAndPlanType(LocalDate.now(), PlanType.PRO))
                .thenReturn(List.of(expiredSubscription));

        List<String> downgradedIds = subscriptionService.downgradeExpiredProSubscriptions();

        assertEquals(1, downgradedIds.size());
        assertEquals(expiredSubscription.getId().toString(), downgradedIds.getFirst());

        ArgumentCaptor<Subscription> captor = ArgumentCaptor.forClass(Subscription.class);
        verify(subscriptionRepository).save(captor.capture());

        Subscription downgradedSubscription = captor.getValue();
        assertThat(downgradedSubscription.getPlanType()).isEqualTo(PlanType.FREE);
        assertThat(downgradedSubscription.getProDetails().getStatus()).isEqualTo(Status.EXPIRED);
    }
}