package com.safetynet.alerts.controller;

import com.safetynet.alerts.model.response.ChildrenWithFamilyResponse;
import com.safetynet.alerts.model.response.InhabitantsResponse;
import com.safetynet.alerts.model.response.InhabitantsWithFireStationResponse;
import com.safetynet.alerts.model.response.PersonDetailResponse;
import com.safetynet.alerts.service.PersonService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
public class URLsController {

    private final Logger logger = LogManager.getLogger(URLsController.class);

    private final PersonService personService;

    public URLsController(PersonService personService) {this.personService = personService;}


    @GetMapping("/childAlert")
    public ResponseEntity<ChildrenWithFamilyResponse> getChildrenWithFamily(@RequestParam("address") String address) {
        logger.info("Trying to get children with family by address");
        ChildrenWithFamilyResponse childrenWithFamilyResponse = personService.getChildrenWithFamilyByAddress(address);
        if (childrenWithFamilyResponse != null) {
            logger.info("Children with family by address retrieved");
            return ResponseEntity.ok(childrenWithFamilyResponse);
        } else {
            logger.warn("No children found for the given address");
            return ResponseEntity.ok(null);
        }
    }

    @GetMapping("/phoneAlert")
    public ResponseEntity<Set<String>> getPhoneNumbersByStation(@RequestParam("firestation") int stationNumber) {
        logger.info("Trying to get phone numbers by station");
        Set<String> phoneNumbers = personService.getPhoneNumbersByStation(stationNumber);
        logger.info("Phone numbers by station retrieved");
        return ResponseEntity.ok(phoneNumbers);

    }

    @GetMapping("/fire")
    public ResponseEntity<InhabitantsWithFireStationResponse> getInhabitantsWithFireStation(
            @RequestParam("address") String address) {
        logger.info("Trying to get inhabitants with fire station by address");
        InhabitantsWithFireStationResponse inhabitantsWithFireStationResponse =
                personService.getInhabitantsByAddress(address);
        if (inhabitantsWithFireStationResponse.getInhabitants().isEmpty() ||
                inhabitantsWithFireStationResponse.getStation() == null) {
            logger.warn("No inhabitants found for the given address");
            return ResponseEntity.notFound().build();
        }
        logger.info("Inhabitants with fire station by address retrieved");
        return ResponseEntity.ok(inhabitantsWithFireStationResponse);
    }

    @GetMapping("/flood")
    public ResponseEntity<Set<InhabitantsResponse>> getInhabitantsByStations(
            @RequestParam("stations") Set<Integer> stations) {
        logger.info("Trying to get inhabitants by stations");
        Set<InhabitantsResponse> inhabitantsResponses = personService.getInhabitantsByStations(stations);
        if (inhabitantsResponses != null) {
            logger.info("Inhabitants by stations retrieved");
            return ResponseEntity.ok(inhabitantsResponses);
        } else {
            logger.warn("Stations not found");
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/personInfo")
    public ResponseEntity<Set<PersonDetailResponse>> getPersonsInfo(@RequestParam("lastName") String lastName) {
        logger.info("Trying to get persons info by last name");
        Set<PersonDetailResponse> personDetailResponses = personService.getPersonsInfoByLastName(lastName);
        if (!personDetailResponses.isEmpty()) {
            logger.info("Persons info by last name retrieved");
            return ResponseEntity.ok(personDetailResponses);
        } else {
            logger.warn("Persons info by last name not retrieved");
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/communityEmail")
    public ResponseEntity<Set<String>> getEmailsByCity(@RequestParam("city") String city) {
        logger.info("Trying to get emails by city");
        Set<String> emails = personService.getEmailsByCity(city);
        if (!emails.isEmpty()) {
            logger.info("Emails by city retrieved");
            return ResponseEntity.ok(emails);
        } else {
            logger.warn("Emails by city not retrieved");
            return ResponseEntity.notFound().build();
        }
    }
}
