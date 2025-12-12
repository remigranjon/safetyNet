package com.safetynet.alerts.service;

import com.safetynet.alerts.model.request.MedicalRecordRequest;
import com.safetynet.alerts.model.request.PersonMinimalRequest;
import com.safetynet.alerts.repository.interfaces.MedicalRecordRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;


@Service
public class MedicalRecordService {
    private final MedicalRecordRepository medicalRecordRepository;
    private final Logger logger = LogManager.getLogger(MedicalRecordService.class);

    public MedicalRecordService(MedicalRecordRepository medicalRecordRepository) {
        this.medicalRecordRepository = medicalRecordRepository;
    }

    public boolean saveMedicalRecord(MedicalRecordRequest medicalRecordRequest) {
        if (medicalRecordRequest == null || !medicalRecordRequest.isValid()) {
            logger.error("Error while creating medical record : request not valid");
           return false;
        }
        if (medicalRecordRepository.findByFirstNameAndLastName(medicalRecordRequest.getFirstName(), medicalRecordRequest.getLastName()) != null) {
            logger.error("Error while creating medical record : medical record already exists");
            return false;
        }
        return medicalRecordRepository.save(medicalRecordRequest.toEntity());
    }

    public boolean updateMedicalRecord(MedicalRecordRequest medicalRecordRequest) {
        if (medicalRecordRequest == null || medicalRecordRequest.getFirstName() == null || medicalRecordRequest.getLastName() == null || medicalRecordRequest.getBirthdate() != null && !medicalRecordRequest.isBirthdateValid()) {
            logger.error("Error while updating medical record : request not valid");
            return false;
        }
        try {
            return medicalRecordRepository.update(medicalRecordRequest.toEntity());
        } catch (IllegalArgumentException e) {
            logger.error("Error while updating medical record : " + e.getMessage());
            return false;
        }
    }

    public boolean deleteMedicalRecord(PersonMinimalRequest personMinimalRequest) {
        if (personMinimalRequest == null || personMinimalRequest.getFirstName() == null || personMinimalRequest.getLastName() == null) {
            logger.error("Error while deleting medical record : request not valid");
            return false;
        }
        return medicalRecordRepository.delete(personMinimalRequest.getFirstName(), personMinimalRequest.getLastName());
    }




}
