package com.safetynet.alerts.repository.implementation;

import com.safetynet.alerts.model.entity.FireStation;
import com.safetynet.alerts.repository.interfaces.FireStationRepository;
import com.safetynet.alerts.utility.reader.implementation.JSONReader;
import com.safetynet.alerts.utility.reader.interfaces.DataReader;

import java.util.Set;
import java.util.stream.Collectors;

public class FireStationRepositoryInMemory implements FireStationRepository {

    private final Set<FireStation> fireStations;

    public FireStationRepositoryInMemory(String dataFilePath) {
        DataReader dataReader = new JSONReader(dataFilePath);
        fireStations = dataReader.readFireStations();
    }

    @Override
    public Set<String> findAddressesByStation(int station) {
        return fireStations.stream()
                .filter(fireStation -> fireStation.getStation() == station)
                .map(FireStation::getAddress)
                .collect(Collectors.toSet());
    }

    @Override
    public int findStationByAddress(String address) {
        return fireStations.stream()
                .filter(fireStation -> fireStation.getAddress().equals(address))
                .map(FireStation::getStation)
                .findFirst()
                .orElse(0);
    }

    @Override
    public FireStation save(FireStation fireStation) {
        boolean isAdded = fireStations.add(fireStation);
        if (isAdded) {
            return FireStation.builder().address(fireStation.getAddress()).station(fireStation.getStation()).build();
        } else {
            return null;
        }
    }

    @Override
    public boolean delete(FireStation fireStation) {
        return fireStations.remove(fireStation);
    }

    @Override
    public FireStation findByAddress(String address) {
        return fireStations.stream()
                .filter(fireStation -> fireStation.getAddress().equals(address))
                .findFirst()
                .orElse(null);
    }

    @Override
    public Set<FireStation> findByStation(int station) {
        return fireStations.stream()
                .filter(fireStation -> fireStation.getStation() == station)
                .collect(Collectors.toSet());
    }
}
