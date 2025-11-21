package com.safetynet.alerts.model.request;


import com.safetynet.alerts.model.entity.Medication;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MedicationRequest {
    private String name;
    private String dosage;

    public Medication toEntity() {
        return Medication.builder()
                .name(name)
                .dosage(dosage)
                .build();
    }
}
