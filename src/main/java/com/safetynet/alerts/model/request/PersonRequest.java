package com.safetynet.alerts.model.request;


import com.safetynet.alerts.model.entity.Person;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PersonRequest {
    private String firstName;
    private String lastName;
    private String address;
    private String city;
    private String zip;
    private String phone;
    private String email;

    public Person toPerson() {
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

    public boolean isValid() {
        return firstName != null && !firstName.isEmpty()
                && lastName != null && !lastName.isEmpty()
                && address != null && !address.isEmpty()
                && city != null && !city.isEmpty()
                && zip != null && !zip.isEmpty()
                && phone != null && !phone.isEmpty()
                && email != null && !email.isEmpty();
    }
}
