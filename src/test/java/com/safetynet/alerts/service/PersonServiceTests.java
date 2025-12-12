package com.safetynet.alerts.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.safetynet.alerts.model.entity.MedicalRecord;
import com.safetynet.alerts.model.entity.Person;
import com.safetynet.alerts.model.request.NewPersonRequest;
import com.safetynet.alerts.model.request.PersonMinimalRequest;
import com.safetynet.alerts.model.request.PersonRequest;
import com.safetynet.alerts.model.response.InhabitantsResponse;
import com.safetynet.alerts.model.response.InhabitantsWithFireStationResponse;
import com.safetynet.alerts.model.response.MedicalRecordResponse;
import com.safetynet.alerts.model.response.PersonDetailResponse;
import com.safetynet.alerts.model.response.PersonWithMedicalRecordResponse;
import com.safetynet.alerts.repository.interfaces.FireStationRepository;
import com.safetynet.alerts.repository.interfaces.MedicalRecordRepository;
import com.safetynet.alerts.repository.interfaces.PersonRepository;

class PersonServiceTests {

        @InjectMocks
        private PersonService personService;
        @Mock
        private PersonRepository personRepository;
        @Mock
        private MedicalRecordRepository medicalRecordRepository;
        @Mock
        private FireStationRepository fireStationRepository;

        @BeforeEach
        void setUp() {
                MockitoAnnotations.openMocks(this);
        }

        @Nested
        class CRUDTests {
                @Test
                void testSavePersonRequestNull() {
                        assertFalse(personService.savePerson(null));
                }

                @Test
                void testSavePerson() {
                        NewPersonRequest personRequest = NewPersonRequest.builder()
                                        .firstName("John")
                                        .lastName("Doe")
                                        .address("1509 Culver St")
                                        .city("Culver")
                                        .zip("97451")
                                        .phone("841-874-6512")
                                        .email("john.doe@example.com")
                                        .birthdate("01/12/2000")
                                        .build();
                        when(medicalRecordRepository.save(any(MedicalRecord.class))).thenReturn(true);
                        when(personRepository.save(any(Person.class))).thenReturn(true);
                        assertTrue(personService.savePerson(personRequest));
                }

                @Test
                void testSavePersonAlreadyExists() {
                        NewPersonRequest personRequest = NewPersonRequest.builder()
                                        .firstName("John")
                                        .lastName("Doe")
                                        .address("1509 Culver St")
                                        .city("Culver")
                                        .zip("97451")
                                        .phone("841-874-6512")
                                        .email("john.doe@example.com")
                                        .birthdate("01/12/2000")
                                        .build();
                        when(personRepository.findByFirstNameAndLastName("John", "Doe")).thenReturn(new Person());
                        assertFalse(personService.savePerson(personRequest));
                }

                @Test
                void testSavePersonMedicalRecordSaveFails() {
                        NewPersonRequest personRequest = NewPersonRequest.builder()
                                        .firstName("John")
                                        .lastName("Doe")
                                        .address("1509 Culver St")
                                        .city("Culver")
                                        .zip("97451")
                                        .phone("841-874-6512")
                                        .email("john.doe@example.com")
                                        .birthdate("01/12/2000")
                                        .build();
                        when(medicalRecordRepository.save(any(MedicalRecord.class))).thenReturn(false);
                        when(personRepository.save(any(Person.class))).thenReturn(false);
                        assertFalse(personService.savePerson(personRequest));
                }

                @Test
                void testUpdatePersonRequestNull() {
                        assertFalse(personService.updatePerson(null));
                }

                @Test
                void testUpdatePersonSuccess() {
                        PersonRequest personRequest = PersonRequest.builder()
                                        .firstName("John")
                                        .lastName("Doe")
                                        .address("1509 Culver St")
                                        .city("Culver")
                                        .zip("97451")
                                        .phone("841-874-6512")
                                        .email("john.doe@example.com")
                                        .build();
                        when(personRepository.update(any(Person.class))).thenReturn(true);
                        assertTrue(personService.updatePerson(personRequest));
                }

                @Test
                void testUpdatePersonFailure() {
                        PersonRequest personRequest = PersonRequest.builder()
                                        .firstName("John")
                                        .lastName("Doe")
                                        .address("1509 Culver St")
                                        .city("Culver")
                                        .zip("97451")
                                        .phone("841-874-6512")
                                        .email("john.doe@example.com")
                                        .build();
                        when(personRepository.update(any(Person.class))).thenReturn(false);
                        assertFalse(personService.updatePerson(personRequest));
                }

                @Test
                void testDeletePersonRequestNull() {
                        assertFalse(personService.deletePerson(null));
                }

