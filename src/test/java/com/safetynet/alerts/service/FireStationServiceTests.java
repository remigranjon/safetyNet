package com.safetynet.alerts.service;


import com.safetynet.alerts.model.entity.FireStation;
import com.safetynet.alerts.model.entity.MedicalRecord;
import com.safetynet.alerts.model.entity.Medication;
import com.safetynet.alerts.model.entity.Person;
import com.safetynet.alerts.model.request.FireStationRequest;
import com.safetynet.alerts.model.response.FireStationResponse;
import com.safetynet.alerts.model.response.PersonsWithCountResponse;
import com.safetynet.alerts.repository.implementation.FireStationRepositoryInMemory;
import com.safetynet.alerts.repository.implementation.MedicalRecordRepositoryInMemory;
import com.safetynet.alerts.repository.implementation.PersonRepositoryInMemory;
import com.safetynet.alerts.repository.interfaces.FireStationRepository;
import com.safetynet.alerts.repository.interfaces.MedicalRecordRepository;
import com.safetynet.alerts.repository.interfaces.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class FireStationServiceTests {
    private FireStationService fireStationService;
    private FireStationRepository fireStationRepository;
    private PersonRepository personRepository;
    private MedicalRecordRepository medicalRecordRepository;

    @BeforeEach
    public void setUp() {
        String datafilePath = "/data/data_tests.json";
        fireStationRepository = new FireStationRepositoryInMemory(datafilePath);
        personRepository = new PersonRepositoryInMemory(datafilePath);
        medicalRecordRepository = new MedicalRecordRepositoryInMemory(datafilePath);
        fireStationService = new FireStationService(fireStationRepository, personRepository, medicalRecordRepository);
    }

    private String getAddress1() {
        return "1509 Culver St";
    }

    private String getAddress2() {
        return "987 Hill St";
    }

    private Person getPerson01(String address) {
        return Person.builder()
                .firstName("John")
                .lastName("Boyd")
                .address(address)
                .city("Culver")
                .zip("97451")
                .phone("841-874-6512")
                .email("J.B@gmail.com")
                .build();
    }

    private Person getPerson02(String address) {
        return Person.builder()
                .firstName("Jacob")
                .lastName("auyeritf")
                .address(address)
                .city("ztrh")
                .zip("zryuk")
                .phone("68784")
                .email("zoteriughzoui")
                .build();
    }

    private FireStation getFireStation01(String address, int station) {
        return FireStation.builder()
                .address(address)
                .station(station)
                .build();
    }

    private MedicalRecord getMedicalRecord01(Person person, boolean isAdult) {
        return MedicalRecord.builder()
                .firstName(person.getFirstName())
                .lastName(person.getLastName())
                .birthdate(isAdult ? "01/01/1900" : "01/01/2020")
                .medications(Set.of(Medication.builder().name("aznol").dosage("125mg").build()))
                .allergies(Set.of("nillacilan"))
                .build();
    }

    @Test
    void testGetPersonsWithCountByStationEmpty() {
        PersonsWithCountResponse response = PersonsWithCountResponse.builder().persons(Set.of()).childrenCount(0).adultCount(0).build();
        assertEquals(response, fireStationService.getPersonsWithCountByStation(1));
    }

    @Test
    void testGetPersonsWithCountByStation() {
        Person p1 = personRepository.save(getPerson01(getAddress1()));
        Person p2 = personRepository.save(getPerson02(getAddress1()));
        fireStationRepository.save(getFireStation01(getAddress1(), 1));
        medicalRecordRepository.save(getMedicalRecord01(p1, true));
        medicalRecordRepository.save(getMedicalRecord01(p2, false));
        PersonsWithCountResponse response = PersonsWithCountResponse.builder()
                .persons(Set.of(p1.toPersonMinimalResponse(), p2.toPersonMinimalResponse())).childrenCount(1).adultCount(1).build();
        assertEquals(response,fireStationService.getPersonsWithCountByStation(1));
    }

    @Test
    void testSaveFireStationAddressNull() {
        FireStationRequest request = FireStationRequest.builder().address(null).station(1).build();
        assertNull(fireStationService.saveFireStation(request));
    }

    @Test
    void testSaveFireStationStationNull() {
        FireStationRequest request = FireStationRequest.builder().address(getAddress1()).station(null).build();
        assertNull(fireStationService.saveFireStation(request));
    }

    @Test
    void testSaveFireStationStationZero() {
        FireStationRequest request = FireStationRequest.builder().address(getAddress1()).station(0).build();
        assertNull(fireStationService.saveFireStation(request));
    }

    @Test
    void testSaveFireStation() {
        FireStationRequest request = FireStationRequest.builder().address(getAddress1()).station(1).build();
        FireStation fireStation = getFireStation01(getAddress1(), 1);
        FireStationResponse response = fireStation.toFireStationResponse();
        assertEquals(response, fireStationService.saveFireStation(request));
    }

    @Test
    void testSaveFireStationAlreadyExists() {
        FireStationRequest request = FireStationRequest.builder().address(getAddress1()).station(1).build();
        fireStationRepository.save(getFireStation01(getAddress1(), 1));
        assertNull(fireStationService.saveFireStation(request));
    }

    @Test
    void testUpdateFireStationAddressNull() {
        FireStationRequest request = FireStationRequest.builder().address(null).station(1).build();
        assertNull(fireStationService.updateFireStation(request));
    }

    @Test
    void testUpdateFireStationStationNull() {
        FireStationRequest request = FireStationRequest.builder().address(getAddress1()).station(null).build();
        assertNull(fireStationService.updateFireStation(request));
    }

    @Test
    void testUpdateFireStationStationZero() {
        FireStationRequest request = FireStationRequest.builder().address(getAddress1()).station(0).build();
        assertNull(fireStationService.updateFireStation(request));
    }

    @Test
    void testUpdateFireStationNotFound() {
        FireStationRequest request = FireStationRequest.builder().address(getAddress1()).station(1).build();
        assertNull(fireStationService.updateFireStation(request));
    }

    @Test
    void testUpdateFireStation() {
        FireStationRequest request = FireStationRequest.builder().address(getAddress1()).station(2).build();
        fireStationRepository.save(getFireStation01(getAddress1(), 1));
        FireStation fireStation = getFireStation01(getAddress1(), 2);
        FireStationResponse response = fireStation.toFireStationResponse();
        assertEquals(response, fireStationService.updateFireStation(request));
    }

    @Test
    void testDeleteFireStationAddressNullStationNull() {
        FireStationRequest request = FireStationRequest.builder().address(null).station(null).build();
        assertFalse(fireStationService.deleteFireStation(request));
    }

    @Test
    void testDeleteFireStationAddressNullStationZero() {
        FireStationRequest request = FireStationRequest.builder().address(null).station(0).build();
        assertFalse(fireStationService.deleteFireStation(request));
    }

    @Test
    void testDeleteFireStationAddressNotNullStationNotFound() {
        FireStationRequest request = FireStationRequest.builder().address(getAddress1()).station(0).build();
        assertFalse(fireStationService.deleteFireStation(request));
    }

    @Test
    void testDeleteFireStationAddressNotNullStationNotNull() {
        FireStationRequest request = FireStationRequest.builder().address(getAddress1()).station(1).build();
        fireStationRepository.save(getFireStation01(getAddress1(), 1));
        assertTrue(fireStationService.deleteFireStation(request));
    }

    @Test
    void testDeleteFireStationAddressNullStationNotNull() {
        FireStationRequest request = FireStationRequest.builder().address(null).station(1).build();
        fireStationRepository.save(getFireStation01(getAddress1(), 1));
        assertTrue(fireStationService.deleteFireStation(request));
    }

    @Test
    void testDeleteFireStationAddressNullStationNotNullMultiple() {
        FireStationRequest request = FireStationRequest.builder().address(null).station(1).build();
        fireStationRepository.save(getFireStation01(getAddress1(), 1));
        fireStationRepository.save(getFireStation01(getAddress2(), 1));
        assertTrue(fireStationService.deleteFireStation(request));
    }

    @Test
    void testDeleteFireStationAddressNullStationNotFound() {
        FireStationRequest request = FireStationRequest.builder().address(null).station(1).build();
        assertFalse(fireStationService.deleteFireStation(request));
    }

}
