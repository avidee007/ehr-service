package com.ps.learning.ehr.repository;

import com.ps.learning.ehr.model.Address;
import com.ps.learning.ehr.model.Patient;
import com.ps.learning.ehr.model.Prescription;
import com.ps.learning.ehr.model.Treatment;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PatientEntityAssembler {

  public PatientEntity toEntity(Patient patient) {
    log.info("Converting patient domain to entity");
    PatientEntity entity = new PatientEntity();
    entity.setName(patient.name());
    entity.setAge(patient.age());
    entity.setAddressEntity(toAddressEntity(patient.address()));
    entity.setTreatmentEntities(getTreatmentEntities(patient, entity));
    log.debug("Converting patient domain to entity: {}",entity);
    return entity;
  }

  public Patient fromEntity(PatientEntity patientEntity) {
    log.info("Converting entity entity to domain");
    return new Patient(patientEntity.getId(),
        patientEntity.getName(),
        patientEntity.getAge(),
        fromAddressEntity(patientEntity.getAddressEntity()),
        patientEntity.getTreatmentEntities().stream()
            .map(this::fromTreatmentEntity)
            .toList()
    );
  }

  private Set<TreatmentEntity> getTreatmentEntities(Patient patient, PatientEntity entity) {
    return patient.treatmentHistory().stream()
        .map(treatment -> toTreatmentEntity(treatment, entity))
        .collect(Collectors.toSet());
  }

  private AddressEntity toAddressEntity(Address address) {
    AddressEntity addressEntity = new AddressEntity();
    addressEntity.setHouseNumber(address.houseNumber());
    addressEntity.setLocality(address.locality());
    addressEntity.setStreet(address.street());
    addressEntity.setDistrict(address.district());
    addressEntity.setState(address.state());
    addressEntity.setZip(address.zip());
    return addressEntity;
  }

  private Address fromAddressEntity(AddressEntity addressEntity) {
    return new Address(
        addressEntity.getHouseNumber(),
        addressEntity.getLocality(),
        addressEntity.getStreet(),
        addressEntity.getDistrict(),
        addressEntity.getState(),
        addressEntity.getZip()
    );
  }

  private TreatmentEntity toTreatmentEntity(Treatment treatment,
                                            PatientEntity patientEntity) {
    TreatmentEntity entity = new TreatmentEntity();
    entity.setAppointmentId(treatment.appointmentId());
    entity.setDoctorName(treatment.doctorName());
    entity.setDatetime(treatment.datetime());
    entity.setPatientEntity(patientEntity);
    entity.setPrescriptionEntities(getPrescriptionEntities(treatment, entity));
    return entity;
  }

  private Set<PrescriptionEntity> getPrescriptionEntities(Treatment treatment,
                                                          TreatmentEntity treatmentEntity) {
    return treatment.prescriptions().stream()
        .map(prescription -> toPrescriptionEntity(prescription, treatmentEntity))
        .collect(Collectors.toSet());
  }

  private PrescriptionEntity toPrescriptionEntity(Prescription prescription,
                                                  TreatmentEntity treatmentEntity) {
    PrescriptionEntity entity = new PrescriptionEntity();
    entity.setMedicineName(prescription.medicineName());
    entity.setQuantity(prescription.quantity());
    entity.setDose(prescription.dose());
    entity.setTreatmentEntity(treatmentEntity);
    return entity;
  }

  private Treatment fromTreatmentEntity(TreatmentEntity treatmentEntity) {
    return new Treatment(
        treatmentEntity.getAppointmentId(),
        treatmentEntity.getDoctorName(),
        treatmentEntity.getPrescriptionEntities().stream()
            .map(this::fromPrescriptionEntity)
            .toList(),
        treatmentEntity.getDatetime()
    );
  }

  private Prescription fromPrescriptionEntity(PrescriptionEntity prescriptionEntity) {
    return new Prescription(
        prescriptionEntity.getMedicineName(),
        prescriptionEntity.getQuantity(),
        prescriptionEntity.getDose()
    );
  }

  public PatientEntity updatePatient(PatientEntity savedEntity, Patient patient) {
    log.info("Updating patient entity");
    savedEntity.setName(patient.name());
    savedEntity.setAge(patient.age());
    savedEntity.setAddressEntity(toAddressEntity(patient.address()));
    log.debug("Updated patient : {}",savedEntity);
    return savedEntity;


  }

  public PatientEntity updateTreatmentHistory(PatientEntity savedEntity, Treatment treatment) {
    log.info("Updating patient's treatment history");
    Set<TreatmentEntity> treatmentEntities = savedEntity.getTreatmentEntities();
    treatmentEntities.add(toTreatmentEntity(treatment, savedEntity));
    log.debug("Updated patient's treatment history {}",treatmentEntities);
    return savedEntity;


  }
}
