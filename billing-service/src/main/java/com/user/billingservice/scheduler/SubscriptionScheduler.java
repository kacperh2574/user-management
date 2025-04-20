package com.user.billingservice.scheduler;

import com.user.billingservice.service.SubscriptionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SubscriptionScheduler {

    private static final Logger log = LoggerFactory.getLogger(SubscriptionScheduler.class);
    private final SubscriptionService subscriptionService;

    public SubscriptionScheduler(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void downgradeExpiredProSubscriptions() {
        List<String> ids = subscriptionService.downgradeExpiredProSubscriptions();

        log.info("Subscriptions downgraded to FREE plan: {}", ids);
    }
}
