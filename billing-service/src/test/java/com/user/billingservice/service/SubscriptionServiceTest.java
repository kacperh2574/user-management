package com.user.billingservice.service;

import com.user.billingservice.dto.SubscriptionRequestDTO;
import com.user.billingservice.dto.SubscriptionResponseDTO;
import com.user.billingservice.exception.SubscriptionNotFoundException;
import com.user.billingservice.mapper.SubscriptionMapper;
import com.user.billingservice.model.PlanType;
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

import static com.user.billingservice.util.TestDataUtil.createSubscription;
import static com.user.billingservice.util.TestDataUtil.createSubscriptionRequestDTO;
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

    UUID userId = UUID.fromString("123e4567-e89b-12d3-a456-426614174001");

    Subscription subscription = createSubscription(UUID.randomUUID(), userId, PlanType.PRO);
    SubscriptionRequestDTO subscriptionRequestDTO = createSubscriptionRequestDTO();

    @BeforeEach
    void setUp() {
        subscriptionService = new SubscriptionService(subscriptionRepository);
    }

    @Test
    void createSubscription_returnsCreatedSubscription() {
        when(subscriptionRepository.save(any(Subscription.class))).thenReturn(subscription);

        SubscriptionResponseDTO subscriptionResponse = subscriptionService.createSubscription(userId, subscriptionRequestDTO);

        assertThat(subscriptionResponse)
                .usingRecursiveComparison()
                .isEqualTo(SubscriptionMapper.toDTO(subscription));
    }

    @Test
    void getSubscription_returnsSubscription_whenSubscriptionExists() {
        when(subscriptionRepository.findByUserId(userId)).thenReturn(Optional.of(subscription));

        SubscriptionResponseDTO subscriptionResponse = subscriptionService.getSubscription(userId);

        assertThat(subscriptionResponse)
                .usingRecursiveComparison()
                .isEqualTo(SubscriptionMapper.toDTO(subscription));
    }

    @Test
    void getSubscription_throwsException_whenSubscriptionDoesNotExist() {
        when(subscriptionRepository.findByUserId(userId)).thenReturn(Optional.empty());

        assertThrows(SubscriptionNotFoundException.class, () -> subscriptionService.getSubscription(userId));
    }

    @Test
    void getSubscriptions_returnsAllSubscriptions() {
        List<Subscription> subscriptions = List.of(subscription);
        when(subscriptionRepository.findAll()).thenReturn(subscriptions);

        List<SubscriptionResponseDTO> subscriptionsResponse = subscriptionService.getSubscriptions();

        assertThat(subscriptionsResponse)
                .usingRecursiveComparison()
                .isEqualTo(subscriptions.stream().map(SubscriptionMapper::toDTO).toList());
    }

    @Test
    void upgradeToProSubscription_returnsSubscriptionWithUpgradedPlan() {
        when(subscriptionRepository.findByUserId(userId)).thenReturn(Optional.of(subscription));
        subscription.setPlan(PlanType.PRO);
        when(subscriptionRepository.save(any(Subscription.class))).thenReturn(subscription);

        subscriptionService.upgradeSubscriptionToPRO(userId);

        ArgumentCaptor<Subscription> captor = ArgumentCaptor.forClass(Subscription.class);

        verify(subscriptionRepository).save(captor.capture());

        Subscription savedSubscription = captor.getValue();

        assertEquals(PlanType.PRO, savedSubscription.getPlan());
    }

    @Test
    void upgradeToProSubscription_throwsException_whenSubscriptionDoesNotExist() {
        when(subscriptionRepository.findByUserId(userId)).thenReturn(Optional.empty());

        assertThrows(SubscriptionNotFoundException.class, () -> subscriptionService.upgradeSubscriptionToPRO(userId));
    }

    @Test
    void downgradeExpiredProSubscriptions_returnsIdsOfSavedSubscriptionsWithDowngradedPlan() {
        when(subscriptionRepository.findAllByEndDateBeforeAndPlan(LocalDate.now(), PlanType.PRO))
                .thenReturn(List.of(subscription));

        List<String> downgradedIds = subscriptionService.downgradeExpiredProSubscriptions();

        assertEquals(1, downgradedIds.size());
        assertEquals(subscription.getId().toString(), downgradedIds.getFirst());

        ArgumentCaptor<Subscription> captor = ArgumentCaptor.forClass(Subscription.class);
        verify(subscriptionRepository).save(captor.capture());

        Subscription savedSubscription = captor.getValue();
        assertThat(savedSubscription.getPlan()).isEqualTo(PlanType.FREE);
        assertThat(savedSubscription.getEndDate()).isNull();
    }
}