package com.safetynet.alerts.model.response;

import com.safetynet.alerts.model.entity.Medication;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MedicationResponse {
    private String name;
    private String dosage;

    public MedicationResponse(Medication m) {
        this.name = m.getName();
        this.dosage = m.getDosage();
    }
}
