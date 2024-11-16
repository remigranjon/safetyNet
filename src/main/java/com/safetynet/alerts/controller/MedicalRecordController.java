package com.safetynet.alerts.controller;


import com.safetynet.alerts.model.request.MedicalRecordRequest;
import com.safetynet.alerts.model.request.PersonMinimalRequest;
import com.safetynet.alerts.model.response.FullMedicalRecordResponse;
import com.safetynet.alerts.service.MedicalRecordService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/medicalRecord")
public class MedicalRecordController {
    private final MedicalRecordService medicalRecordService;

    public MedicalRecordController(MedicalRecordService medicalRecordService) {
        this.medicalRecordService = medicalRecordService;
    }

    @PostMapping
    public ResponseEntity<FullMedicalRecordResponse> saveMedicalRecord(MedicalRecordRequest medicalRecordRequest) {
        FullMedicalRecordResponse fullMedicalRecordResponse = medicalRecordService.saveMedicalRecord(medicalRecordRequest);
        if (fullMedicalRecordResponse != null) {
            return new ResponseEntity<>(fullMedicalRecordResponse, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping
    public ResponseEntity<FullMedicalRecordResponse> updateMedicalRecord(MedicalRecordRequest medicalRecordRequest) {
        FullMedicalRecordResponse fullMedicalRecordResponse = medicalRecordService.updateMedicalRecord(medicalRecordRequest);
        if (fullMedicalRecordResponse != null) {
            return new ResponseEntity<>(fullMedicalRecordResponse, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping
    public ResponseEntity<Boolean> deleteMedicalRecord(PersonMinimalRequest personMinimalRequest) {
        boolean isDeleted = medicalRecordService.deleteMedicalRecord(personMinimalRequest);
        if (isDeleted) {
            return new ResponseEntity<>(true, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
        }
    }
}
