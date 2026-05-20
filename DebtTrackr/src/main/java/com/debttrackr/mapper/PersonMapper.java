package com.debttrackr.mapper;

import com.debttrackr.domain.Person;
import com.debttrackr.service.dto.PersonDTO;
import jakarta.persistence.EntityManager;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring" , uses = {})
public interface PersonMapper {
    Person toEntity (PersonDTO personDTO);
    List<Person> toEntityList (List<PersonDTO> personDTO);
    PersonDTO toDto (Person personDTO);
    List<PersonDTO> toDtoList (List<Person> personDTO);
}
