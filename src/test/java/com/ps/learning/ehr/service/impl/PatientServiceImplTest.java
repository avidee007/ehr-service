package com.ps.learning.ehr.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import com.ps.learning.ehr.event.KafkaEventProducer;
import com.ps.learning.ehr.event.PatientUpdatedEvent;
import com.ps.learning.ehr.exception.PatientNotFoundException;
import com.ps.learning.ehr.model.Address;
import com.ps.learning.ehr.model.Patient;
import com.ps.learning.ehr.model.Prescription;
import com.ps.learning.ehr.model.Treatment;
import com.ps.learning.ehr.repository.AddressEntity;
import com.ps.learning.ehr.repository.PatientEntity;
import com.ps.learning.ehr.repository.PatientEntityAssembler;
import com.ps.learning.ehr.repository.PatientRepository;
import com.ps.learning.ehr.repository.PrescriptionEntity;
import com.ps.learning.ehr.repository.TreatmentEntity;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PatientServiceImplTest {

  @Mock
  private PatientRepository repository;

  @Mock
  private KafkaEventProducer eventProducer;

  private final PatientEntityAssembler assembler = new PatientEntityAssembler();


  @InjectMocks
  private PatientServiceImpl patientService;

  @BeforeEach
  public void setUp() {
    patientService = new PatientServiceImpl(repository, assembler, eventProducer);
  }

  @Test
  void test_savePatient() {
    var patient = getPatient();
    var patientEntity = getPatientEntity();

    when(repository.save(any(PatientEntity.class))).thenReturn(patientEntity);

    Patient result = patientService.savePatient(patient);

    assertNotNull(result);
    assertEquals(result.id(), patientEntity.getId());

  }

  @Test
  void test_getPatient() {
    var patientEntity = getPatientEntity();

    when(repository.findById(anyLong())).thenReturn(Optional.of(patientEntity));

    Patient result = patientService.getPatientById(1L);

    assertNotNull(result);
    assertEquals(result.id(), patientEntity.getId());

  }

  @Test
  void test_getPatient_not_found() {

    when(repository.findById(anyLong())).thenReturn(Optional.empty());

    assertThrows(PatientNotFoundException.class, () -> patientService.getPatientById(1L));


  }

  @Test
  void test_updatePatient() {
    var patient = getUpdatePatient();
    var patientEntity = getPatientEntity();


    when(repository.findById(anyLong())).thenReturn(Optional.of(patientEntity));
    when(repository.save(any(PatientEntity.class))).thenReturn(patientEntity);
    doNothing().when(eventProducer).sendMessage(any(PatientUpdatedEvent.class));

    Patient result = patientService.updatePatient(1L, patient);

    assertNotNull(result);
    assertEquals(result.id(), patientEntity.getId());
    assertEquals(result.name(), patient.name());

  }

  @Test
  void test_updatePatient_not_found() {
    var updatedPatient = getUpdatePatient();

    when(repository.findById(anyLong())).thenReturn(Optional.empty());

    assertThrows(PatientNotFoundException.class, () -> patientService.updatePatient(1L, updatedPatient));


  }


  @Test
  void test_updateTreatmentHistory() {
    var treatment = getUpdateTreatment();
    var patientEntity = getPatientEntity();


    when(repository.findById(anyLong())).thenReturn(Optional.of(patientEntity));
    when(repository.save(any(PatientEntity.class))).thenReturn(patientEntity);
    doNothing().when(eventProducer).sendMessage(any(PatientUpdatedEvent.class));

    Patient result = patientService.updateTreatmentHistory(1L, treatment);

    assertNotNull(result);
    assertEquals(result.id(), patientEntity.getId());

  }

  @Test
  void test_updateTreatmentHistory_not_found() {
    var updateTreatment = getUpdateTreatment();

    when(repository.findById(anyLong())).thenReturn(Optional.empty());

    assertThrows(PatientNotFoundException.class, () -> patientService.updateTreatmentHistory(1L, updateTreatment));


  }

  private Patient getPatient() {
    var prescription = new Prescription("Test", 20, 3);
    var address = new Address("50A", "Locality", "Street", "DHN", "JH", 826001);
    var treatment = new Treatment(12, "Doctor", List.of(prescription), LocalDateTime.now());
    return new Patient(1, "test", 30, address, List.of(treatment));
  }

  private Patient getUpdatePatient() {
    var address = new Address("50A", "Locality", "Street", "DHN", "JH", 826001);
    return new Patient(1, "Updated Name", 40, address, null);
  }

  private Treatment getUpdateTreatment() {
    var prescription = new Prescription("Test", 20, 3);
    return new Treatment(12, "Doctor", List.of(prescription), LocalDateTime.now());
  }

  private PatientEntity getPatientEntity() {

    var addressEntity = getAddressEntity();
    var prescriptionEntity = getPrescriptionEntity();
    HashSet<PrescriptionEntity> prescriptionEntities = new HashSet<>();
    prescriptionEntities.add(prescriptionEntity);
    HashSet<TreatmentEntity> treatmentEntities = getTreatmentEntities(prescriptionEntities);

    var patientEntity = new PatientEntity();
    patientEntity.setId(1L);
    patientEntity.setName("test");
    patientEntity.setAge(30);
    patientEntity.setAddressEntity(addressEntity);
    patientEntity.setTreatmentEntities(treatmentEntities);
    return patientEntity;
  }

  private HashSet<TreatmentEntity> getTreatmentEntities(
      HashSet<PrescriptionEntity> prescriptionEntities) {
    var treatmentEntity = new TreatmentEntity();
    treatmentEntity.setId(1L);
    treatmentEntity.setAppointmentId(121L);
    treatmentEntity.setDoctorName("Dr Test");
    treatmentEntity.setDatetime(LocalDateTime.now());
    treatmentEntity.setPatientEntity(new PatientEntity());
    treatmentEntity.setPrescriptionEntities(prescriptionEntities);

    HashSet<TreatmentEntity> treatmentEntities = new HashSet<>();
    treatmentEntities.add(treatmentEntity);
    return treatmentEntities;
  }

  private PrescriptionEntity getPrescriptionEntity() {
    var prescriptionEntity = new PrescriptionEntity();
    prescriptionEntity.setId(1L);
    prescriptionEntity.setMedicineName("Medicine name");
    prescriptionEntity.setQuantity(10);
    prescriptionEntity.setDose(3);
    prescriptionEntity.setTreatmentEntity(new TreatmentEntity());
    return prescriptionEntity;
  }

  private AddressEntity getAddressEntity() {
    var addressEntity = new AddressEntity();
    addressEntity.setId(1L);
    addressEntity.setHouseNumber("50A");
    addressEntity.setLocality("locality");
    addressEntity.setStreet("Street");
    addressEntity.setDistrict("DHN");
    addressEntity.setState("JH");
    addressEntity.setZip(826001);
    return addressEntity;
  }


}