package com.safetynet.alerts.repository.implementation;

import com.safetynet.alerts.model.entity.FireStation;
import com.safetynet.alerts.repository.interfaces.FireStationRepository;
import com.safetynet.alerts.utility.reader.implementation.JSONReader;
import com.safetynet.alerts.utility.reader.interfaces.interfaces.DataReader;

import java.util.Set;
import java.util.stream.Collectors;

public class FireStationRepositoryInMemory implements FireStationRepository {

    private Set<FireStation> fireStations;

    public FireStationRepositoryInMemory() {
        DataReader dataReader = new JSONReader("/data/data.json");
        fireStations = dataReader.readFireStations();
    }

    @Override
    public Set<String> findAddressesByStation(int station) {
        return fireStations.stream()
                .filter(fireStation -> fireStation.getStation() == station)
                .map(FireStation::getAddress)
                .collect(Collectors.toSet());
    }
}
