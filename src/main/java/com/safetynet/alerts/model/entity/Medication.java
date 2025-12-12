package com.safetynet.alerts.model.entity;


import com.safetynet.alerts.model.response.MedicationResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Medication {
    private String name;
    private String dosage;

    public MedicationResponse toResponse() {
        return MedicationResponse.builder()
                .name(name)
                .dosage(dosage)
                .build();
    }

    @Override
    public String toString() {
        return name + ":" + dosage;
    }   
}
