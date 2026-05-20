package com.debttrackr.service;

import com.debttrackr.service.dto.PersonDTO;

import java.util.List;

public interface PersonService {
    List<PersonDTO> createPersons(List<PersonDTO> requests);
    List<PersonDTO> getAllPerson();
}
