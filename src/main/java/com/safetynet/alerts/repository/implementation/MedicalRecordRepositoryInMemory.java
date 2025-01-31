package com.safetynet.alerts.repository.implementation;

import com.safetynet.alerts.model.entity.MedicalRecord;
import com.safetynet.alerts.repository.interfaces.MedicalRecordRepository;
import com.safetynet.alerts.utility.reader.implementation.JSONReader;
import com.safetynet.alerts.utility.reader.interfaces.DataReader;

import java.util.Set;

public class MedicalRecordRepositoryInMemory implements MedicalRecordRepository {
    private final Set<MedicalRecord> medicalRecords;

    public MedicalRecordRepositoryInMemory(String dataFilePath) {
        DataReader dataReader = new JSONReader(dataFilePath);
        medicalRecords = dataReader.readMedicalRecords();
    }

    @Override
    public MedicalRecord findByFirstNameAndLastName(String firstName, String lastName) {
        return medicalRecords.stream()
                .filter(medicalRecord -> medicalRecord.getFirstName().equals(firstName) && medicalRecord.getLastName().equals(lastName))
                .findFirst()
                .orElse(null);
    }

    @Override
    public MedicalRecord save(MedicalRecord medicalRecord) {
        boolean isAdded = medicalRecords.add(medicalRecord);
        if (isAdded) {
            return MedicalRecord.builder().firstName(medicalRecord.getFirstName()).lastName(medicalRecord.getLastName())
                    .birthdate(medicalRecord.getBirthdate()).medications(medicalRecord.getMedications())
                    .allergies(medicalRecord.getAllergies()).build();
        } else {
            return null;
        }
    }

    @Override
    public boolean delete(MedicalRecord medicalRecord) {
        return medicalRecords.remove(medicalRecord);
    }
}
