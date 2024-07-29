package com.ps.learning.ehr.exception;

public class PatientNotFoundException extends RuntimeException{


  public PatientNotFoundException(String message) {
    super(message);
  }
}