                @Test
                void testDeletePersonSuccess() {
                        Person person = new Person();
                        person.setFirstName("John");
                        person.setLastName("Doe");
                        when(personRepository.findByFirstNameAndLastName("John", "Doe")).thenReturn(person);
                        when(personRepository.delete(any(String.class), any(String.class))).thenReturn(true);
                        assertTrue(personService.deletePerson(
                                        PersonMinimalRequest.builder()
                                                        .firstName("John")
                                                        .lastName("Doe")
                                                        .build()));
                }

                @Test
                void testDeletePersonFailure() {
                        Person person = new Person();
                        person.setFirstName("John");
                        person.setLastName("Doe");
                        when(personRepository.findByFirstNameAndLastName("John", "Doe")).thenReturn(person);
                        when(personRepository.delete(any(String.class), any(String.class))).thenReturn(false);
                        assertFalse(personService.deletePerson(
                                        PersonMinimalRequest.builder()
                                                        .firstName("John")
                                                        .lastName("Doe")
                                                        .build()));
                }
        }

        @Nested
        class GetTests {
                @Test
                void testGetEmailsByCity() {
                        when(personRepository.findByCity("Culver")).thenReturn(
                                        Set.of(
                                                        Person.builder().email("john.doe@example.com").build(),
                                                        Person.builder().email("jane.doe@example.com").build()));
                        Set<String> emails = personService.getEmailsByCity("Culver");
                        assertEquals(2, emails.size());
                        assertTrue(emails.contains("john.doe@example.com"));
                        assertTrue(emails.contains("jane.doe@example.com"));
                }

                @Test
                void testGetPersonsInfoByLastName() {
                        when(personRepository.findByLastName("Doe")).thenReturn(
                                        Set.of(
                                                        Person.builder()
                                                                        .firstName("John")
                                                                        .lastName("Doe")
                                                                        .address("1509 Culver St")
                                                                        .city("Culver")
                                                                        .zip("97451")
                                                                        .phone("841-874-6512")
                                                                        .email("john.doe@example.com")
                                                                        .build()));
                        when(medicalRecordRepository.findByFirstNameAndLastName("John", "Doe"))
                                        .thenReturn(
                                                        MedicalRecord.builder()
                                                                        .firstName("John")
                                                                        .lastName("Doe")
                                                                        .birthdate("01/12/2000")
                                                                        .build());
                        Set<PersonDetailResponse> persons = personService.getPersonsInfoByLastName("Doe");
                        assertEquals(1, persons.size());
                        assertTrue(persons.stream().anyMatch(
                                        p -> p.getFirstName().equals("John") && p.getLastName().equals("Doe")));
                }

                @Test
                void testGetInhabitantsByStationsNotFound() {
                        when(fireStationRepository.findAddressesByStation(1))
                                        .thenReturn(new HashSet<>());
                        Set<Integer> stations = Set.of(1);
                        assertNull(personService.getInhabitantsByStations(stations));
                }

                @Test
                void testGetInhabitantsByStations() {
                        when(fireStationRepository.findAddressesByStation(1))
                                        .thenReturn(Set.of("1509 Culver St"));
                        when(personRepository.findByAddress("1509 Culver St"))
                                        .thenReturn(
                                                        Set.of(
                                                                        Person.builder()
                                                                                        .firstName("John")
                                                                                        .lastName("Doe")
                                                                                        .address("1509 Culver St")
                                                                                        .city("Culver")
                                                                                        .zip("97451")
                                                                                        .phone("841-874-6512")
                                                                                        .email("john.doe@example.com")
                                                                                        .build()));
                        Set<Integer> stations = Set.of(1);
                        when(medicalRecordRepository.findByFirstNameAndLastName("John", "Doe"))
                                        .thenReturn(
                                                        MedicalRecord.builder()
                                                                        .firstName("John")
                                                                        .lastName("Doe")
                                                                        .birthdate("01/12/2000")
                                                                        .build());
                        InhabitantsResponse response = InhabitantsResponse.builder()
                                        .address("1509 Culver St")
                                        .inhabitants(
                                                        Set.of(
                                                                        PersonWithMedicalRecordResponse.builder()
                                                                                        .firstName("John")
                                                                                        .lastName("Doe")
                                                                                        .phone("841-874-6512")
                                                                                        .age(25L)
                                                                                        .medicalRecord(
                                                                                                        MedicalRecordResponse
                                                                                                                        .builder()
                                                                                                                        .medications(new HashSet<>())
                                                                                                                        .build())
                                                                                        .build()))
                                        .build();
                        assertEquals(Set.of(response), personService.getInhabitantsByStations(stations));
                }

