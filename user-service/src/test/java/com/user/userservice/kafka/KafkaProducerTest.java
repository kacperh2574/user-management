package com.user.userservice.kafka;

import com.user.userservice.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KafkaProducerTest {

    @Mock
    private KafkaTemplate<String, byte[]> kafkaTemplate;

    @InjectMocks
    private KafkaProducer kafkaProducer;

    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(UUID.randomUUID())
                .name("name")
                .email("email")
                .build();
    }

    @Test
    void sendEvent_sendsMessageToKafka() {
        kafkaProducer.sendEvent(user);

        verify(kafkaTemplate, times(1)).send(eq("user"), any(byte[].class));
    }

    @Test
    void sendEvent_throwsException() {
        doThrow(new RuntimeException("Error sending event"))
                .when(kafkaTemplate)
                .send(eq("user"), any(byte[].class));

        kafkaProducer.sendEvent(user);

        verify(kafkaTemplate, times(1)).send(eq("user"), any(byte[].class));
    }
}
