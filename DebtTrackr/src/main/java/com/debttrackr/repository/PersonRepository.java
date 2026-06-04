package com.debttrackr.repository;

import com.debttrackr.domain.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PersonRepository extends JpaRepository<Person,Long> {

    List<Person> findByUserId(Long user);

}
