package com.ps.learning.ehr.exception;

import com.ps.learning.ehr.model.ErrorResponse;
import java.time.Instant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

  @ExceptionHandler(PatientNotFoundException.class)
  @ResponseStatus(code = HttpStatus.NOT_FOUND)
  public ErrorResponse handlePatientNotFoundException(PatientNotFoundException ex) {
    log.info("PatientNotFoundException exception happened.");
    log.error("Exception stack trace: ", ex);
    return new ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage(),
        Instant.now());
  }

  @ExceptionHandler(Exception.class)
  @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
  public ErrorResponse handleException(Exception ex) {
    log.info("Exception happened.");
    log.error("Exception stack trace: ", ex);
    return new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage(),
        Instant.now());
  }


}
