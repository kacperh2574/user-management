package com.user.billingservice.scheduler;

import com.user.billingservice.service.SubscriptionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SubscriptionSchedulerTest {

    @Mock
    SubscriptionService subscriptionService;

    @InjectMocks
    SubscriptionScheduler subscriptionScheduler;

    @Test
    void downgradeExpiredProSubscriptions() {
        List<String> downgradedIds = List.of("id-1", "id-2");

        when(subscriptionService.downgradeExpiredProSubscriptions()).thenReturn(downgradedIds);

        subscriptionScheduler.downgradeExpiredProSubscriptions();

        verify(subscriptionService, times(1)).downgradeExpiredProSubscriptions();
    }
}
