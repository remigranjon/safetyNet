package com.safetynet.alerts.controller;

import com.safetynet.alerts.model.request.FireStationRequest;
import com.safetynet.alerts.model.response.PersonsWithCountResponse;
import com.safetynet.alerts.service.FireStationService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/firestation")
public class FireStationController {

    private final Logger logger = LogManager.getLogger(FireStationController.class);
    private final FireStationService fireStationService;

    public FireStationController(FireStationService fireStationService) {this.fireStationService = fireStationService;}

    @GetMapping
    public ResponseEntity<PersonsWithCountResponse> getPersonsByStation(@RequestParam("stationNumber") int stationNumber) {
        logger.info("Trying to get persons by station");
        PersonsWithCountResponse personsWithCountResponse = fireStationService.getPersonsWithCountByStation(stationNumber);
        if (personsWithCountResponse != null) {
            logger.info("Persons by station retrieved");
            return new ResponseEntity<>(personsWithCountResponse, HttpStatus.OK);
        } else {
            logger.warn("Persons by station not retrieved");
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping
    public ResponseEntity<Boolean> saveFireStation(@RequestBody FireStationRequest fireStationRequest) {
        logger.info("Trying to save fire station");
        boolean fireStationSaved = fireStationService.saveFireStation(fireStationRequest);
        if (fireStationSaved) {
            logger.info("Fire station saved");
            return new ResponseEntity<>(true, HttpStatus.CREATED);
        } else {
            logger.warn("Fire station not saved");
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping
    public ResponseEntity<Boolean> updateFireStation(@RequestBody FireStationRequest fireStationRequest) {
        logger.info("Trying to update fire station");
        boolean fireStationUpdated = fireStationService.updateFireStation(fireStationRequest);
        if (fireStationUpdated) {
            logger.info("Fire station updated");
            return new ResponseEntity<>(true, HttpStatus.OK);
        } else {
            logger.warn("Fire station not updated");
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping
    public ResponseEntity<Boolean> deleteFireStation(@RequestBody FireStationRequest fireStationRequest) {
        logger.info("Trying to delete fire station");
        boolean isDeleted = fireStationService.deleteFireStation(fireStationRequest);
        if (isDeleted) {
            logger.info("Fire station deleted");
            return new ResponseEntity<>(true, HttpStatus.OK);
        } else {
            logger.warn("Fire station not deleted");
            return ResponseEntity.badRequest().build();
        }
    }
}
