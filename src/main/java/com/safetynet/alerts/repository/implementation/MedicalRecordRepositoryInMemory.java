package com.safetynet.alerts.repository.implementation;

import com.safetynet.alerts.model.entity.MedicalRecord;
import com.safetynet.alerts.repository.interfaces.MedicalRecordRepository;
import com.safetynet.alerts.utility.reader.implementation.JSONReader;
import com.safetynet.alerts.utility.reader.interfaces.interfaces.DataReader;

import java.util.Set;

public class MedicalRecordRepositoryInMemory implements MedicalRecordRepository {
    Set<MedicalRecord> medicalRecords;

    public MedicalRecordRepositoryInMemory() {
        DataReader dataReader = new JSONReader("/data/data.json");
        medicalRecords = dataReader.readMedicalRecords();

    }

    @Override
    public MedicalRecord findByFirstNameAndLastName(String firstName, String lastName) {
        return medicalRecords.stream()
                .filter(medicalRecord -> medicalRecord.getFirstName().equals(firstName) && medicalRecord.getLastName().equals(lastName))
                .findFirst()
                .orElse(null);
    }
}
