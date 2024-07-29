package com.ps.learning.ehr.event;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

@ExtendWith(MockitoExtension.class)
class KafkaEventProducerTest {

  @Mock
  private KafkaTemplate<String, PatientUpdatedEvent> kafkaTemplate;

  @InjectMocks
  private KafkaEventProducer kafkaEventProducer;

  @BeforeEach
  public void setUp() {
    var topicName = "test-topic";
    kafkaEventProducer = new KafkaEventProducer(kafkaTemplate, topicName);

  }

  @Test
  void sendMessage() {
    var updatedPatientEvent = new PatientUpdatedEvent(1L, LocalDateTime.now());
    assertDoesNotThrow(() -> kafkaEventProducer.sendMessage(updatedPatientEvent));
    verify(kafkaTemplate, times(1)).send(anyString(), any(PatientUpdatedEvent.class));
  }
}