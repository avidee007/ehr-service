package com.ps.learning.ehr.model;

import java.io.Serializable;

public record Prescription(String medicineName, int quantity, int dose) implements Serializable {
}
