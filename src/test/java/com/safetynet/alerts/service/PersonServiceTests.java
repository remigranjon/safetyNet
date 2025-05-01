package com.safetynet.alerts.service;

import com.safetynet.alerts.model.entity.FireStation;
import com.safetynet.alerts.model.entity.MedicalRecord;
import com.safetynet.alerts.model.entity.Medication;
import com.safetynet.alerts.model.entity.Person;
import com.safetynet.alerts.model.response.*;
import com.safetynet.alerts.repository.implementation.FireStationRepositoryInMemory;
import com.safetynet.alerts.repository.implementation.MedicalRecordRepositoryInMemory;
import com.safetynet.alerts.repository.implementation.PersonRepositoryInMemory;
import com.safetynet.alerts.repository.interfaces.FireStationRepository;
import com.safetynet.alerts.repository.interfaces.MedicalRecordRepository;
import com.safetynet.alerts.repository.interfaces.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class PersonServiceTests {
    private PersonService personService;
    private PersonRepository personRepository;
    private FireStationRepository fireStationRepository;
    private MedicalRecordRepository medicalRecordRepository;

    @BeforeEach
    public void setUp() {
        String datafilePath = "/data/data_tests.json";
        personRepository = new PersonRepositoryInMemory(datafilePath);
        fireStationRepository = new FireStationRepositoryInMemory(datafilePath);
        medicalRecordRepository = new MedicalRecordRepositoryInMemory(datafilePath);
        personService = new PersonService(personRepository, fireStationRepository, medicalRecordRepository);
    }

    @Nested
    class GetChildrenWithFamilyByAddressTests {
        @Test
        void testGetChildrenWithFamilyByAddress() {
            String address = "1509 Culver St";
            Person adult1 = Person.builder()
                    .firstName("John")
                    .lastName("Boyd")
                    .address(address)
                    .phone("123-456-7890")
                    .build();
            Person adult2 = Person.builder()
                    .firstName("Jane")
                    .lastName("Boyd")
                    .address(address)
                    .phone("098-765-4321")
                    .build();
            Person child = Person.builder()
                    .firstName("Jack")
                    .lastName("Boyd")
                    .address(address)
                    .phone("555-555-5555")
                    .build();
            personRepository.save(adult1);
            personRepository.save(adult2);
            personRepository.save(child);
            MedicalRecord mrChild = MedicalRecord.builder()
                    .firstName("Jack")
                    .lastName("Boyd")
                    .birthdate("01/01/2010")
                    .build();
            MedicalRecord mrAdult1 = MedicalRecord.builder()
                    .firstName("John")
                    .lastName("Boyd")
                    .birthdate("01/01/1980")
                    .build();
            MedicalRecord mrAdult2 = MedicalRecord.builder()
                    .firstName("Jane")
                    .lastName("Boyd")
                    .birthdate("01/01/1985")
                    .build();
            medicalRecordRepository.save(mrChild);
            medicalRecordRepository.save(mrAdult1);
            medicalRecordRepository.save(mrAdult2);
            PersonNamesAndAgeResponse adult1Response = PersonNamesAndAgeResponse.builder()
                    .firstName(adult1.getFirstName())
                    .lastName(adult1.getLastName())
                    .age(mrAdult1.getAge())
                    .build();
            PersonNamesAndAgeResponse adult2Response = PersonNamesAndAgeResponse.builder()
                    .firstName(adult2.getFirstName())
                    .lastName(adult2.getLastName())
                    .age(mrAdult2.getAge())
                    .build();
            PersonNamesAndAgeResponse childResponse = PersonNamesAndAgeResponse.builder()
                    .firstName(child.getFirstName())
                    .lastName(child.getLastName())
                    .age(mrChild.getAge())
                    .build();

            ChildrenWithFamilyResponse response = ChildrenWithFamilyResponse.builder().children(Set.of(childResponse))
                    .familyMembers(Set.of(adult1Response, adult2Response))
                    .build();
            assertEquals(response, personService.getChildrenWithFamilyByAddress(address));

        }

        @Test
        void testGetChildrenWithFamilyByAddressNotFound() {
            String address = "Unknown Address";

            assertNull(personService.getChildrenWithFamilyByAddress(address));
        }
    }

    @Nested
    class GetPhoneNumbersByStationTests {
        @Test
        void testGetPhoneNumbersByStation() {
            int stationNumber = 1;
            Person person1 = Person.builder()
                    .firstName("John")
                    .lastName("Boyd")
                    .address("1509 Culver St")
                    .phone("123-456-7890")
                    .build();
            Person person2 = Person.builder()
                    .firstName("Jane")
                    .lastName("Boyd")
                    .address("1509 Culver St")
                    .phone("098-765-4321")
                    .build();
            personRepository.save(person1);
            personRepository.save(person2);
            FireStation fireStation = FireStation.builder()
                    .address("1509 Culver St")
                    .station(stationNumber)
                    .build();
            fireStationRepository.save(fireStation);
            Set<String> expectedPhones = Set.of(person1.getPhone(), person2.getPhone());
            assertEquals(expectedPhones, personService.getPhoneNumbersByStation(stationNumber));
        }
    }

    @Nested
    class GetInhabitantsByAddressTests {
        @Test
        void testGetInhabitantsByAddress() {
            String address = "1509 Culver St";
            Person person1 = Person.builder()
                    .firstName("John")
                    .lastName("Boyd")
                    .address(address)
                    .phone("123-456-7890")
                    .build();
            Person person2 = Person.builder()
                    .firstName("Jane")
                    .lastName("Boyd")
                    .address(address)
                    .phone("098-765-4321")
                    .build();
            personRepository.save(person1);
            personRepository.save(person2);
            Medication medication1 = Medication.builder()
                    .name("Medication1")
                    .dosage("Dosage1")
                    .build();
            Medication medication2 = Medication.builder()
                    .name("Medication2")
                    .dosage("Dosage2")
                    .build();
            MedicalRecord mr1 = MedicalRecord.builder()
                    .firstName("John")
                    .lastName("Boyd")
                    .birthdate("01/01/1980")
                    .allergies(Set.of("Peanuts", "Shellfish"))
                    .medications(Set.of(medication1, medication2))
                    .build();
            MedicalRecord mr2 = MedicalRecord.builder()
                    .firstName("Jane")
                    .lastName("Boyd")
                    .birthdate("01/01/1985")
                    .build();
            medicalRecordRepository.save(mr1);
            medicalRecordRepository.save(mr2);
            FireStation fireStation = FireStation.builder()
                    .address(address)
                    .station(1)
                    .build();
            fireStationRepository.save(fireStation);
            InhabitantsWithFireStationResponse response =
                    InhabitantsWithFireStationResponse.builder().inhabitants(Set.of(
                                    PersonWithMedicalRecordResponse.builder()
                                            .firstName(person1.getFirstName())
                                            .lastName(person1.getLastName())
                                            .phone(person1.getPhone())
                                            .age(mr1.getAge())
                                            .medicalRecord(mr1.toResponse()).build(),
                                    PersonWithMedicalRecordResponse.builder()
                                            .firstName(person2.getFirstName())
                                            .lastName(person2.getLastName())
                                            .phone(person2.getPhone())
                                            .age(mr2.getAge())
                                            .medicalRecord(mr2.toResponse()).build()
                            ))
                            .station(1)
                            .build();
            assertEquals(response, personService.getInhabitantsByAddress(address));
        }
    }

    @Nested
    class GetInhabitantsByStationsTests {
        @Test
        void testGetInhabitantsByStations() {
            int stationNumber = 1;
            int stationNumber2 = 2;
            Person person1 = Person.builder()
                    .firstName("John")
                    .lastName("Boyd")
                    .address("1509 Culver St")
                    .phone("123-456-7890")
                    .build();
            Person person2 = Person.builder()
                    .firstName("Jane")
                    .lastName("Boyd")
                    .address("test")
                    .phone("098-765-4321")
                    .build();
            Person person3 = Person.builder()
                    .firstName("Jack")
                    .lastName("Boyd")
                    .address("test")
                    .phone("555-555-5555")
                    .build();
            personRepository.save(person3);
            personRepository.save(person1);
            personRepository.save(person2);
            Medication medication1 = Medication.builder()
                    .name("Medication1")
                    .dosage("Dosage1")
                    .build();
            Medication medication2 = Medication.builder()
                    .name("Medication2")
                    .dosage("Dosage2")
                    .build();
            MedicalRecord mr1 = MedicalRecord.builder()
                    .firstName("John")
                    .lastName("Boyd")
                    .birthdate("01/01/1980")
                    .allergies(Set.of("Peanuts", "Shellfish"))
                    .medications(Set.of(medication1, medication2))
                    .build();
            MedicalRecord mr2 = MedicalRecord.builder()
                    .firstName("Jane")
                    .lastName("Boyd")
                    .birthdate("01/01/1985")
                    .build();
            MedicalRecord mr3 = MedicalRecord.builder()
                    .firstName("Jack")
                    .lastName("Boyd")
                    .birthdate("01/01/1985")
                    .build();
            medicalRecordRepository.save(mr1);
            medicalRecordRepository.save(mr2);
            medicalRecordRepository.save(mr3);
            FireStation fireStation = FireStation.builder()
                    .address("1509 Culver St")
                    .station(stationNumber)
                    .build();
            FireStation fireStation2 = FireStation.builder()
                    .address("test")
                    .station(stationNumber2)
                    .build();
            fireStationRepository.save(fireStation);
            fireStationRepository.save(fireStation2);
            Set<InhabitantsResponse> response = Set.of(
                    InhabitantsResponse.builder()
                            .address(person1.getAddress())
                            .inhabitants(Set.of(
                                    PersonWithMedicalRecordResponse.builder()
                                            .firstName(person1.getFirstName())
                                            .lastName(person1.getLastName())
                                            .phone(person1.getPhone())
                                            .age(mr1.getAge())
                                            .medicalRecord(mr1.toResponse()).build()))
                            .build(),
                    InhabitantsResponse.builder()
                            .address(person2.getAddress())
                            .inhabitants(Set.of(
                                    PersonWithMedicalRecordResponse.builder()
                                            .firstName(person2.getFirstName())
                                            .lastName(person2.getLastName())
                                            .phone(person2.getPhone())
                                            .age(mr2.getAge())
                                            .medicalRecord(mr2.toResponse()).build(),
                                    PersonWithMedicalRecordResponse.builder()
                                            .firstName(person3.getFirstName())
                                            .lastName(person3.getLastName())
                                            .phone(person3.getPhone())
                                            .age(mr3.getAge())
                                            .medicalRecord(mr3.toResponse()).build()))
                            .build());
            assertEquals(response, personService.getInhabitantsByStations(Set.of(stationNumber, stationNumber2)));
        }

        @Test
        void testGetInhabitantsByStationsWithWrongStation() {
            assertNull(personService.getInhabitantsByStations(Set.of(1)));
        }
    }

    @Nested
    class GetPersonsInfoByLastNameTests {
        @Test
        void testGetPersonsInfoByLastName() {
            String lastName = "Boyd";
            Person person1 = Person.builder()
                    .firstName("John")
                    .lastName(lastName)
                    .address("1509 Culver St")
                    .email("test")
                    .build();
            Person person2 = Person.builder()
                    .firstName("Jane")
                    .lastName(lastName)
                    .address("1509 Culver St")
                    .email("test2")
                    .build();
            personRepository.save(person1);
            personRepository.save(person2);
            Medication medication1 = Medication.builder()
                    .name("Medication1")
                    .dosage("Dosage1")
                    .build();
            Medication medication2 = Medication.builder()
                    .name("Medication2")
                    .dosage("Dosage2")
                    .build();
            MedicalRecord mr1 = MedicalRecord.builder()
                    .firstName("John")
                    .lastName(lastName)
                    .birthdate("01/01/1980")
                    .allergies(Set.of("Peanuts", "Shellfish"))
                    .medications(Set.of(medication1, medication2))
                    .build();
            MedicalRecord mr2 = MedicalRecord.builder()
                    .firstName("Jane")
                    .lastName(lastName)
                    .birthdate("01/01/1985")
                    .build();
            medicalRecordRepository.save(mr1);
            medicalRecordRepository.save(mr2);
            PersonDetailResponse person1Response = PersonDetailResponse.builder()
                    .firstName(person1.getFirstName())
                    .lastName(person1.getLastName())
                    .address(person1.getAddress())
                    .email(person1.getEmail())
                    .age(mr1.getAge())
                    .medicalRecord(mr1.toResponse()).build();
            PersonDetailResponse person2Response = PersonDetailResponse.builder()
                    .firstName(person2.getFirstName())
                    .lastName(person2.getLastName())
                    .address(person2.getAddress())
                    .email(person2.getEmail())
                    .age(mr2.getAge())
                    .medicalRecord(mr2.toResponse()).build();
            Set<PersonDetailResponse> response = personService.getPersonsInfoByLastName(lastName);

            assertTrue(response.contains(person1Response));
            assertTrue(response.contains(person2Response));
        }
    }

    @Nested
    class GetEmailsByCityTests {
        @Test
        void testGetEmailsByCity() {
            String city = "Culver";
            Person person1 = Person.builder()
                    .firstName("John")
                    .lastName("Boyd")
                    .address("1509 Culver St")
                    .city(city)
                    .email("test")
                    .build();
            Person person2 = Person.builder()
                    .firstName("Jane")
                    .lastName("Boyd")
                    .address("1509 Culver St")
                    .city(city)
                    .email("test2")
                    .build();
            Person person3 = Person.builder()
                    .firstName("Jack")
                    .lastName("Boyd")
                    .address("1509 Culver St")
                    .city("OtherCity")
                    .email("test3")
                    .build();
            personRepository.save(person1);
            personRepository.save(person2);
            personRepository.save(person3);
            Set<String> expectedEmails = Set.of(person1.getEmail(), person2.getEmail());
            assertEquals(expectedEmails, personService.getEmailsByCity(city));
        }
    }

}
