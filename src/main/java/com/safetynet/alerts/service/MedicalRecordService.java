package com.safetynet.alerts.service;

import com.safetynet.alerts.model.entity.MedicalRecord;
import com.safetynet.alerts.model.request.MedicalRecordRequest;
import com.safetynet.alerts.model.request.MedicationRequest;
import com.safetynet.alerts.model.request.PersonMinimalRequest;
import com.safetynet.alerts.model.response.FullMedicalRecordResponse;
import com.safetynet.alerts.repository.interfaces.MedicalRecordRepository;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class MedicalRecordService {
    private final MedicalRecordRepository medicalRecordRepository;

    public MedicalRecordService(MedicalRecordRepository medicalRecordRepository) {
        this.medicalRecordRepository = medicalRecordRepository;
    }

    public FullMedicalRecordResponse saveMedicalRecord(MedicalRecordRequest medicalRecordRequest) {
        if (medicalRecordRequest == null || !medicalRecordRequest.isValid()) {
           return null;
        }
        MedicalRecord medicalRecordSaved = medicalRecordRepository.save(medicalRecordRequest.toEntity());
        if (medicalRecordSaved == null) {
            return null;
        }
        return medicalRecordSaved.toFullResponse();
    }

    public FullMedicalRecordResponse updateMedicalRecord(MedicalRecordRequest medicalRecordRequest) {
        if (medicalRecordRequest.getFirstName() == null || medicalRecordRequest.getLastName() == null) {
            return null;
        }
        MedicalRecord medicalRecord = medicalRecordRepository.findByFirstNameAndLastName(medicalRecordRequest.getFirstName(), medicalRecordRequest.getLastName());
        if (medicalRecord == null) {
            return null;
        }
        medicalRecordRepository.delete(medicalRecord);
        if (medicalRecordRequest.getBirthdate() != null) {
            medicalRecord.setBirthdate(medicalRecordRequest.getBirthdate());
        }
        if (medicalRecordRequest.getMedications() != null) {
            medicalRecord.setMedications(medicalRecordRequest.getMedications().stream()
                    .map(MedicationRequest::toEntity)
                    .collect(Collectors.toSet()));
        }
        if (medicalRecordRequest.getAllergies() != null) {
            medicalRecord.setAllergies(medicalRecordRequest.getAllergies());
        }
        MedicalRecord medicalRecordUpdated = medicalRecordRepository.save(medicalRecord);
        if (medicalRecordUpdated == null) {
            return null;
        }
        return medicalRecordUpdated.toFullResponse();
    }

    public boolean deleteMedicalRecord(PersonMinimalRequest personMinimalRequest) {
        if (personMinimalRequest == null || personMinimalRequest.getFirstName() == null || personMinimalRequest.getLastName() == null) {
            return false;
        }
        MedicalRecord medicalRecord = medicalRecordRepository.findByFirstNameAndLastName(personMinimalRequest.getFirstName(), personMinimalRequest.getLastName());
        if (medicalRecord == null) {
            return false;
        }
        return medicalRecordRepository.delete(medicalRecord);
    }




}
