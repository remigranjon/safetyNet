package com.safetynet.alerts.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.safetynet.alerts.model.entity.MedicalRecord;
import com.safetynet.alerts.model.request.MedicalRecordRequest;
import com.safetynet.alerts.model.request.PersonMinimalRequest;
import com.safetynet.alerts.repository.interfaces.MedicalRecordRepository;

class MedicalRecordServiceTests {
    @InjectMocks
    private MedicalRecordService medicalRecordService;

    @Mock
    private MedicalRecordRepository medicalRecordRepository;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveMedicalRecordRequestNull() {
        assertFalse(medicalRecordService.saveMedicalRecord(null));
    }

    @Test
    void testSaveMedicalRecordRequestNotValid() {
        assertFalse(medicalRecordService.saveMedicalRecord(new MedicalRecordRequest()));
    }

    @Test
    void testSaveMedicalRecordRequestAlreadyExists() {
        MedicalRecordRequest medicalRecordRequest = new MedicalRecordRequest();
        medicalRecordRequest.setFirstName("John");
        medicalRecordRequest.setLastName("Doe");
        medicalRecordRequest.setBirthdate("01/01/1990");

        when(medicalRecordRepository.findByFirstNameAndLastName("John", "Doe"))
                .thenReturn(new MedicalRecord());

        assertFalse(medicalRecordService.saveMedicalRecord(medicalRecordRequest));
    }

    @Test
    void testSaveMedicalRecordSuccess() {
        MedicalRecordRequest medicalRecordRequest = new MedicalRecordRequest();
        medicalRecordRequest.setFirstName("Jane");
        medicalRecordRequest.setLastName("Doe");
        medicalRecordRequest.setBirthdate("02/02/1992");

        when(medicalRecordRepository.findByFirstNameAndLastName("Jane", "Doe"))
                .thenReturn(null);
        when(medicalRecordRepository.save(medicalRecordRequest.toEntity()))
                .thenReturn(true);

        assertTrue(medicalRecordService.saveMedicalRecord(medicalRecordRequest));
    }

    @Test
    void testUpdateMedicalRecordRequestNull() {
        assertFalse(medicalRecordService.updateMedicalRecord(null));
    }

    @Test
    void testUpdateMedicalRecordRequestNotValid() {
        MedicalRecordRequest medicalRecordRequest = new MedicalRecordRequest();
        medicalRecordRequest.setFirstName(null);
        medicalRecordRequest.setLastName("Doe");

        assertFalse(medicalRecordService.updateMedicalRecord(medicalRecordRequest));
    }

    @Test
    void testUpdateMedicalRecordSuccess() {
        MedicalRecordRequest medicalRecordRequest = new MedicalRecordRequest();
        medicalRecordRequest.setFirstName("John");
        medicalRecordRequest.setLastName("Doe");
        medicalRecordRequest.setBirthdate("01/01/1990");    

        when(medicalRecordRepository.update(medicalRecordRequest.toEntity()))
                .thenReturn(true);

        assertTrue(medicalRecordService.updateMedicalRecord(medicalRecordRequest));
    }

    @Test
    void testUpdateMedicalRecordFailure() {
        MedicalRecordRequest medicalRecordRequest = new MedicalRecordRequest();
        medicalRecordRequest.setFirstName("John");
        medicalRecordRequest.setLastName("Doe");
        medicalRecordRequest.setBirthdate("01/01/1990");    

        when(medicalRecordRepository.update(medicalRecordRequest.toEntity()))
                .thenThrow(new IllegalArgumentException("Medical record not found"));

        assertFalse(medicalRecordService.updateMedicalRecord(medicalRecordRequest));
    }

    @Test
    void testDeleteMedicalRecordRequestNull() {
        assertFalse(medicalRecordService.deleteMedicalRecord(null));
    }

    @Test
    void testDeleteMedicalRecordRequestNotValid() {
        assertFalse(medicalRecordService.deleteMedicalRecord(new PersonMinimalRequest()));
    }

    @Test
    void testDeleteMedicalRecordSuccess() {
        String firstName = "John";
        String lastName = "Doe";
        
        PersonMinimalRequest personMinimalRequest = new PersonMinimalRequest();
        personMinimalRequest.setFirstName(firstName);
        personMinimalRequest.setLastName(lastName);
        when(medicalRecordRepository.delete(firstName, lastName))
                .thenReturn(true);

        assertTrue(medicalRecordService.deleteMedicalRecord(personMinimalRequest));
    }
}