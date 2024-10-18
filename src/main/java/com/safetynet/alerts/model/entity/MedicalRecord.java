package com.safetynet.alerts.model.entity;


import com.safetynet.alerts.model.response.MedicalRecordResponse;
import com.safetynet.alerts.utility.DateUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicalRecord {
    private String firstName;
    private String lastName;
    private String birthdate;
    private Set<Medication> medications;
    private Set<String> allergies;


    public boolean isAdult() {
        return getAge() > 18;
    }

    public long getAge() {
        if (birthdate == null) {
            return 0;
        }
        Date currentDate = new Date();
        long diff = currentDate.getTime() - DateUtils.convertStringToDate(birthdate, "dd/MM/yyyy").getTime();
        return (diff / (1000L * 60 * 60 * 24 * 365));
    }

    public MedicalRecordResponse toResponse() {
        return MedicalRecordResponse.builder()
                .medications(medications.stream().map(Medication::toResponse).collect(Collectors.toSet()))
                .allergies(allergies)
                .build();
    }
}
