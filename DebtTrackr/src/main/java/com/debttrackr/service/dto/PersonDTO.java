package com.debttrackr.service.dto;

import com.debttrackr.domain.enumeration.Relation;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
public class PersonDTO implements Serializable {
    private Long id;
    private String name;
    private String email;
    private String contactNumber;

    private String address;
    private String city;
    private String state;
    private String country;

    private Relation relation;

    private String notes;
}
