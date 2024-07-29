package com.ps.learning.ehr.exception;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

class GlobalExceptionHandlerTest {

  private final GlobalExceptionHandler exceptionHandler = new GlobalExceptionHandler();

  @Test
  void handlePatientNotFoundException() {
    var patientNotFoundException = new PatientNotFoundException("Patient with id : 1 not found.");

    var errorResponse = exceptionHandler.handlePatientNotFoundException(patientNotFoundException);

    Assertions.assertEquals(HttpStatus.NOT_FOUND.value(),errorResponse.code());
    Assertions.assertEquals(patientNotFoundException.getMessage(),errorResponse.error());
  }

  @Test
  void handleException() {

    var exception = new RuntimeException("Internal Server Error.");

    var errorResponse = exceptionHandler.handleException(exception);

    Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(),errorResponse.code());
    Assertions.assertEquals(exception.getMessage(),errorResponse.error());
  }
}