package com.ps.learning.ehr.controller;

import com.ps.learning.ehr.model.ErrorResponse;
import com.ps.learning.ehr.model.Patient;
import com.ps.learning.ehr.model.Treatment;
import com.ps.learning.ehr.service.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/patients")
@Slf4j
public class PatientController {
  private final PatientService patientService;

  @PostMapping
  @Operation(summary = "Create new patient record.")
  @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Patient.class)))
  @ApiResponse(responseCode = "401", description = "Un-authorize Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
  @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
  @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
  Mono<ResponseEntity<Patient>> savePatient(@RequestBody Patient patient) {
    log.trace("Save patient request received");
    log.debug("Save patient request payload :  {}",patient);
    return Mono.fromCallable(
        () -> ResponseEntity.ok(patientService.savePatient(patient)));
  }

  @GetMapping("{id}")
  @Operation(summary = "Get patient by id.")
  @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Patient.class)))
  @ApiResponse(responseCode = "401", description = "Un-authorize Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
  @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))

  @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
  Mono<ResponseEntity<Patient>> getPatientById(@PathVariable("id") long id) {
    log.trace("Get patient request received");
    log.debug("Get patient id :  {}",id);
    return Mono.fromCallable(
        () -> ResponseEntity.ok(patientService.getPatientById(id)));
  }

  @PutMapping("{id}")
  @Operation(summary = "Update patient record by id.")
  @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Patient.class)))
  @ApiResponse(responseCode = "401", description = "Un-authorize Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
  @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))

  @PreAuthorize("hasRole('ADMIN')")
  Mono<ResponseEntity<Patient>> updatePatient(@PathVariable("id") long id,
                                              @RequestBody Patient patient) {
    log.trace("Update patient request received");
    log.debug("Update patient request payload :  {}",patient);
    return Mono.fromCallable(
        () -> ResponseEntity.ok(patientService.updatePatient(id, patient)));
  }

  @PutMapping("{id}/treatments")
  @Operation(summary = "Create new treatment history to a patient by id.")
  @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Patient.class)))
  @ApiResponse(responseCode = "401", description = "Un-authorize Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
  @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))

  @PreAuthorize("hasRole('ADMIN')")
  Mono<ResponseEntity<Patient>> addTreatment(@PathVariable("id") long id,
                                             @RequestBody Treatment treatment) {
    log.trace("Update treatment history request received");
    log.debug("Update treatment history payload :  {}",treatment);
    return Mono.fromCallable(
        () -> ResponseEntity.ok(patientService.updateTreatmentHistory(id, treatment)));
  }


}
