package com.ps.learning.ehr.service.impl;

import com.ps.learning.ehr.event.KafkaEventProducer;
import com.ps.learning.ehr.event.PatientUpdatedEvent;
import com.ps.learning.ehr.exception.PatientNotFoundException;
import com.ps.learning.ehr.model.Patient;
import com.ps.learning.ehr.model.Treatment;
import com.ps.learning.ehr.repository.PatientEntity;
import com.ps.learning.ehr.repository.PatientEntityAssembler;
import com.ps.learning.ehr.repository.PatientRepository;
import com.ps.learning.ehr.service.PatientService;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PatientServiceImpl implements PatientService {

  private static final String NOT_FOUND_ERROR = "Patient with id : %s not found";

  private final PatientRepository repository;
  private final PatientEntityAssembler assembler;
  private final KafkaEventProducer kafkaEventProducer;


  @Override
  public Patient savePatient(Patient patient) {
    var savedPatient = repository.save(assembler.toEntity(patient));
    log.info("Successfully created patient with id : {}",savedPatient.getId());
    return assembler.fromEntity(savedPatient);
  }

  @Override
  @Cacheable(value = "patients", key = "#id")
  public Patient getPatientById(long id) {
    log.info("Successfully found patient with id : {}",id);
    return repository.findById(id)
        .map(assembler::fromEntity)
        .orElseThrow(() -> new PatientNotFoundException(String.format(NOT_FOUND_ERROR, id)));
  }

  @Override
  @CachePut(value = "patients", key = "#id")
  public Patient updatePatient(long id, Patient patient) {
    log.info("Successfully updated patient with id : {}",id);
    return repository.findById(id).map(savedEntity -> assembler.updatePatient(savedEntity, patient))
        .map(this::saveAndProduceKafkaEvent)
        .orElseThrow(() -> new PatientNotFoundException(String.format(NOT_FOUND_ERROR, id)));

  }

  private Patient saveAndProduceKafkaEvent(PatientEntity value) {
    var updatedPatient = assembler.fromEntity(repository.save(value));
    var event = new PatientUpdatedEvent(updatedPatient.id(), LocalDateTime.now());
    log.info("Patient update event created with id: {} at timestamp : {}", event.id(),
        event.updateTimestamp());
    log.debug("Event created : {}",event);
    kafkaEventProducer.sendMessage(event);
    log.debug("updated patient : {}",updatedPatient);
    return updatedPatient;
  }

  @Override
  @CachePut(value = "patients", key = "#id")
  public Patient updateTreatmentHistory(long id, Treatment treatment) {
    log.info("Successfully updated patient treatment history with id : {}",id);
    return repository.findById(id)
        .map(savedEntity -> assembler.updateTreatmentHistory(savedEntity, treatment))
        .map(this::saveAndProduceKafkaEvent)
        .orElseThrow(() -> new PatientNotFoundException(String.format(NOT_FOUND_ERROR, id)));
  }
}
