package com.ps.learning.ehr.controller;

import com.ps.learning.ehr.model.Address;
import com.ps.learning.ehr.model.Patient;
import com.ps.learning.ehr.model.Prescription;
import com.ps.learning.ehr.model.Treatment;
import com.ps.learning.ehr.service.PatientService;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

@ExtendWith(MockitoExtension.class)
class PatientControllerTest {
  @Mock
  private PatientService patientService;
  @InjectMocks
  private PatientController patientController;

  @Test
  void savePatient() {
    var patient = getPatient();

    Mockito.when(patientService.savePatient(Mockito.any(Patient.class))).thenReturn(patient);

    var responseEntityMono = patientController.savePatient(patient);

    responseEntityMono.subscribe(responseEntity ->{
      Assertions.assertEquals(HttpStatus.OK,responseEntity.getStatusCode());

    });



  }

  @Test
  void getPatientById() {
    var patient = getPatient();

    Mockito.when(patientService.getPatientById(Mockito.anyLong())).thenReturn(patient);

    var responseEntityMono = patientController.getPatientById(1L);

    responseEntityMono.subscribe(responseEntity ->{
      Assertions.assertEquals(HttpStatus.OK,responseEntity.getStatusCode());

    });
  }

  @Test
  void updatePatient() {
    var patient = getPatient();

    Mockito.when(patientService.updatePatient(Mockito.anyLong(),Mockito.any(Patient.class))).thenReturn(patient);

    var responseEntityMono = patientController.updatePatient(1L,patient);

    responseEntityMono.subscribe(responseEntity ->{
      Assertions.assertEquals(HttpStatus.OK,responseEntity.getStatusCode());

    });
  }

  @Test
  void addTreatment() {
    var updateTreatment = getUpdateTreatment();

    Mockito.when(patientService.updateTreatmentHistory(Mockito.anyLong(),Mockito.any(Treatment.class))).thenReturn(
        getPatient());

    var responseEntityMono = patientController.addTreatment(1L, updateTreatment);

    responseEntityMono.subscribe(responseEntity ->{
      Assertions.assertEquals(HttpStatus.OK,responseEntity.getStatusCode());

    });
  }

  private Patient getPatient() {
    var prescription = new Prescription("Test", 20, 3);
    var address = new Address("50A", "Locality", "Street", "DHN", "JH", 826001);
    var treatment = new Treatment(12, "Doctor", List.of(prescription), LocalDateTime.now());
    return new Patient(1, "test", 30, address, List.of(treatment));
  }

  private Treatment getUpdateTreatment() {
    var prescription = new Prescription("Test", 20, 3);
    return new Treatment(12, "Doctor", List.of(prescription), LocalDateTime.now());
  }
}