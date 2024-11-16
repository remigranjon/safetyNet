package com.safetynet.alerts.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PersonMinimalResponse {
    private String firstName;
    private String lastName;
    private String address;
    private String phone;
}
