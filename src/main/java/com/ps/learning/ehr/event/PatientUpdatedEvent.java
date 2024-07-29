package com.ps.learning.ehr.event;

import java.io.Serializable;
import java.time.LocalDateTime;

public record PatientUpdatedEvent(long id, LocalDateTime updateTimestamp) implements Serializable {
}
