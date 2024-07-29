package com.ps.learning.ehr.service;

import com.ps.learning.ehr.model.Patient;
import com.ps.learning.ehr.model.Treatment;

public interface PatientService {

  Patient savePatient(Patient patient);

  Patient getPatientById(long id);

  Patient updatePatient(long id, Patient patient);

  Patient updateTreatmentHistory(long id, Treatment treatment);
}
