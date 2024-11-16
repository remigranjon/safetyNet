package com.safetynet.alerts.model.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

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
}
