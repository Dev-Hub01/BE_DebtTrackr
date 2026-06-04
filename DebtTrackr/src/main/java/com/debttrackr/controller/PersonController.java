package com.debttrackr.controller;

import com.debttrackr.service.PersonService;
import com.debttrackr.service.dto.PersonDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/persons")
@CrossOrigin(origins = "http://localhost:5173")
public class PersonController {

    private final PersonService personService;

    @PostMapping("/create/bulk")
    public ResponseEntity<List<PersonDTO>> createPersons(@RequestBody List<PersonDTO> requests) {
        System.out.printf("Request to create person");
        return ResponseEntity.ok(personService.createPersons(requests));
    }


    @GetMapping("")
    public ResponseEntity<List<PersonDTO>> fetchAllPerson() {
        log.info("Request to get all person");
        return ResponseEntity.ok(personService.getAllPerson());
    }


    @PostMapping("/create")
    public ResponseEntity<List<PersonDTO>> createPerson(@RequestBody PersonDTO request) {
        log.info("Rest request to create a new person  ");
        return ResponseEntity.ok(personService.createPersons(List.of(request)));
    }

    @PutMapping("/update")
    public ResponseEntity<PersonDTO> updatePerson(@RequestBody PersonDTO request) {
        log.info("Rest request to update a person id {} name {}", request.getId(), request.getName());
        return ResponseEntity.ok(personService.updatePerson(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PersonDTO>fetchPersonById(@PathVariable("id") Long id) {
        log.info("Request to get a person information by id {}", id);
        return ResponseEntity.ok(personService.getPersonById(id));
    }

}
