package com.safetynet.alerts.controller;

import com.safetynet.alerts.model.request.FireStationRequest;
import com.safetynet.alerts.model.response.FireStationResponse;
import com.safetynet.alerts.model.response.PersonsWithCountResponse;
import com.safetynet.alerts.service.FireStationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/firestation")
public class FireStationController {

    private final FireStationService fireStationService;

    public FireStationController(FireStationService fireStationService) {this.fireStationService = fireStationService;}

    @GetMapping
    public PersonsWithCountResponse getPersonsByStation(@RequestParam("stationNumber") int stationNumber) {
        return fireStationService.getPersonsWithCountByStation(stationNumber);
    }

    @PostMapping
    public ResponseEntity<FireStationResponse> saveFireStation(@RequestBody FireStationRequest fireStationRequest) {
        FireStationResponse fireStationResponse = fireStationService.saveFireStation(fireStationRequest);
        if (fireStationResponse != null) {
            return new ResponseEntity<>(fireStationResponse, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping
    public ResponseEntity<FireStationResponse> updateFireStation(@RequestBody FireStationRequest fireStationRequest) {
        FireStationResponse fireStationResponse = fireStationService.updateFireStation(fireStationRequest);
        if (fireStationResponse != null) {
            return new ResponseEntity<>(fireStationResponse, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping
    public ResponseEntity<Boolean> deleteFireStation(@RequestBody FireStationRequest fireStationRequest) {
        boolean isDeleted = fireStationService.deleteFireStation(fireStationRequest);
        if (isDeleted) {
            return new ResponseEntity<>(true, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
        }
    }
}
