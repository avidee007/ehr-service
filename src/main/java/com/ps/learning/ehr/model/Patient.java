package com.ps.learning.ehr.model;

import java.io.Serializable;
import java.util.List;

public record Patient(long id, String name, int age, Address address,
                      List<Treatment> treatmentHistory) implements Serializable {

}
