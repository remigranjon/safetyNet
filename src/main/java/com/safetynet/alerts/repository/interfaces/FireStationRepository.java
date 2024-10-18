package com.safetynet.alerts.repository.interfaces;


import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface FireStationRepository {
    Set<String> findAddressesByStation(int station);

    int findStationByAddress(String address);
}
