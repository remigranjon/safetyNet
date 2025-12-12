package com.safetynet.alerts.repository.implementation;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.safetynet.alerts.model.DTO.MedicalRecordDTO;
import com.safetynet.alerts.model.entity.MedicalRecord;
import com.safetynet.alerts.repository.interfaces.MedicalRecordRepository;
import com.safetynet.alerts.utility.JSONReaderNode;
import com.safetynet.alerts.utility.JSONWriter;

import java.util.Iterator;
import java.util.Set;

public class MedicalRecordRepositoryJSON implements MedicalRecordRepository {
    final ObjectMapper objectMapper = new ObjectMapper();
    final JSONWriter jsonWriter;
    final JSONReaderNode jsonReader;


    public MedicalRecordRepositoryJSON(String dataFilePath) {
        jsonReader = new JSONReaderNode(dataFilePath);
        jsonWriter = new JSONWriter(dataFilePath);
    }

    @Override
    public MedicalRecord findByFirstNameAndLastName(String firstName, String lastName) {
        final JsonNode medicalRecordsNode = jsonReader.getRootNode().get("medicalrecords");
        for (JsonNode medicalRecordNode : medicalRecordsNode) {
            if (medicalRecordNode.get("firstName").asText().equals(firstName) &&
                    medicalRecordNode.get("lastName").asText().equals(lastName)) {
                MedicalRecordDTO medicalRecordDTO = MedicalRecordDTO.builder()
                        .firstName(medicalRecordNode.get("firstName").asText())
                        .lastName(medicalRecordNode.get("lastName").asText())
                        .birthdate(medicalRecordNode.get("birthdate").asText())
                        .medications(objectMapper.convertValue(
                                medicalRecordNode.get("medications"),
                                objectMapper.getTypeFactory().constructCollectionType(Set.class, String.class)
                        ))
                        .allergies(objectMapper.convertValue(
                                medicalRecordNode.get("allergies"),
                                objectMapper.getTypeFactory().constructCollectionType(Set.class, String.class)
                        )).build();
                return medicalRecordDTO.toEntity();
            }}
        return null;
    }

    @Override
    public boolean save(MedicalRecord medicalRecord) {
        if (findByFirstNameAndLastName(medicalRecord.getFirstName(), medicalRecord.getLastName()) != null) {
            throw new IllegalArgumentException("MedicalRecord for " + medicalRecord.getFirstName() + " " + medicalRecord.getLastName() + " already exists.");
        }
        MedicalRecordDTO medicalRecordDTO = medicalRecord.toDTO();
        final JsonNode rootNode = jsonReader.getRootNode();
        ArrayNode medicalRecordsNode = (ArrayNode) rootNode.get("medicalrecords");
        ObjectNode newMedicalRecordNode = objectMapper.createObjectNode();
        newMedicalRecordNode.put("firstName", medicalRecord.getFirstName());
        newMedicalRecordNode.put("lastName", medicalRecord.getLastName());
        newMedicalRecordNode.put("birthdate", medicalRecord.getBirthdate());
        newMedicalRecordNode.putPOJO("medications", medicalRecordDTO.getMedications());
        newMedicalRecordNode.putPOJO("allergies", medicalRecordDTO.getAllergies());
        medicalRecordsNode.add(newMedicalRecordNode);
        jsonWriter.writeJson(rootNode);
        return true;
    }

    @Override
    public boolean delete(String firstName, String lastName) {
        boolean deleted = false;
        final JsonNode rootNode = jsonReader.getRootNode();
        Iterator<JsonNode> medicalRecordsNode = rootNode.get("medicalrecords").elements();
        while (medicalRecordsNode.hasNext()) {
            JsonNode medicalRecordNode = medicalRecordsNode.next();
            if (medicalRecordNode.get("firstName").asText().equals(firstName) &&
                    medicalRecordNode.get("lastName").asText().equals(lastName)) {
                medicalRecordsNode.remove();
                deleted = true;
            }
        }
        if (deleted) {
            jsonWriter.writeJson(rootNode);
        }
        return deleted;
    }

    @Override
    public boolean update(MedicalRecord medicalRecord) {
        final JsonNode rootNode = jsonReader.getRootNode();
        Iterator<JsonNode> medicalRecordsNode = rootNode.get("medicalrecords").elements();
        boolean found = false;
        while (medicalRecordsNode.hasNext()) {
            JsonNode medicalRecordNode = medicalRecordsNode.next();
            if (medicalRecordNode.get("firstName").asText().equals(medicalRecord.getFirstName()) &&
                    medicalRecordNode.get("lastName").asText().equals(medicalRecord.getLastName())) {
                found = true;
                medicalRecordsNode.remove();
            }
        }
        if (!found) {
            throw new IllegalArgumentException("MedicalRecord for " + medicalRecord.getFirstName() + " " + medicalRecord.getLastName() + " not found.");
        }
        MedicalRecordDTO medicalRecordDTO = medicalRecord.toDTO();
        ArrayNode medicalRecordsArrayNode = (ArrayNode) rootNode.get("medicalrecords");
        ObjectNode updatedMedicalRecordNode = objectMapper.createObjectNode();
        updatedMedicalRecordNode.put("firstName", medicalRecord.getFirstName());
        updatedMedicalRecordNode.put("lastName", medicalRecord.getLastName());
        updatedMedicalRecordNode.put("birthdate", medicalRecord.getBirthdate());
        updatedMedicalRecordNode.putPOJO("medications", medicalRecordDTO.getMedications());
        updatedMedicalRecordNode.putPOJO("allergies", medicalRecordDTO.getAllergies());
        medicalRecordsArrayNode.add(updatedMedicalRecordNode);
        jsonWriter.writeJson(rootNode);
        return true;
    }
}
