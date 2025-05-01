package com.safetynet.alerts.model.request;


import com.safetynet.alerts.model.entity.MedicalRecord;
import com.safetynet.alerts.model.entity.Person;
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
public class NewPersonRequest {
    private String firstName;
    private String lastName;
    private String address;
    private String city;
    private String zip;
    private String phone;
    private String email;
    private String birthdate;
    private Set<MedicationRequest> medications;
    private Set<String> allergies;

    public Person getPerson() {
        return Person.builder()
                .firstName(firstName)
                .lastName(lastName)
                .address(address)
                .city(city)
                .zip(zip)
                .phone(phone)
                .email(email)
                .build();
    }

    public MedicalRecord getMedicalRecord() {
        return MedicalRecord.builder()
                .firstName(firstName)
                .lastName(lastName)
                .birthdate(birthdate)
                .medications(medications != null ? medications.stream()
                        .map(MedicationRequest::toEntity)
                        .collect(Collectors.toSet()) : Set.of())
                .allergies(allergies)
                .build();
    }

    public boolean isValid() {
        return firstName != null && !firstName.isEmpty()
                && lastName != null && !lastName.isEmpty()
                && address != null && !address.isEmpty()
                && city != null && !city.isEmpty()
                && zip != null && !zip.isEmpty()
                && phone != null && !phone.isEmpty()
                && email != null && !email.isEmpty()
                && birthdate != null && isValidDate(birthdate);
    }

    private static boolean isValidDate(String date) {
        String regex = "^(0[1-9]|1[0-2])/([0-2][0-9]|3[01])/\\d{4}$";
        return Pattern.matches(regex, date);
    }
}
