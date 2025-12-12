package com.safetynet.alerts.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.safetynet.alerts.model.entity.FireStation;
import com.safetynet.alerts.repository.implementation.FireStationRepositoryJSON;
import com.safetynet.alerts.utility.JSONWriter;
import com.safetynet.alerts.utility.enums.JSONNodes;

public class FireStationRepositoryJSONTests {
    FireStationRepositoryJSON fireStationRepositoryJSON = new FireStationRepositoryJSON("data/data_tests.json");

    @BeforeEach
    public void setUp() {
        initializeJSONData();
    }

    private void initializeJSONData() {
        JSONWriter jsonWriter = new JSONWriter("data/data_tests.json");
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode rootNode = objectMapper.createObjectNode();
        rootNode.putArray(JSONNodes.FIRESTATIONS.getValue());
        rootNode.putArray(JSONNodes.PERSONS.getValue());
        rootNode.putArray(JSONNodes.MEDICALRECORDS.getValue());
        jsonWriter.writeJson(rootNode);
    }

    @Test
    void testSaveAndGetFireStation() {
        FireStation fireStation = new FireStation(
                "123 Main St",
                5);
        fireStationRepositoryJSON.save(fireStation);
        assertEquals(fireStationRepositoryJSON.findByAddress("123 Main St"), fireStation);
    }

    @Test
    void testSaveAlreadyExistsAdress() {
        String address = "123 Main St";
        FireStation fireStation = new FireStation(
                address,
                5);
        fireStationRepositoryJSON.save(fireStation);
        FireStation anotherFireStation = new FireStation(
                address,
                10);
        assertThrows( IllegalArgumentException.class,() -> fireStationRepositoryJSON.save(anotherFireStation));
    }

    @Test
    void testUpdateFireStation() {
        String address = "123 Main St";
        FireStation fireStation = new FireStation(
                address,
                5);
        fireStationRepositoryJSON.save(fireStation);
        fireStationRepositoryJSON.put(address, 10);
        assertEquals(fireStationRepositoryJSON.findByAddress(address).getStation(), 10);
    }

    @Test
    void testUpdateFireStationNotFound() {
        String address = "Non Existent Address";
        boolean result = fireStationRepositoryJSON.put(address, 10);
        assertEquals(result, false);
    }

    @Test
    void testDeleteFireStationByAddress() {
        String address = "123 Main St";
        FireStation fireStation = new FireStation(
                address,
                5);
        fireStationRepositoryJSON.save(fireStation);
        fireStationRepositoryJSON.deleteAdresse(address);
        assertEquals(fireStationRepositoryJSON.findByAddress(address), null);
    }

    @Test
    void testDeleteFireStationByStation() {
        int station = 5;
        FireStation fireStation = new FireStation(
                "123 Main St",
                station);
        fireStationRepositoryJSON.save(fireStation);
        FireStation anotherFireStation = new FireStation(
                "456 Elm St",
                station);
        fireStationRepositoryJSON.save(anotherFireStation);
        fireStationRepositoryJSON.deleteFireStation(station);
        assertEquals(fireStationRepositoryJSON.findByStation(station).size(), 0);
    }

    @Test
    void testFindByStation() {
        int station = 5;
        FireStation fireStation = new FireStation(
                "123 Main St",
                station);
        fireStationRepositoryJSON.save(fireStation);
        FireStation anotherFireStation = new FireStation(
                "456 Elm St",
                station);
        fireStationRepositoryJSON.save(anotherFireStation);
        assertEquals(fireStationRepositoryJSON.findByStation(station).size(), 2);
    }

    @Test
    void testFindStationByAddress() {
        String address = "123 Main St";
        FireStation fireStation = new FireStation(
                address,
                5);
        fireStationRepositoryJSON.save(fireStation);
        assertEquals(fireStationRepositoryJSON.findStationByAddress(address), 5);
    }

    @Test
    void testFindByAddressNotFound() {
        assertEquals(fireStationRepositoryJSON.findByAddress("Non Existent Address"), null);
    }

    @Test
    void testFindStationByAddressNotFound() {
        assertEquals(fireStationRepositoryJSON.findStationByAddress("Non Existent Address"), null);
    }

    @Test
    void testFindAddressesByStation() {
        FireStation fireStation = new FireStation(
                "123 Main St",
                5);
                FireStation anotherFireStation = new FireStation(
                "456 Elm St",
                5);
                FireStation differentStationFireStation = new FireStation(
                "789 Oak St",
                10);
        fireStationRepositoryJSON.save(fireStation);
        fireStationRepositoryJSON.save(anotherFireStation);
        fireStationRepositoryJSON.save(differentStationFireStation);
        assertEquals(fireStationRepositoryJSON.findAddressesByStation(5).size(), 2);
    }

}