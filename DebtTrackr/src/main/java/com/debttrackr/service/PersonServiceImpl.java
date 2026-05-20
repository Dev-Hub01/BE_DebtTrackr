package com.debttrackr.service;

import com.debttrackr.domain.Person;
import com.debttrackr.mapper.PersonMapper;
import com.debttrackr.repository.PersonRepository;
import com.debttrackr.service.dto.PersonDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService {

    private final PersonRepository personRepo;

    private final PersonMapper personMapper;

    @Override
    public List<PersonDTO> createPersons(List<PersonDTO> requests) {
        System.out.println("onboarding person into database");
        List<Person> persons = requests.stream().map(req -> {
            Person p = personMapper.toEntity(req);
            p.setActive(true);
            return p;
        }).toList();
        List<Person> saved = personRepo.saveAll(persons);
        return personMapper.toDtoList(saved);
    }

    @Override
    public List<PersonDTO> getAllPerson() {
        List<Person> all = personRepo.findAll();
     return  personMapper.toDtoList(all);

    }

//    private PersonDTO mapToResponse(Person p) {
//        return PersonDTO.builder()
//                .name(p.getName())
//                .email(p.getEmail())
//                .contactNumber(p.getContactNumber())
//                .relation(p.getRelation())
//                .build();
//    }
}
