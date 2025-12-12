package com.safetynet.alerts.utility.enums;

import lombok.Getter;


public enum JSONNodes {
    PERSONS("persons"),
    FIRESTATIONS("firestations"),
    MEDICALRECORDS("medicalrecords");

    @Getter
    private final String value;

    JSONNodes(String value) {
        this.value = value;
    }

}
