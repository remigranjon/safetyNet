package com.safetynet.alerts.service;

import com.safetynet.alerts.model.entity.Person;
import com.safetynet.alerts.model.response.PersonMinimalResponse;
import com.safetynet.alerts.model.response.PersonsWithCountResponse;
import com.safetynet.alerts.repository.interfaces.FireStationRepository;
import com.safetynet.alerts.repository.interfaces.MedicalRecordRepository;
import com.safetynet.alerts.repository.interfaces.PersonRepository;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FireStationService {
    private final FireStationRepository fireStationRepository;
    private final PersonRepository personRepository;
    private final MedicalRecordRepository medicalRecordRepository;

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

}
