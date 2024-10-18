package com.safetynet.alerts.model.entity;

import com.safetynet.alerts.model.response.PersonNamesAndAgeResponse;
import com.safetynet.alerts.model.response.PersonResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Person {
    private String firstName;
    private String lastName;
    private String address;
    private String city;
    private String zip;
    private String phone;
    private String email;

    public PersonResponse toPersonResponse() {
        return PersonResponse.builder()
                .firstName(firstName)
                .lastName(lastName)
                .address(address)
                .phone(phone)
                .build();
    }

    public Object toPersonNamesAndAgeResponse() {
        return PersonNamesAndAgeResponse.builder()
                .firstName(firstName)
                .lastName(lastName)
                .build();
    }
}
