package com.user.analyticsservice.kafka;

import com.google.protobuf.InvalidProtocolBufferException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import user.events.UserEvent;

@Service
public class KafkaConsumer {

    private static final Logger log = LoggerFactory.getLogger(KafkaConsumer.class);

    @KafkaListener(topics = "user", groupId = "analytics-service")
    public void consumeEvent(byte[] event) {
        try {
            UserEvent userEvent = UserEvent.parseFrom(event);

            log.info("Received user event: [userId: {}, name: {}, email: {}]",
                    userEvent.getUserId(),userEvent.getName(), userEvent.getEmail()
            );
        } catch (InvalidProtocolBufferException e) {
            log.error("Error deserializing user event: {}", e.getMessage());
        }
    }
}
