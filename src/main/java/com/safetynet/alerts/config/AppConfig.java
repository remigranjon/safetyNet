package com.safetynet.alerts.config;


import com.safetynet.alerts.repository.implementation.FireStationRepositoryInMemory;
import com.safetynet.alerts.repository.implementation.MedicalRecordRepositoryInMemory;
import com.safetynet.alerts.repository.implementation.PersonRepositoryInMemory;
import com.safetynet.alerts.repository.interfaces.FireStationRepository;
import com.safetynet.alerts.repository.interfaces.MedicalRecordRepository;
import com.safetynet.alerts.repository.interfaces.PersonRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    private static final String DATAFILE_PATH = "/data/data.json";

    @Bean
    public PersonRepository personRepository() {
        return new PersonRepositoryInMemory(DATAFILE_PATH);
    }

    @Bean
    public FireStationRepository fireStationRepository() {
        return new FireStationRepositoryInMemory(DATAFILE_PATH);
    }

    @Bean
    public MedicalRecordRepository medicalRecordRepository() {
        return new MedicalRecordRepositoryInMemory(DATAFILE_PATH);
    }

}
