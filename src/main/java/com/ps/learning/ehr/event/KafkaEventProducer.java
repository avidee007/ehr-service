package com.ps.learning.ehr.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaEventProducer {

  private final KafkaTemplate<String, PatientUpdatedEvent> kafkaTemplate;

  private final String topicName;

  public KafkaEventProducer(KafkaTemplate<String, PatientUpdatedEvent> kafkaTemplate,
                            @Value("${kafka.topic.name}")
                            String topicName) {
    this.kafkaTemplate = kafkaTemplate;
    this.topicName = topicName;
  }

  public void sendMessage(PatientUpdatedEvent event) {
    log.info("Sending event: {} to topic: {}", event, topicName);
    kafkaTemplate.send(topicName, event);
    log.info("Event sending successful.");

  }
}
