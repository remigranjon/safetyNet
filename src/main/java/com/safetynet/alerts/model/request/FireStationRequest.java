package com.safetynet.alerts.model.request;


import com.safetynet.alerts.model.entity.FireStation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FireStationRequest {
    private String address;
    private Integer station;

    public FireStation toFireStation() {
        return FireStation.builder()
                .address(address)
                .station(station)
                .build();
    }
}