                @Test
                void testGetInhabitantsByAddress() {
                        String address = "1509 Culver St";
                        when(personRepository.findByAddress(address))
                                        .thenReturn(
                                                        Set.of(
                                                                        Person.builder()
                                                                                        .firstName("John")
                                                                                        .lastName("Doe")
                                                                                        .address("1509 Culver St")
                                                                                        .city("Culver")
                                                                                        .zip("97451")
                                                                                        .phone("841-874-6512")
                                                                                        .email("john.doe@example.com")
                                                                                        .build()));
                        when(medicalRecordRepository.findByFirstNameAndLastName("John", "Doe"))
                                        .thenReturn(
                                                        MedicalRecord.builder()
                                                                        .firstName("John")
                                                                        .lastName("Doe")
                                                                        .birthdate("01/12/2000")
                                                                        .build());
                        when(fireStationRepository.findStationByAddress(address)).thenReturn(1);
                        InhabitantsWithFireStationResponse response = InhabitantsWithFireStationResponse.builder()
                                        .inhabitants(
                                                        Set.of(
                                                                        PersonWithMedicalRecordResponse.builder()
                                                                                        .firstName("John")
                                                                                        .lastName("Doe")
                                                                                        .phone("841-874-6512")
                                                                                        .age(25L)
                                                                                        .medicalRecord(
                                                                                                        MedicalRecordResponse
                                                                                                                        .builder()
                                                                                                                        .medications(new HashSet<>())
                                                                                                                        .build())
                                                                                        .build()))
                                        .station(1)
                                        .build();
                        assertEquals(response, personService.getInhabitantsByAddress(address));
                }

                @Test
                void testGetPhoneNumbersByStation() {
                        int station = 1;
                        when(fireStationRepository.findAddressesByStation(station))
                                        .thenReturn(Set.of("1509 Culver St"));
                        when(personRepository.findByAddress("1509 Culver St"))
                                        .thenReturn(
                                                        Set.of(
                                                                        Person.builder()
                                                                                        .firstName("John")
                                                                                        .lastName("Doe")
                                                                                        .address("1509 Culver St")
                                                                                        .city("Culver")
                                                                                        .zip("97451")
                                                                                        .phone("841-874-6512")
                                                                                        .email("john.doe@example.com")
                                                                                        .build()));
                        Set<String> response = Set.of("841-874-6512");
                        assertEquals(response, personService.getPhoneNumbersByStation(station));
                }

                @Test
                void testGetChildrenWithFamilyByAddressNotFound() {
                        String address = "1509 Culver St";
                        when(personRepository.findByAddress(address))
                                        .thenReturn(new HashSet<>());
                        assertNull(personService.getChildrenWithFamilyByAddress(address));
                }

                @Test
                void testGetChildrenWithFamilyByAddress() {
                        String address = "1509 Culver St";
                        when(personRepository.findByAddress(address))
                                        .thenReturn(
                                                        Set.of(
                                                                        Person.builder()
                                                                                        .firstName("Tom")
                                                                                        .lastName("Doe")
                                                                                        .address("1509 Culver St")
                                                                                        .city("Culver")
                                                                                        .zip("97451")
                                                                                        .phone("841-874-6512")
                                                                                        .email("tom.doe@example.com")
                                                                                        .build(),
                                                                        Person.builder()
                                                                                        .firstName("Jane")
                                                                                        .lastName("Doe")
                                                                                        .address("1509 Culver St")
                                                                                        .city("Culver")
                                                                                        .zip("97451")
                                                                                        .phone("841-874-6513")
                                                                                        .email("jane.doe@example.com")
                                                                                        .build()));
                        when(medicalRecordRepository.findByFirstNameAndLastName("Tom", "Doe"))
                                        .thenReturn(
                                                        MedicalRecord.builder()
                                                                        .firstName("Tom")
                                                                        .lastName("Doe")
                                                                        .birthdate("01/12/2015")
                                                                        .build());
                        when(medicalRecordRepository.findByFirstNameAndLastName("Jane", "Doe"))
                                        .thenReturn(
                                                        MedicalRecord.builder()
                                                                        .firstName("Jane")
                                                                        .lastName("Doe")
                                                                        .birthdate("01/12/2000")
                                                                        .build());

                        assertEquals(1,
                                        personService.getChildrenWithFamilyByAddress(address)
                                                        .getChildren()
                                                        .size());
                        assertEquals(1, personService.getChildrenWithFamilyByAddress(address).getFamilyMembers()
                                        .size());
                }
        }
}