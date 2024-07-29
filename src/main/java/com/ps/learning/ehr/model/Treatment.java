package com.ps.learning.ehr.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

public record Treatment(long appointmentId, String doctorName, List<Prescription> prescriptions,
                        LocalDateTime datetime) implements Serializable {
}
