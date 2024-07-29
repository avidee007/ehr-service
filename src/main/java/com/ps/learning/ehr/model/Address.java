package com.ps.learning.ehr.model;

import java.io.Serializable;

public record Address(String houseNumber, String locality, String street,
                      String district, String state, int zip) implements Serializable {
}
