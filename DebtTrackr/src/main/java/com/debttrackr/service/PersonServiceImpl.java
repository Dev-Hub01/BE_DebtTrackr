package com.debttrackr.service;

import com.debttrackr.domain.Person;
import com.debttrackr.domain.User;
import com.debttrackr.mapper.PersonMapper;
import com.debttrackr.repository.PersonRepository;
import com.debttrackr.repository.UserRepository;
import com.debttrackr.service.dto.PersonDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PersonServiceImpl implements PersonService {

    private final PersonRepository personRepo;

    private final PersonMapper personMapper;
    private final UserRepository userRepository;

    @Override
    public List<PersonDTO> createPersons(List<PersonDTO> requests) {
        log.info("Processing  person data into database");
        User admin_user = userRepository.findByEmail(Constants.DEFAULT_ADMIN_EMAIL).get();
        List<Person> persons = requests.stream().map(req -> {
            Person p = personMapper.toEntity(req);
            p.setActive(true);
            p.setCountry("IND");
            p.setAddress(req.getState() +" , "+req.getCity());
            p.setUserId(admin_user.getId());
            return p;
        }).toList();
        List<Person> saved = personRepo.saveAll(persons);
        log.info("New Person {} Onboarded And Mapped Default Admin User To {} ", requests.get(0).getName(), admin_user.getName());
        return personMapper.toDtoList(saved);
    }

    @Override
    @Cacheable(value = "personAllCache")
    public List<PersonDTO> getAllPerson() {
        List<Person> all = personRepo.findAll();
     return  personMapper.toDtoList(all);

    }

    @Override
    @Cacheable(value = "personList")
    public PersonDTO getPersonById(Long id) {
        Optional<Person> person = personRepo.findById(id);
        if(person.isEmpty()){
            throw new RuntimeException("Person Not Found");
        }
        return  personMapper.toDto(person.get());

    }

    @Override
    public PersonDTO updatePerson(PersonDTO request) {
        if(ObjectUtils.isEmpty(request.getId()) || request.getId() == null){
            throw new RuntimeException();
        }
        Person p = personMapper.toEntity(request);
        Person saved = personRepo.save(p);
        return personMapper.toDto(saved);
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
