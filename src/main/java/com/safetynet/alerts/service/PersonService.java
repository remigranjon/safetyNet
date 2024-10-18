package com.safetynet.alerts.service;

import com.safetynet.alerts.model.entity.MedicalRecord;
import com.safetynet.alerts.model.entity.Person;
import com.safetynet.alerts.model.response.*;
import com.safetynet.alerts.repository.interfaces.FireStationRepository;
import com.safetynet.alerts.repository.interfaces.MedicalRecordRepository;
import com.safetynet.alerts.repository.interfaces.PersonRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PersonService {

    private final PersonRepository personRepository;
    private final FireStationRepository fireStationRepository;
    private final MedicalRecordRepository medicalRecordRepository;

    public PersonService(PersonRepository personRepository, FireStationRepository fireStationRepository,
                         MedicalRecordRepository medicalRecordRepository) {
        this.personRepository = personRepository;
        this.fireStationRepository = fireStationRepository;
        this.medicalRecordRepository = medicalRecordRepository;
    }

    public ChildrenWithFamilyResponse getChildrenWithFamilyByAddress(String address) {
        Set<Person> persons = personRepository.findByAddress(address);
        Set<PersonNamesAndAgeResponse> children = new HashSet<>();
        Set<PersonNamesAndAgeResponse> familyMembers = new HashSet<>();
        persons
                .forEach(person -> {
                    PersonNamesAndAgeResponse response = PersonNamesAndAgeResponse.builder()
                            .firstName(person.getFirstName())
                            .lastName(person.getLastName())
                            .age(medicalRecordRepository.findByFirstNameAndLastName(person.getFirstName(), person.getLastName()).getAge())
                            .build();
                    if (response.getAge() <= 18) {
                        children.add(response);
                    } else {
                        familyMembers.add(response);
                    }
                    });
        return ChildrenWithFamilyResponse.builder()
                .children(children)
                .familyMembers(familyMembers)
                .build();


    }

    public Set<String> getPhoneNumbersByStation(int station) {
        Set<String> addresses = fireStationRepository.findAddressesByStation(station);
        return addresses.stream()
                .map(personRepository::findByAddress)
                .flatMap(Set::stream)
                .map(Person::getPhone)
                .collect(Collectors.toSet());
    }

    public InhabitantsWithFireStationResponse getInhabitantsByAddress(String address) {
        Set<Person> persons = personRepository.findByAddress(address);
        return InhabitantsWithFireStationResponse.builder()
                .inhabitants(persons.stream()
                        .map((Person person) -> {
                            MedicalRecord medicalRecord = medicalRecordRepository.findByFirstNameAndLastName(person.getFirstName(), person.getLastName());
                            return PersonWithMedicalRecordResponse.builder()
                                    .firstName(person.getFirstName())
                                    .lastName(person.getLastName())
                                    .phone(person.getPhone())
                                    .age(medicalRecord.getAge())
                                    .medicalRecord(medicalRecord.toResponse())
                                    .build();
                        })
                        .collect(Collectors.toSet()))
                .station(fireStationRepository.findStationByAddress(address))
                .build();
    }

    public Set<InhabitantsResponse> getInhabitantsByStations(Set<Integer> stations) {
        Set<String> addresses = stations.stream()
                .map(fireStationRepository::findAddressesByStation)
                .flatMap(Set::stream)
                .collect(Collectors.toSet());
        Set<InhabitantsResponse> responses = new HashSet<>();
        addresses.forEach(address -> {
            Set<Person> persons = personRepository.findByAddress(address);
            InhabitantsResponse response = InhabitantsResponse.builder().address(address).inhabitants(new HashSet<>()).build();
            persons.forEach(person -> {
                MedicalRecord medicalRecord = medicalRecordRepository.findByFirstNameAndLastName(person.getFirstName(), person.getLastName());
                response.getInhabitants().add(PersonWithMedicalRecordResponse.builder()
                        .firstName(person.getFirstName())
                        .lastName(person.getLastName())
                        .phone(person.getPhone())
                        .age(medicalRecord.getAge())
                        .medicalRecord(medicalRecord.toResponse())
                        .build());
            });
            responses.add(response);
            });
        return responses;
    }

}
