package com.safetynet.alerts.repository.implementation;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.safetynet.alerts.model.entity.FireStation;
import com.safetynet.alerts.repository.interfaces.FireStationRepository;
import com.safetynet.alerts.utility.JSONReaderNode;
import com.safetynet.alerts.utility.JSONWriter;
import com.safetynet.alerts.utility.enums.JSONNodes;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class FireStationRepositoryJSON implements FireStationRepository {

    final ObjectMapper objectMapper = new ObjectMapper();
    final JSONWriter jsonWriter;
    final JSONReaderNode jsonReader;

    public FireStationRepositoryJSON(String dataFilePath) {
        jsonReader = new JSONReaderNode(dataFilePath);
        jsonWriter = new JSONWriter(dataFilePath);
    }

    @Override
    public Set<String> findAddressesByStation(Integer station) {
        final JsonNode fireStationNode = jsonReader.getRootNode().get(JSONNodes.FIRESTATIONS.getValue());
        List<String> addresses = new ArrayList<>();
        for (JsonNode firestation : fireStationNode) {
            if (firestation.get("station").asInt() == station) {
                addresses.add(firestation.get("address").asText());
            }
        }
        return new HashSet<>(addresses);
    }

    @Override
    public Integer findStationByAddress(String address) {
        final JsonNode fireStationNode = jsonReader.getRootNode().get(JSONNodes.FIRESTATIONS.getValue());
        for (JsonNode firestation : fireStationNode) {
            if (firestation.get("address").asText().equals(address)) {
                return firestation.get("station").asInt();
            }
        }
        return null;
    }

    @Override
    public boolean save(FireStation fireStation) {
        if (findByAddress(fireStation.getAddress()) != null) {
            throw new IllegalArgumentException("FireStation with address " + fireStation.getAddress() + " already exists.");
        }
        final JsonNode rootNode = jsonReader.getRootNode();
        ObjectNode newFireStationNode = objectMapper.createObjectNode();
        newFireStationNode.put("address", fireStation.getAddress());
        newFireStationNode.put("station", fireStation.getStation());
        ArrayNode firestationsNode = (ArrayNode) rootNode.get(JSONNodes.FIRESTATIONS.getValue());
        firestationsNode.add(newFireStationNode);
        return jsonWriter.writeJson(rootNode);
    }

    @Override
    public boolean deleteAdresse(String address) {
        boolean deleted = false;
        final JsonNode rootNode = jsonReader.getRootNode();
        Iterator<JsonNode> firestationsNode = rootNode.get(JSONNodes.FIRESTATIONS.getValue()).elements();
        while (firestationsNode.hasNext()) {
            JsonNode firestation = firestationsNode.next();
            if (firestation.get("address").asText().equals(address)) {
                firestationsNode.remove();
                deleted = true;
            }
        }
        jsonWriter.writeJson(rootNode);
        return deleted;
    }

    @Override
    public FireStation findByAddress(String address) {
        final JsonNode rootNode = jsonReader.getRootNode();
        for (JsonNode firestation : rootNode.get(JSONNodes.FIRESTATIONS.getValue())) {
            if (firestation.get("address").asText().equals(address)) {
                return new FireStation(
                        firestation.get("address").asText(),
                        firestation.get("station").asInt());
            }
        }
        return null;
    }

    @Override
    public Set<FireStation> findByStation(int station) {
        final JsonNode rootNode = jsonReader.getRootNode();
        Set<FireStation> fireStations = new HashSet<>();
        for (JsonNode firestation : rootNode.get(JSONNodes.FIRESTATIONS.getValue())) {
            if (firestation.get("station").asInt() == station) {
                fireStations.add(new FireStation(
                        firestation.get("address").asText(),
                        firestation.get("station").asInt()));
            }
        }
        return fireStations;
    }

    @Override
    public boolean put(String address, Integer station) {
        final JsonNode rootNode = jsonReader.getRootNode();
        for (JsonNode firestation : rootNode.get(JSONNodes.FIRESTATIONS.getValue())) {
            if (firestation.get("address").asText().equals(address)) {
                firestation = ((ObjectNode) firestation).put("station", station);
                return jsonWriter.writeJson(rootNode);
            }
        }
        return false;
    }

    @Override
    public boolean deleteFireStation(Integer station) {
        boolean deleted = false;
        final JsonNode rootNode = jsonReader.getRootNode();
        Iterator<JsonNode> firestationsNode = rootNode.get(JSONNodes.FIRESTATIONS.getValue()).elements();
        while (firestationsNode.hasNext()) {
            JsonNode firestation = firestationsNode.next();
            if (firestation.get("station").asInt() == station) {
                firestationsNode.remove();
                deleted = true;
            }
        }
        jsonWriter.writeJson(rootNode);
        return deleted;
    }
}
