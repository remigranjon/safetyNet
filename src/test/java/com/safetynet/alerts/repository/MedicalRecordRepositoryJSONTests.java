package com.safetynet.alerts.repository;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.safetynet.alerts.model.entity.MedicalRecord;
import com.safetynet.alerts.repository.implementation.MedicalRecordRepositoryJSON;
import com.safetynet.alerts.utility.JSONWriter;
import com.safetynet.alerts.utility.enums.JSONNodes;

public class MedicalRecordRepositoryJSONTests {
    MedicalRecordRepositoryJSON medicalRecordRepositoryJSON = new MedicalRecordRepositoryJSON("data/data_tests.json");    


    @BeforeEach
    public void setUp() {
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
    public void testFindByFirstNameAndLastNameNotFound() {
        assert(medicalRecordRepositoryJSON.findByFirstNameAndLastName("Jane", "Smith") == null);
    }

    @Test
    public void testSaveAndGetMedicalRecord() {
        MedicalRecord medicalRecord = MedicalRecord.builder()
                .firstName("John")
                .lastName("Doe")
                .birthdate("01/01/1990")
                .build();
        medicalRecordRepositoryJSON.save(medicalRecord);
        MedicalRecord retrievedMedicalRecord = medicalRecordRepositoryJSON.findByFirstNameAndLastName("John", "Doe");
        assert(retrievedMedicalRecord != null);
        assert(retrievedMedicalRecord.getFirstName().equals("John"));
        assert(retrievedMedicalRecord.getLastName().equals("Doe"));
        assert(retrievedMedicalRecord.getBirthdate().equals("01/01/1990"));
    }

    @Test
    public void testSaveAlreadyExistsMedicalRecord() {
        String firstName = "John";
        String lastName = "Doe";
        MedicalRecord medicalRecord = MedicalRecord.builder()
                .firstName(firstName)
                .lastName(lastName)
                .birthdate("01/01/1990")
                .build();
        medicalRecordRepositoryJSON.save(medicalRecord);
        MedicalRecord anotherMedicalRecord = MedicalRecord.builder()
                .firstName(firstName)
                .lastName(lastName)
                .birthdate("02/02/1992")
                .build();
        assertThrows( IllegalArgumentException.class,() -> medicalRecordRepositoryJSON.save(anotherMedicalRecord));
    }

    @Test
    public void testUpdateMedicalRecord() {
        String firstName = "John";
        String lastName = "Doe";
        MedicalRecord medicalRecord = MedicalRecord.builder()
                .firstName(firstName)
                .lastName(lastName)
                .birthdate("01/01/1990")
                .build();
        medicalRecordRepositoryJSON.save(medicalRecord);
        MedicalRecord updatedMedicalRecord = MedicalRecord.builder()
                .firstName(firstName)
                .lastName(lastName)
                .birthdate("02/02/1992")
                .build();
        medicalRecordRepositoryJSON.update(updatedMedicalRecord);
        MedicalRecord retrievedMedicalRecord = medicalRecordRepositoryJSON.findByFirstNameAndLastName(firstName, lastName);
        assert(retrievedMedicalRecord != null);
        assert(retrievedMedicalRecord.getBirthdate().equals("02/02/1992"));
    }

    @Test
    public void testUpdateMedicalRecordNotFound() {
        MedicalRecord medicalRecord = MedicalRecord.builder()
                .firstName("Jane")
                .lastName("Smith")
                .birthdate("03/03/1993")
                .build();
        assertThrows( IllegalArgumentException.class,() -> medicalRecordRepositoryJSON.update(medicalRecord));
    }

    @Test
    public void testDeleteMedicalRecord() {
        String firstName = "John";
        String lastName = "Doe";
        MedicalRecord medicalRecord = MedicalRecord.builder()
                .firstName(firstName)
                .lastName(lastName)
                .birthdate("01/01/1990")
                .build();
        medicalRecordRepositoryJSON.save(medicalRecord);
        boolean deleted = medicalRecordRepositoryJSON.delete(firstName, lastName);
        assertTrue(deleted);
        MedicalRecord retrievedMedicalRecord = medicalRecordRepositoryJSON.findByFirstNameAndLastName(firstName, lastName);
        assertNull(retrievedMedicalRecord);
    }
}
