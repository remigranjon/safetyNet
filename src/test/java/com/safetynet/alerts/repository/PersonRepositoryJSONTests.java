package com.safetynet.alerts.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.safetynet.alerts.model.entity.Person;
import com.safetynet.alerts.repository.implementation.PersonRepositoryJSON;
import com.safetynet.alerts.utility.JSONWriter;
import com.safetynet.alerts.utility.enums.JSONNodes;

public class PersonRepositoryJSONTests {
    PersonRepositoryJSON personRepositoryJSON = new PersonRepositoryJSON("data/data_tests.json");

    @BeforeEach
    public void setup() {
        initializeJSONData();
    }

    private void initializeJSONData() {
        JSONWriter jsonWriter = new JSONWriter("data/data_tests.json");
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode rootNode = objectMapper.createObjectNode();
        rootNode.putArray(JSONNodes.FIRESTATIONS.getValue());
        rootNode.putArray(JSONNodes.PERSONS.getValue());
        rootNode.putArray(JSONNodes.MEDICALRECORDS.getValue());
        jsonWriter.writeJson(rootNode);
    }

    @Test
    public void testSaveAndGetPerson() {
        Person person = Person.builder()
                .firstName("John")
                .lastName("Doe")
                .address("123 Main St")
                .city("Anytown")
                .zip("12345")
                .phone("555-1234")
                .email("john.doe@example.com")
                .build();
        personRepositoryJSON.save(person);
        Person retrievedPerson = personRepositoryJSON.findByFirstNameAndLastName("John", "Doe");
        assertNotNull(retrievedPerson);
        assertEquals("John", retrievedPerson.getFirstName());
        assertEquals("Doe", retrievedPerson.getLastName());
        assertEquals("123 Main St", retrievedPerson.getAddress());
        assertEquals("Anytown", retrievedPerson.getCity());
        assertEquals("12345", retrievedPerson.getZip());
        assertEquals("555-1234", retrievedPerson.getPhone());
        assertEquals("john.doe@example.com", retrievedPerson.getEmail());
    }

    @Test
    public void testFindByFirstNameAndLastNameNotFound() {
        Person retrievedPerson = personRepositoryJSON.findByFirstNameAndLastName("Jane", "Smith");
        assertEquals(null, retrievedPerson);
    }

    @Test
    public void testFindByAddress() {
        Person person1 = Person.builder()
                .firstName("Alice")
                .lastName("Johnson")
                .address("456 Oak St")
                .city("Othertown")
                .zip("67890")
                .phone("555-5678")
                .email("alice.johnson@example.com").build();
        Person person2 = Person.builder()
                .firstName("Bob")
                .lastName("Smith")
                .address("457 Oak St")
                .city("Othertown")
                .zip("67890")
                .phone("555-8765")
                .email("bob.smith@example.com").build();
        personRepositoryJSON.save(person1);
        personRepositoryJSON.save(person2);
        var personsAtAddress = personRepositoryJSON.findByAddress("456 Oak St");
        assertEquals(1, personsAtAddress.size());
        assertEquals("Alice", personsAtAddress.iterator().next().getFirstName());
    }

    @Test
    public void testFindByLastName() {
        Person person1 = Person.builder()
                .firstName("Charlie")
                .lastName("Brown")
                .address("789 Pine St")
                .city("Sometown")
                .zip("11223")
                .phone("555-0000")
                .email("charlie.brown@example.com").build();
        Person person2 = Person.builder()
                .firstName("David")
                .lastName("Brown")
                .address("790 Pine St")
                .city("Sometown")
                .zip("11223")
                .phone("555-1111")
                .email("david.brown@example.com").build();
        Person person3 = Person.builder()
                .firstName("Eve")
                .lastName("Davis")
                .address("791 Pine St")
                .city("Sometown")
                .zip("11223")
                .phone("555-2222")
                .email("eve.davis@example.com").build();
        personRepositoryJSON.save(person1);
        personRepositoryJSON.save(person2);
        personRepositoryJSON.save(person3);
        var personsWithLastName = personRepositoryJSON.findByLastName("Brown");
        assertEquals(2, personsWithLastName.size());
    }

    @Test
    public void testFindByCity() {
        Person person1 = Person.builder()
                .firstName("Frank")
                .lastName("Green")
                .address("101 Maple St")
                .city("Newcity")
                .zip("33445")
                .phone("555-3333")
                .email("frank.green@example.com").build();
        Person person2 = Person.builder()
                .firstName("Grace")
                .lastName("Harris")
                .address("102 Maple St")
                .city("Newcity")
                .zip("33445")
                .phone("555-4444")
                .email("grace.harris@example.com").build();
        Person person3 = Person.builder()
                .firstName("Hank")
                .lastName("Iverson")
                .address("103 Oak St")
                .city("Oldcity")
                .zip("55667")
                .phone("555-5555")
                .email("hank.iverson@example.com").build();
        personRepositoryJSON.save(person1);
        personRepositoryJSON.save(person2);
        personRepositoryJSON.save(person3);
        var personsInCity = personRepositoryJSON.findByCity("Newcity");
        assertEquals(2, personsInCity.size());
    }

    @Test
    public void testDeletePerson() {
        Person person = Person.builder()
                .firstName("Ivy")
                .lastName("Jones")
                .address("202 Birch St")
                .city("Smalltown")
                .zip("77889")
                .phone("555-6666")
                .email("ivy.jones@example.com").build();
        personRepositoryJSON.save(person);
        personRepositoryJSON.delete(person.getFirstName(), person.getLastName());
        var persons = personRepositoryJSON.findByLastName("Jones");
        assertFalse(persons.contains(person));
    }

    @Test
    public void testUpdatePerson() {
        Person person = Person.builder()
                .firstName("Jack")
                .lastName("King")
                .address("303 Cedar St")
                .city("Bigtown")
                .zip("99000")
                .phone("555-7777")
                .email("jack.king@example.com").build();
        personRepositoryJSON.save(person);
        person.setCity("Newtown");
        personRepositoryJSON.update(person);
        var updatedPerson = personRepositoryJSON.findByLastName("King").iterator().next();
        assertEquals("Newtown", updatedPerson.getCity());
    }

    @Test
    public void testUpdatePersonNotFound() {
        Person person = Person.builder()
                .firstName("Lily")
                .lastName("Moore")
                .address("404 Spruce St")
                .city("Middletown")
                .zip("11122")
                .phone("555-8888")
                .email("lily.moore@example.com").build();
        assertFalse(
                personRepositoryJSON.update(person));
    }
}