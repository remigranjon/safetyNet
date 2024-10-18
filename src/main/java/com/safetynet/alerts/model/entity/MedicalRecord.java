package com.safetynet.alerts.model.entity;


import com.safetynet.alerts.utility.converter.DateConverter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
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

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

        return DateConverter.convertStringToDate(birthdate, "dd/MM/yyyy").before(DateConverter.convertStringToDate(LocalDate.now().minusYears(18).toString(), "yyyy-MM-dd"));
    }
}
