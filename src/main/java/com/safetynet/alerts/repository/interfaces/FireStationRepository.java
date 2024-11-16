package com.safetynet.alerts.repository.interfaces;


import com.safetynet.alerts.model.entity.FireStation;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface FireStationRepository {
    Set<String> findAddressesByStation(int station);

    int findStationByAddress(String address);

    FireStation save(FireStation fireStation);

    boolean delete(FireStation fireStation);

    FireStation findByAddress(String address);

    Set<FireStation> findByStation(int station);

}
