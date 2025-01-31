package com.safetynet.alerts.service;

import com.safetynet.alerts.model.entity.FireStation;
import com.safetynet.alerts.model.entity.Person;
import com.safetynet.alerts.model.request.FireStationRequest;
import com.safetynet.alerts.model.response.FireStationResponse;
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

    public FireStationResponse saveFireStation(FireStationRequest fireStationRequest) {
        if (fireStationRequest.getAddress() == null || fireStationRequest.getStation() == null || fireStationRequest.getStation() == 0 ) {
            logger.error("Error while creating fire station : request not valid");
            return null;
        }
        FireStation fireStationSaved = fireStationRepository.save(fireStationRequest.toFireStation());
        if (fireStationSaved == null) {
            logger.error("Error while creating fire station : saving unsuccessful");
            return null;
        }
        return fireStationSaved.toFireStationResponse();
    }

    public FireStationResponse updateFireStation(FireStationRequest fireStationRequest) {
        if (fireStationRequest.getAddress() == null || fireStationRequest.getStation() == null || fireStationRequest.getStation() == 0) {
            logger.error("Error while updating fire station : request not valid");
            return null;
        }
        FireStation fireStation = fireStationRepository.findByAddress(fireStationRequest.getAddress());
        if (fireStation == null) {
            logger.error("Error while updating fire station : fire station not found");
            return null;
        }
        fireStationRepository.delete(fireStation);
        fireStation.setStation(fireStationRequest.getStation());
        FireStation fireStationUpdated = fireStationRepository.save(fireStation);
        return fireStationUpdated.toFireStationResponse();
    }

    public boolean deleteFireStation( FireStationRequest fireStationRequest) {
        if (fireStationRequest.getAddress() == null && (fireStationRequest.getStation() == null || fireStationRequest.getStation() == 0)) {
            logger.error("Error while deleting fire station : request not valid");
            return false;
        }
        if (fireStationRequest.getAddress() != null) {
            FireStation fireStation = fireStationRepository.findByAddress(fireStationRequest.getAddress());
            if (fireStation == null) {
                logger.error("Error while deleting fire station : fire station not found");
                return false;
            }
            return fireStationRepository.delete(fireStation);
        }
        Set<FireStation> fireStations = fireStationRepository.findByStation(fireStationRequest.getStation());
        if (fireStations.isEmpty()) {
            logger.error("Error while deleting fire station : fire station not found");
            return false;
        }
        fireStations.forEach(fireStationRepository::delete);
        return true;
    }
}
