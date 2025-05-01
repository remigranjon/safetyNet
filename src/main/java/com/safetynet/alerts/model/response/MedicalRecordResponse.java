package com.safetynet.alerts.model.response;


import com.safetynet.alerts.model.entity.MedicalRecord;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicalRecordResponse {
    private Set<MedicationResponse> medications;
    private Set<String> allergies;

    public MedicalRecordResponse(MedicalRecord medicalRecord) {
        this.medications = medicalRecord.getMedications() != null ? medicalRecord.getMedications().stream()
                .map(MedicationResponse::new)
                .collect(Collectors.toSet()) : Set.of();
        this.allergies = medicalRecord.getAllergies();
    }
}
