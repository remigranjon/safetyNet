package com.safetynet.alerts.controller;

import com.safetynet.alerts.model.response.PersonsWithCountResponse;
import com.safetynet.alerts.service.FireStationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/firestation")
public class FireStationController {

    private final FireStationService fireStationService;

    public FireStationController(FireStationService fireStationService) {this.fireStationService = fireStationService;}

    @GetMapping
    public PersonsWithCountResponse getPersonsByStation(@RequestParam("stationNumber") int stationNumber) {
        return fireStationService.getPersonsWithCountByStation(stationNumber);
    }
}
