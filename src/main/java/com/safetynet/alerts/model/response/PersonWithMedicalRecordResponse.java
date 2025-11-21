package com.safetynet.alerts.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PersonWithMedicalRecordResponse {
    private String firstName;
    private String lastName;
    private String phone;
    private Long age;
    private MedicalRecordResponse medicalRecord;
}
