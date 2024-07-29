package com.ps.learning.ehr.repository;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Setter
@Getter
public class TreatmentEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private Long appointmentId;
  private String doctorName;
  private LocalDateTime datetime;

  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "patient_id")
  private PatientEntity patientEntity;

  @OneToMany(mappedBy = "treatmentEntity", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<PrescriptionEntity> prescriptionEntities = new HashSet<>();

}
