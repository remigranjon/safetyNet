package com.safetynet.alerts.model.response;


import com.safetynet.alerts.model.entity.MedicalRecord;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FullMedicalRecordResponse {
    private String firstName;
    private String lastName;
    private String birthdate;
    private Set<MedicationResponse> medications;
    private Set<String> allergies;

    public FullMedicalRecordResponse(MedicalRecord medicalRecord) {
        this.firstName = medicalRecord.getFirstName();
        this.lastName = medicalRecord.getLastName();
        this.birthdate = medicalRecord.getBirthdate();
        this.medications = medicalRecord.getMedications() != null ? medicalRecord.getMedications().stream()
                .map(MedicationResponse::new)
                .collect(Collectors.toSet()) : Set.of();
        this.allergies = medicalRecord.getAllergies();
    }
}
