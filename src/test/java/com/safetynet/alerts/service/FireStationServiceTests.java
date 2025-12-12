package com.safetynet.alerts.service;


import com.safetynet.alerts.model.entity.FireStation;
import com.safetynet.alerts.model.entity.MedicalRecord;
import com.safetynet.alerts.model.entity.Person;
import com.safetynet.alerts.model.request.FireStationRequest;
import com.safetynet.alerts.repository.interfaces.FireStationRepository;
import com.safetynet.alerts.repository.interfaces.MedicalRecordRepository;
import com.safetynet.alerts.repository.interfaces.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Set;


class FireStationServiceTests {

    @InjectMocks
    private FireStationService fireStationService;
    @Mock
    private FireStationRepository fireStationRepository;
    @Mock
    private PersonRepository personRepository;
    @Mock
    private MedicalRecordRepository medicalRecordRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Nested
    class CRUDTests {
        @Test
        void testSaveFireStationAddressNull() {
            FireStationRequest request = FireStationRequest.builder().address(null).station(1).build();
            assertFalse(fireStationService.saveFireStation(request));
        }
        @Test
        void testSaveFireStationStationNull() {
            FireStationRequest request = FireStationRequest.builder().address("1509 Culver St").station(null).build();
            assertFalse(fireStationService.saveFireStation(request));
        }
        @Test
        void testSaveFireStationValid() {
            FireStationRequest request = FireStationRequest.builder().address("1509 Culver St").station(1).build();
            when(fireStationRepository.save(any(FireStation.class))).thenReturn(true);
            assertTrue(fireStationService.saveFireStation(request));
            verify(fireStationRepository, times(1)).save(any(FireStation.class));
        }
        @Test
        void testSaveFireStationAlreadyExists() {
            FireStationRequest request = FireStationRequest.builder().address("1509 Culver St").station(1).build();
            when(fireStationRepository.save(any(FireStation.class))).thenThrow(new IllegalArgumentException("FireStation with address 1509 Culver St already exists."));
            assertFalse(fireStationService.saveFireStation(request));
            verify(fireStationRepository, times(1)).save(any(FireStation.class));
        }
        @Test
        void testUpdateFireStationValid() {
            FireStationRequest request = FireStationRequest.builder().address("1509 Culver St").station(2).build();
            when(fireStationRepository.put(any(String.class), any(Integer.class))).thenReturn(true);
            assertTrue(fireStationService.updateFireStation(request));
            verify(fireStationRepository, times(1)).put(any(String.class), any(Integer.class));
        }
        @Test
        void testUpdateFireStationNotFound() {
            FireStationRequest request = FireStationRequest.builder().address("1509 Culver St").station(2).build();
            when(fireStationRepository.put(any(String.class), any(Integer.class))).thenReturn(false);
            assertFalse(fireStationService.updateFireStation(request));
            verify(fireStationRepository, times(1)).put(any(String.class), any(Integer.class));
        }
        @Test
        void testUpdateFireStationAddressNull() {
            FireStationRequest request = FireStationRequest.builder().address(null).station(2).build();
            assertFalse(fireStationService.updateFireStation(request));
        }
        @Test
        void testUpdateFireStationStationNull() {
            FireStationRequest request = FireStationRequest.builder().address("1509 Culver St").station(null).build();
            assertFalse(fireStationService.updateFireStation(request));
        }
        @Test
        void testUpdateFireStationStationZero() {
            FireStationRequest request = FireStationRequest.builder().address("1509 Culver St").station(0).build();
            assertFalse(fireStationService.updateFireStation(request));
        }

        @Test 
        void testUpdateFireStationFailure() {
            FireStationRequest request = FireStationRequest.builder().address("1509 Culver St").station(2).build();
            when(fireStationRepository.put(any(String.class), any(Integer.class))).thenThrow(new IllegalArgumentException("FireStation with address 1509 Culver St not found."));
            assertFalse(fireStationService.updateFireStation(request));
            verify(fireStationRepository, times(1)).put(any(String.class), any(Integer.class));
        }
        @Test
        void testDeleteFireStationAddressNotNull() {
            FireStationRequest request = FireStationRequest.builder().address("1509 Culver St").station(null).build();
            when(fireStationRepository.deleteAdresse("1509 Culver St")).thenReturn(true);
            assertTrue(fireStationService.deleteFireStation(request));
            verify(fireStationRepository, times(1)).deleteAdresse("1509 Culver St");
        }
        @Test
        void testDeleteFireStationAddressAndStationNull() {
            FireStationRequest request = FireStationRequest.builder().address(null).station(null).build();
            assertFalse(fireStationService.deleteFireStation(request));
        }
        @Test
        void testDeleteFireStationStationNotNull() {
            FireStationRequest request = FireStationRequest.builder().address(null).station(1).build();
            when(fireStationRepository.deleteFireStation(1)).thenReturn(true);
            assertTrue(fireStationService.deleteFireStation(request));
            verify(fireStationRepository, times(1)).deleteFireStation(1);
        }
        
    }

    @Nested
    class GetPersonsWithCountByStationTests {
        @Test
        void testGetPersonsWithCountByStation() {
            int stationNumber = 1;
            when(fireStationRepository.findAddressesByStation(stationNumber))
                    .thenReturn(Set.of("1509 Culver St", "29 15th St"));

            when(personRepository.findByAddress("1509 Culver St"))
                    .thenReturn(Set.of(
                            Person.builder().firstName("John").lastName("Doe").build(),
                            Person.builder().firstName("Jane").lastName("Doe").build()
                    ));
            when(personRepository.findByAddress("29 15th St"))
                    .thenReturn(Set.of(
                            Person.builder().firstName("Jim").lastName("Beam").build()
                    ));

            when(medicalRecordRepository.findByFirstNameAndLastName("John", "Doe"))
                    .thenReturn(MedicalRecord.builder().birthdate("01/01/2000").build());
            when(medicalRecordRepository.findByFirstNameAndLastName("Jane", "Doe"))
                    .thenReturn(MedicalRecord.builder().birthdate("01/01/2010").build());
            when(medicalRecordRepository.findByFirstNameAndLastName("Jim", "Beam"))
                    .thenReturn(MedicalRecord.builder().birthdate("01/01/1980").build());

            var response = fireStationService.getPersonsWithCountByStation(stationNumber);

            assertEquals(3, response.getPersons().size());
            assertEquals(2, response.getAdultCount());
            assertEquals(1, response.getChildrenCount());
        }
    }
    
}
