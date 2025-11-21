package com.safetynet.alerts.repository.interfaces;

import com.safetynet.alerts.model.entity.Person;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface PersonRepository {

    Set<Person> findByAddress(String address);

    Set<Person> findByLastName(String lastName);

    Set<Person> findByCity(String city);

    Person save(Person person);

    boolean delete(Person person);

    Person findByFirstNameAndLastName(String firstName, String lastName);
}
