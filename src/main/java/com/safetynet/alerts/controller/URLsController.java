package com.safetynet.alerts.controller;

import com.safetynet.alerts.model.response.ChildrenWithFamilyResponse;
import com.safetynet.alerts.model.response.InhabitantsResponse;
import com.safetynet.alerts.model.response.InhabitantsWithFireStationResponse;
import com.safetynet.alerts.model.response.PersonDetailResponse;
import com.safetynet.alerts.service.PersonService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
public class URLsController {

    private final PersonService personService;

    public URLsController(PersonService personService) {this.personService = personService;}


    @GetMapping("/childAlert")
    public ChildrenWithFamilyResponse getChildrenWithFamily(@RequestParam("address") String address) {
        return personService.getChildrenWithFamilyByAddress(address);
    }

    @GetMapping("/phoneAlert")
    public Set<String> getPhoneNumbersByStation(@RequestParam("firestation") int stationNumber) {
        return personService.getPhoneNumbersByStation(stationNumber);
    }

    @GetMapping("/fire")
    public InhabitantsWithFireStationResponse getInhabitantsWithFireStation(@RequestParam("address") String address) {
        return personService.getInhabitantsByAddress(address);
    }

    @GetMapping("/flood")
    public Set<InhabitantsResponse> getInhabitantsByStations(@RequestParam("stations") Set<Integer> stations) {
        return personService.getInhabitantsByStations(stations);
    }

    @GetMapping("/personInfo")
    public Set<PersonDetailResponse> getPersonsInfo(@RequestParam("lastName") String lastName) {
        return personService.getPersonsInfoByLastName(lastName);
    }

    @GetMapping("/communityEmail")
    public Set<String> getEmailsByCity(@RequestParam("city") String city) {
        return personService.getEmailsByCity(city);
    }
}
