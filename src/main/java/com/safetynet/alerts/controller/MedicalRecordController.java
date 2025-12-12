package com.safetynet.alerts.controller;

import com.safetynet.alerts.model.request.MedicalRecordRequest;
import com.safetynet.alerts.model.request.PersonMinimalRequest;
import com.safetynet.alerts.service.MedicalRecordService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/medicalRecord")
public class MedicalRecordController {

    private final Logger logger = LogManager.getLogger(MedicalRecordController.class);

    private final MedicalRecordService medicalRecordService;

    public MedicalRecordController(MedicalRecordService medicalRecordService) {
        this.medicalRecordService = medicalRecordService;
    }

    @PostMapping
    public ResponseEntity<Boolean> saveMedicalRecord(MedicalRecordRequest medicalRecordRequest) {
        logger.info("Trying to save medical record");
        boolean isSaved = medicalRecordService.saveMedicalRecord(medicalRecordRequest);
        if (isSaved) {
            logger.info("Medical record saved");
            return new ResponseEntity<>(true, HttpStatus.CREATED);
        } else {
            logger.warn("Medical record not saved");
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping
    public ResponseEntity<Boolean> updateMedicalRecord(MedicalRecordRequest medicalRecordRequest) {
        logger.info("Trying to update medical record");
        boolean isUpdated = medicalRecordService.updateMedicalRecord(medicalRecordRequest);
        if (isUpdated) {
            logger.info("Medical record updated");
            return new ResponseEntity<>(true, HttpStatus.OK);
        } else {
            logger.warn("Medical record not updated");
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping
    public ResponseEntity<Boolean> deleteMedicalRecord(PersonMinimalRequest personMinimalRequest) {
        logger.info("Trying to delete medical record");
        boolean isDeleted = medicalRecordService.deleteMedicalRecord(personMinimalRequest);
        if (isDeleted) {
            logger.info("Medical record deleted");
            return new ResponseEntity<>(true, HttpStatus.OK);
        } else {
            logger.warn("Medical record not deleted");
            return ResponseEntity.badRequest().build();
        }
    }
}
