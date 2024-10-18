package com.safetynet.alerts.repository.implementation;

import com.safetynet.alerts.model.entity.Person;
import com.safetynet.alerts.repository.interfaces.PersonRepository;
import com.safetynet.alerts.utility.reader.implementation.JSONReader;
import com.safetynet.alerts.utility.reader.interfaces.DataReader;

import java.util.Set;
import java.util.stream.Collectors;

public class PersonRepositoryInMemory implements PersonRepository {

    private Set<Person> persons;

    public PersonRepositoryInMemory() {
        DataReader dataReader = new JSONReader("/data/data.json");
        persons = dataReader.readPersons();
    }


    @Override
    public Set<Person> findByAddress(String address) {
        return persons.stream()
                .filter(person -> person.getAddress().equals(address))
                .collect(Collectors.toSet());
    }

    @Override
    public Set<Person> findByLastName(String lastName) {
        return persons.stream()
                .filter(person -> person.getLastName().equals(lastName))
                .collect(Collectors.toSet());
    }

    @Override
    public Set<Person> findByCity(String city) {
        return persons.stream()
                .filter(person -> person.getCity().equals(city))
                .collect(Collectors.toSet());
    }
}
