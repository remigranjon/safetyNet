package com.safetynet.alerts.service;

import com.safetynet.alerts.model.entity.Person;
import com.safetynet.alerts.model.request.FireStationRequest;
import com.safetynet.alerts.model.response.PersonMinimalResponse;
import com.safetynet.alerts.model.response.PersonsWithCountResponse;
import com.safetynet.alerts.repository.interfaces.FireStationRepository;
import com.safetynet.alerts.repository.interfaces.MedicalRecordRepository;
import com.safetynet.alerts.repository.interfaces.PersonRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FireStationService {
    private final FireStationRepository fireStationRepository;
    private final PersonRepository personRepository;
    private final MedicalRecordRepository medicalRecordRepository;
    private final Logger logger = LogManager.getLogger(FireStationService.class);

    public FireStationService(FireStationRepository fireStationRepository, PersonRepository personRepository,
                              MedicalRecordRepository medicalRecordRepository) {
        this.fireStationRepository = fireStationRepository;
        this.personRepository = personRepository;
        this.medicalRecordRepository = medicalRecordRepository;
    }

    public PersonsWithCountResponse getPersonsWithCountByStation(int station) {
        Set<String> addresses = fireStationRepository.findAddressesByStation(station);
        Set<PersonMinimalResponse> persons = addresses.stream()
                .map(personRepository::findByAddress)
                .flatMap(Set::stream)
                .map(Person::toPersonMinimalResponse)
                .collect(Collectors.toSet());
        long adultCount = persons.stream()
                .filter(person -> medicalRecordRepository.findByFirstNameAndLastName(person.getFirstName(), person.getLastName()).isAdult())
                .count();
        long childrenCount = persons.size() - adultCount;

        return PersonsWithCountResponse.builder()
                .persons(persons)
                .adultCount(adultCount)
                .childrenCount(childrenCount)
                .build();
    }

    public boolean saveFireStation(FireStationRequest fireStationRequest) {
        if (fireStationRequest.getAddress() == null || fireStationRequest.getStation() == null || fireStationRequest.getStation() == 0 ) {
            logger.error("Error while creating fire station : request not valid");
            return false;
        }
        boolean fireStationSaved = false;
        try {
            fireStationSaved = fireStationRepository.save(fireStationRequest.toFireStation());
        } catch (IllegalArgumentException e) {
            logger.error("Error while creating fire station : " + e.getMessage());
            return false;
        }
        return fireStationSaved;
    }

    public boolean updateFireStation(FireStationRequest fireStationRequest) {
        if (fireStationRequest.getAddress() == null || fireStationRequest.getStation() == null || fireStationRequest.getStation() == 0) {
            logger.error("Error while updating fire station : request not valid");
            return false;
        }
        try { 
        return fireStationRepository.put(fireStationRequest.getAddress(), fireStationRequest.getStation());
        } catch (Exception e) {
            logger.error("Error while updating fire station : " + e.getMessage());
            return false;
        }
    }

    public boolean deleteFireStation( FireStationRequest fireStationRequest) {
        if (fireStationRequest.getAddress() == null && (fireStationRequest.getStation() == null || fireStationRequest.getStation() == 0)) {
            logger.error("Error while deleting fire station : request not valid");
            return false;
        }
        if (fireStationRequest.getAddress() != null) {
            return fireStationRepository.deleteAdresse(fireStationRequest.getAddress());
        }
        fireStationRepository.deleteFireStation(fireStationRequest.getStation());
        return true;
    }
}
