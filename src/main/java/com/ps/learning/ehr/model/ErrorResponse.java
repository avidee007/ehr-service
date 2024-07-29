package com.ps.learning.ehr.model;

import java.time.Instant;

public record ErrorResponse(int code, String error, Instant timestamp) {
}
