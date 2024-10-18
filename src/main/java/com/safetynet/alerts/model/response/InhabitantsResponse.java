package com.safetynet.alerts.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InhabitantsResponse {
    private String address;
    private Set<PersonWithMedicalRecordResponse> inhabitants;
}
