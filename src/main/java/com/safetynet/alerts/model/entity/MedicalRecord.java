package com.safetynet.alerts.model.entity;


import com.safetynet.alerts.utility.DateUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicalRecord {
    private String firstName;
    private String lastName;
    private String birthdate;
    private Set<String> medications;
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
}
