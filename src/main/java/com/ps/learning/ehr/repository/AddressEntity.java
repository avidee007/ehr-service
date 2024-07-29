package com.ps.learning.ehr.repository;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@Entity
public class AddressEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;
  private String houseNumber;
  private String locality;
  private String street;
  private String district;
  private String state;
  private int zip;
}