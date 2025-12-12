package com.safetynet.alerts.model.DTO;

import com.safetynet.alerts.model.entity.MedicalRecord;
import com.safetynet.alerts.model.entity.Medication;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicalRecordDTO {
    private String firstName;
    private String lastName;
    private String birthdate;
    private Set<String> medications;
    private Set<String> allergies;

    public MedicalRecord toEntity() {
        Set<Medication> medications = new HashSet<>();
        if (this.medications != null) {
            for (String medication : this.medications) {
                String name = medication.split(":")[0];
                String dosage = medication.split(":")[1];
                medications.add(Medication.builder().name(name).dosage(dosage).build());
            }
        }
        return MedicalRecord.builder()
                .firstName(firstName)
                .lastName(lastName)
                .birthdate(birthdate)
                .medications(medications)
                .allergies(allergies)
                .build();
    }
   
}

