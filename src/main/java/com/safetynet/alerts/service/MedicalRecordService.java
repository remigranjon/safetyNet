package com.safetynet.alerts.service;

import com.safetynet.alerts.model.entity.MedicalRecord;
import com.safetynet.alerts.model.request.MedicalRecordRequest;
import com.safetynet.alerts.model.request.MedicationRequest;
import com.safetynet.alerts.model.request.PersonMinimalRequest;
import com.safetynet.alerts.model.response.FullMedicalRecordResponse;
import com.safetynet.alerts.repository.interfaces.MedicalRecordRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class MedicalRecordService {
    private final MedicalRecordRepository medicalRecordRepository;
    private final Logger logger = LogManager.getLogger(MedicalRecordService.class);

    public MedicalRecordService(MedicalRecordRepository medicalRecordRepository) {
        this.medicalRecordRepository = medicalRecordRepository;
    }

    public FullMedicalRecordResponse saveMedicalRecord(MedicalRecordRequest medicalRecordRequest) {
        if (medicalRecordRequest == null || !medicalRecordRequest.isValid()) {
            logger.error("Error while creating medical record : request not valid");
           return null;
        }
        if (medicalRecordRepository.findByFirstNameAndLastName(medicalRecordRequest.getFirstName(), medicalRecordRequest.getLastName()) != null) {
            logger.error("Error while creating medical record : medical record already exists");
            return null;
        }
        return medicalRecordRepository.save(medicalRecordRequest.toEntity()).toFullResponse();
    }

    public FullMedicalRecordResponse updateMedicalRecord(MedicalRecordRequest medicalRecordRequest) {
        if (medicalRecordRequest == null || medicalRecordRequest.getFirstName() == null || medicalRecordRequest.getLastName() == null || medicalRecordRequest.getBirthdate() != null && !medicalRecordRequest.isBirthdateValid()) {
            logger.error("Error while updating medical record : request not valid");
            return null;
        }
        MedicalRecord medicalRecord = medicalRecordRepository.findByFirstNameAndLastName(medicalRecordRequest.getFirstName(), medicalRecordRequest.getLastName());
        if (medicalRecord == null) {
            logger.error("Error while updating medical record : medical record not found");
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
        return new FullMedicalRecordResponse(medicalRecordRepository.save(medicalRecord));
    }

    public boolean deleteMedicalRecord(PersonMinimalRequest personMinimalRequest) {
        if (personMinimalRequest == null || personMinimalRequest.getFirstName() == null || personMinimalRequest.getLastName() == null) {
            logger.error("Error while deleting medical record : request not valid");
            return false;
        }
        MedicalRecord medicalRecord = medicalRecordRepository.findByFirstNameAndLastName(personMinimalRequest.getFirstName(), personMinimalRequest.getLastName());
        if (medicalRecord == null) {
            logger.error("Error while deleting medical record : medical record not found");
            return false;
        }
        return medicalRecordRepository.delete(medicalRecord);
    }




}
