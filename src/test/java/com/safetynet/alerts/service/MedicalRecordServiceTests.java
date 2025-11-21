package com.safetynet.alerts.service;


import com.safetynet.alerts.model.entity.MedicalRecord;
import com.safetynet.alerts.model.request.MedicalRecordRequest;
import com.safetynet.alerts.model.request.MedicationRequest;
import com.safetynet.alerts.model.request.PersonMinimalRequest;
import com.safetynet.alerts.model.response.FullMedicalRecordResponse;
import com.safetynet.alerts.repository.implementation.MedicalRecordRepositoryInMemory;
import com.safetynet.alerts.repository.interfaces.MedicalRecordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MedicalRecordServiceTests {
    private MedicalRecordService medicalRecordService;
    private MedicalRecordRepository medicalRecordRepository;

    @BeforeEach
    public void setUp() {
        String datafilePath = "/data/data_tests.json";
        medicalRecordRepository = new MedicalRecordRepositoryInMemory(datafilePath);
        medicalRecordService = new MedicalRecordService(medicalRecordRepository);
    }


    @Nested
    class SaveMedicalRecordTests {
        @Test
        void testSaveMedicalRecordWithNullRequest() {
            assertNull(medicalRecordService.saveMedicalRecord(null));
        }

        @Test
        void testSaveEmptyMedicalRecord() {
            assertNull(medicalRecordService.saveMedicalRecord(MedicalRecordRequest.builder().build()));
        }



        @Test
        void testSaveMedicalRecordWithoutLastName() {
            MedicalRecordRequest request = MedicalRecordRequest.builder().firstName("John").build();
            assertNull(medicalRecordService.saveMedicalRecord(request));
        }

        @Test
        void testSaveMedicalRecordWithoutFirstName() {
            MedicalRecordRequest request = MedicalRecordRequest.builder().lastName("Boyd").build();
            assertNull(medicalRecordService.saveMedicalRecord(request));
        }

        @Test
        void testSaveMedicalRecordWithoutBirthdate() {
            MedicalRecordRequest request = MedicalRecordRequest.builder().firstName("John").lastName("Boyd").build();
            assertNull(medicalRecordService.saveMedicalRecord(request));
        }

        @Test
        void testAlreadySavedMedicalRecord() {
            MedicalRecordRequest request = MedicalRecordRequest.builder().firstName("John")
                    .lastName("Boyd")
                    .birthdate("03/06/1984").build();
            medicalRecordRepository.save(request.toEntity());
            assertNull(medicalRecordService.saveMedicalRecord(request));
        }

        @Test
        void testSaveMedicalRecordWithFirstNameAndLastNameAlreadyExists() {
            MedicalRecordRequest request = MedicalRecordRequest.builder().firstName("John")
                    .lastName("Boyd")
                    .birthdate("03/06/1984").build();
            medicalRecordRepository.save(request.toEntity());
            request.setBirthdate("01/01/2000");
            assertNull(medicalRecordService.saveMedicalRecord(request));
        }


        @Test
        void testSaveMedicalRecordWithValidRequest() {
            MedicalRecordRequest request = MedicalRecordRequest.builder().firstName("John")
                    .lastName("Boyd")
                    .birthdate("03/06/1984").build();
            assertEquals(request.toEntity().toFullResponse(),medicalRecordService.saveMedicalRecord(request));
        }
    }

    @Nested
    class UpdateMedicalRecordTests {
        @Test
        void testUpdateMedicalRecordWithNullRequest() {
            assertNull(medicalRecordService.updateMedicalRecord(null));
        }

        @Test
        void testUpdateMedicalRecordWithEmptyRequest() {
            assertNull(medicalRecordService.updateMedicalRecord(MedicalRecordRequest.builder().build()));
        }

        @Test
        void testUpdateMedicalRecordWithoutLastName() {
            MedicalRecordRequest request = MedicalRecordRequest.builder().firstName("John").build();
            assertNull(medicalRecordService.updateMedicalRecord(request));
        }

        @Test
        void testUpdateMedicalRecordWithoutFirstName() {
            MedicalRecordRequest request = MedicalRecordRequest.builder().lastName("Boyd").build();
            assertNull(medicalRecordService.updateMedicalRecord(request));
        }

        @Test
        void testUpdateMedicalRecordWithNonExistentRecord() {
            MedicalRecordRequest request = MedicalRecordRequest.builder().firstName("John").lastName("Doe").build();
            assertNull(medicalRecordService.updateMedicalRecord(request));
        }

        @Test
        void testUpdateMedicalRecordOnBirthdate() {
            MedicalRecord mr = MedicalRecord.builder().firstName("John")
                    .lastName("Boyd")
                    .birthdate("03/06/1984").build();
            medicalRecordRepository.save(mr);
            MedicalRecordRequest request = MedicalRecordRequest.builder().firstName("John")
                    .lastName("Boyd")
                    .birthdate("04/06/1984").build();
            mr.setBirthdate("04/06/1984");
            assertEquals(new FullMedicalRecordResponse(mr),medicalRecordService.updateMedicalRecord(request));
        }

        @Test
        void testUpdateMedicalRecordOnMedications() {
            MedicalRecord mr = MedicalRecord.builder().firstName("John")
                    .lastName("Boyd")
                    .birthdate("03/06/1984").build();
            medicalRecordRepository.save(mr);
            MedicationRequest medicationRequest = MedicationRequest.builder()
                    .name("medication1")
                    .dosage("dosage1")
                    .build();
            MedicalRecordRequest request = MedicalRecordRequest.builder().firstName("John")
                    .lastName("Boyd")
                    .medications(Set.of(medicationRequest))
                    .build();
            mr.setMedications(Set.of(medicationRequest.toEntity()));
            assertEquals(new FullMedicalRecordResponse(mr),medicalRecordService.updateMedicalRecord(request));
        }

        @Test
        void testUpdateMedicalRecordOnAllergies() {
            MedicalRecord mr = MedicalRecord.builder().firstName("John")
                    .lastName("Boyd")
                    .birthdate("03/06/1984").build();
            medicalRecordRepository.save(mr);
            MedicalRecordRequest request = MedicalRecordRequest.builder().firstName("John")
                    .lastName("Boyd")
                    .allergies(Set.of("allergy1"))
                    .build();
            mr.setAllergies(Set.of("allergy1"));
            assertEquals(new FullMedicalRecordResponse(mr),medicalRecordService.updateMedicalRecord(request));
        }

    }

    @Nested
    class DeleteMedicalRecordTests {
        @Test
        void testDeleteMedicalRecordWithNullRequest() {
            assertFalse(medicalRecordService.deleteMedicalRecord(null));
        }

        @Test
        void testDeleteMedicalRecordWithEmptyRequest() {
            assertFalse(medicalRecordService.deleteMedicalRecord(PersonMinimalRequest.builder().build()));
        }

        @Test
        void testDeleteMedicalRecordWithoutLastName() {
            PersonMinimalRequest request = PersonMinimalRequest.builder().firstName("John").build();
            assertFalse(medicalRecordService.deleteMedicalRecord(request));
        }

        @Test
        void testDeleteMedicalRecordWithoutFirstName() {
            PersonMinimalRequest request = PersonMinimalRequest.builder().lastName("Boyd").build();
            assertFalse(medicalRecordService.deleteMedicalRecord(request));
        }

        @Test
        void testDeleteNonExistentMedicalRecord() {
            PersonMinimalRequest request = PersonMinimalRequest.builder().firstName("John").lastName("Doe").build();
            assertFalse(medicalRecordService.deleteMedicalRecord(request));
        }

        @Test
        void testDeleteExistingMedicalRecord() {
            MedicalRecord mr = MedicalRecord.builder().firstName("John")
                    .lastName("Boyd")
                    .birthdate("03/06/1984").build();
            medicalRecordRepository.save(mr);
            PersonMinimalRequest request = PersonMinimalRequest.builder().firstName("John")
                    .lastName("Boyd").build();
            assertTrue(medicalRecordService.deleteMedicalRecord(request));
        }
    }

}
