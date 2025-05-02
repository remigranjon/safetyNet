package com.safetynet.alerts.model.request;

import com.safetynet.alerts.model.entity.MedicalRecord;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MedicalRecordRequest {
    private String firstName;
    private String lastName;
    private String birthdate;
    private Set<MedicationRequest> medications;
    private Set<String> allergies;

    public MedicalRecord toEntity() {
        return MedicalRecord.builder()
                .firstName(firstName)
                .lastName(lastName)
                .birthdate(birthdate)
                .medications(medications != null ? medications.stream()
                        .map(MedicationRequest::toEntity)
                        .collect(Collectors.toSet()): Set.of())
                .allergies(allergies)
                .build();
    }

    public boolean isValid() {
        return firstName != null && !firstName.isEmpty()
                && lastName != null && !lastName.isEmpty()
                && birthdate != null && isBirthdateValid();
    }

    public boolean isBirthdateValid() {
        String regex = "^(0[1-9]|1[0-2])/([0-2][0-9]|3[01])/\\d{4}$";
        return Pattern.matches(regex, birthdate);
    }
}
