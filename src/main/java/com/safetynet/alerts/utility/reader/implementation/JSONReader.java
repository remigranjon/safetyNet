package com.safetynet.alerts.utility.reader.implementation;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alerts.model.entity.FireStation;
import com.safetynet.alerts.model.entity.MedicalRecord;
import com.safetynet.alerts.model.entity.Person;
import com.safetynet.alerts.utility.reader.JSONNodes;
import com.safetynet.alerts.utility.reader.interfaces.DataReader;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class JSONReader implements DataReader {

    private final ObjectMapper mapper = new ObjectMapper();
    private final JsonNode rootNode;

    public JSONReader(String filePath) {
        try {
            rootNode = mapper.readTree(getClass().getResourceAsStream(filePath));
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid JSON");
        }
    }

    @Override
    public Set<Person> readPersons() {
        Set<Person> persons = new HashSet<>();
        JsonNode personsNode = rootNode.get(JSONNodes.PERSONS.getValue());
        if (personsNode != null) {
            Person[] personArray = mapper.convertValue(personsNode, Person[].class);
            persons.addAll(Arrays.asList(personArray));
        }
        return persons;
    }

    @Override
    public Set<FireStation> readFireStations() {
        Set<FireStation> fireStations = new HashSet<>();
        JsonNode fireStationsNode = rootNode.get(JSONNodes.FIRESTATIONS.getValue());
        if (fireStationsNode != null) {
            FireStation[] fireStationArray = mapper.convertValue(fireStationsNode, FireStation[].class);
            fireStations.addAll(Arrays.asList(fireStationArray));
        }
        return fireStations;
    }

    @Override
    public Set<MedicalRecord> readMedicalRecords() {
        Set<MedicalRecord> medicalRecords = new HashSet<>();
        JsonNode medicalRecordsNode = rootNode.get(JSONNodes.MEDICALRECORDS.getValue());
        if (medicalRecordsNode != null) {
            MedicalRecord[] medicalRecordArray = mapper.convertValue(medicalRecordsNode, MedicalRecord[].class);
            medicalRecords.addAll(Arrays.asList(medicalRecordArray));
        }
        return medicalRecords;
    }
}
