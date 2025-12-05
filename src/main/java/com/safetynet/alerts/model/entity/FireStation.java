package com.safetynet.alerts.model.entity;


import com.safetynet.alerts.model.response.FireStationResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FireStation {
    private String address;
    private Integer station;

    public FireStationResponse toFireStationResponse() {
        return FireStationResponse.builder()
                .address(address)
                .station(station)
                .build();
    }
}
