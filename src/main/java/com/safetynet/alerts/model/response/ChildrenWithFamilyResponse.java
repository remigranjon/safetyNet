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
public class ChildrenWithFamilyResponse {
    private Set<PersonNamesAndAgeResponse> children;
    private Set<PersonNamesAndAgeResponse> familyMembers;
}
