package com.user.analyticsservice.kafka;

import com.google.protobuf.InvalidProtocolBufferException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import user.events.UserEvent;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class KafkaConsumerTest {

    private KafkaConsumer kafkaConsumer;

    @BeforeEach
    void setUp() {
        kafkaConsumer = new KafkaConsumer();
    }

    @Test
    void consumeEvent_doesNotThrowException() {
        UserEvent userEvent = UserEvent.newBuilder()
                .setUserId("userId")
                .setName("name")
                .setEmail("email")
                .build();

        byte[] eventBytes = userEvent.toByteArray();

        assertDoesNotThrow(() -> kafkaConsumer.consumeEvent(eventBytes));
    }
}
