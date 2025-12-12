package com.safetynet.alerts.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alerts.model.request.MedicalRecordRequest;
import com.safetynet.alerts.model.request.MedicationRequest;
import com.safetynet.alerts.model.request.PersonMinimalRequest;
import com.safetynet.alerts.service.MedicalRecordService;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MedicalRecordController.class)
public class MedicalRecordControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MedicalRecordService medicalRecordService;

    @Nested
    class SaveMedicalRecordTests {
        @Test
        void testSaveMedicalRecord() throws Exception {
            // Mock the service call and response
            MedicalRecordRequest medicalRecordRequest = new MedicalRecordRequest();
            medicalRecordRequest.setFirstName("John");
            medicalRecordRequest.setLastName("Doe");
            medicalRecordRequest.setBirthdate("01/01/1990");
            medicalRecordRequest.setAllergies(Set.of("Peanuts"));
            MedicationRequest medicationRequest = MedicationRequest.builder().
                    name("Medication1").
                    dosage("Dosage1").
                    build();;
            medicalRecordRequest.setMedications(Set.of(medicationRequest));

            ObjectMapper objectMapper = new ObjectMapper();

            when(medicalRecordService.saveMedicalRecord(any(MedicalRecordRequest.class)))
                    .thenReturn(true);

            mockMvc.perform(post("/medicalRecord")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(medicalRecordRequest)))
                    .andExpect(status().isCreated());
        }

        @Test
        void testSaveMedicalRecordWithInvalidData() throws Exception {
            // Mock the service call and response
            MedicalRecordRequest medicalRecordRequest = new MedicalRecordRequest();
            medicalRecordRequest.setFirstName("John");
            medicalRecordRequest.setLastName("Doe");
            medicalRecordRequest.setBirthdate("01/01/1990");
            medicalRecordRequest.setAllergies(Set.of("Peanuts"));
            MedicationRequest medicationRequest = MedicationRequest.builder().
                    name("Medication1").
                    dosage("Dosage1").
                    build();;
            medicalRecordRequest.setMedications(Set.of(medicationRequest));

            ObjectMapper objectMapper = new ObjectMapper();

            when(medicalRecordService.saveMedicalRecord(any(MedicalRecordRequest.class)))
                    .thenReturn(false);

            mockMvc.perform(post("/medicalRecord")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(medicalRecordRequest)))
                    .andExpect(status().isBadRequest());
        }
    }
    @Nested
    class UpdateMedicalRecordTests {
        @Test
        void testUpdateMedicalRecord() throws Exception {
            // Mock the service call and response
            MedicalRecordRequest medicalRecordRequest = new MedicalRecordRequest();
            medicalRecordRequest.setFirstName("John");
            medicalRecordRequest.setLastName("Doe");
            medicalRecordRequest.setBirthdate("01/01/1990");
            medicalRecordRequest.setAllergies(Set.of("Peanuts"));
            MedicationRequest medicationRequest = MedicationRequest.builder().
                    name("Medication1").
                    dosage("Dosage1").
                    build();;
            medicalRecordRequest.setMedications(Set.of(medicationRequest));

            ObjectMapper objectMapper = new ObjectMapper();

            when(medicalRecordService.updateMedicalRecord(any(MedicalRecordRequest.class)))
                    .thenReturn(true);

            mockMvc.perform(put("/medicalRecord")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(medicalRecordRequest)))
                    .andExpect(status().isOk());
        }

        @Test
        void testUpdateMedicalRecordWithInvalidData() throws Exception {
            // Mock the service call and response
            MedicalRecordRequest medicalRecordRequest = new MedicalRecordRequest();
            medicalRecordRequest.setFirstName("John");
            medicalRecordRequest.setLastName("Doe");
            medicalRecordRequest.setBirthdate("01/01/1990");
            medicalRecordRequest.setAllergies(Set.of("Peanuts"));
            MedicationRequest medicationRequest = MedicationRequest.builder().
                    name("Medication1").
                    dosage("Dosage1").
                    build();;
            medicalRecordRequest.setMedications(Set.of(medicationRequest));

            ObjectMapper objectMapper = new ObjectMapper();

            when(medicalRecordService.updateMedicalRecord(any(MedicalRecordRequest.class)))
                    .thenReturn(false);

            mockMvc.perform(put("/medicalRecord")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(medicalRecordRequest)))
                    .andExpect(status().isBadRequest());
        }
    }
    @Nested
    class DeleteMedicalRecordTests {
        @Test
        void testDeleteMedicalRecord() throws Exception {
            // Mock the service call and response
            PersonMinimalRequest personMinimalRequest = PersonMinimalRequest.builder().firstName("John").lastName("Doe").build();

            ObjectMapper objectMapper = new ObjectMapper();

            when(medicalRecordService.deleteMedicalRecord(any(PersonMinimalRequest.class)))
                    .thenReturn(true);

            mockMvc.perform(delete("/medicalRecord")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(personMinimalRequest)))
                    .andExpect(status().isOk());
        }

        @Test
        void testDeleteMedicalRecordWithInvalidData() throws Exception {
            // Mock the service call and response
            PersonMinimalRequest personMinimalRequest = PersonMinimalRequest.builder().firstName("John").lastName("Doe").build();


            ObjectMapper objectMapper = new ObjectMapper();

            when(medicalRecordService.deleteMedicalRecord(any(PersonMinimalRequest.class)))
                    .thenReturn(false);

            mockMvc.perform(delete("/medicalRecord")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(personMinimalRequest)))
                    .andExpect(status().isBadRequest());
        }
    }
}
