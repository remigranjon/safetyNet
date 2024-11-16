package com.safetynet.alerts.controller;

import com.safetynet.alerts.model.request.FireStationRequest;
import com.safetynet.alerts.model.response.FireStationResponse;
import com.safetynet.alerts.model.response.PersonsWithCountResponse;
import com.safetynet.alerts.service.FireStationService;
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
    public FireStationResponse saveFireStation(@RequestBody FireStationRequest fireStationRequest) {
        return fireStationService.saveFireStation(fireStationRequest);
    }

    @PutMapping
    public FireStationResponse updateFireStation(@RequestBody FireStationRequest fireStationRequest) {
        return fireStationService.updateFireStation(fireStationRequest);
    }

    @DeleteMapping
    public boolean deleteFireStation(@RequestBody FireStationRequest fireStationRequest) {
        return fireStationService.deleteFireStation(fireStationRequest);
    }
}
