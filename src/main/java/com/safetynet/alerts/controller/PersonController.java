package com.safetynet.alerts.controller;


import com.safetynet.alerts.model.request.PersonMinimalRequest;
import com.safetynet.alerts.model.request.PersonRequest;
import com.safetynet.alerts.model.response.PersonResponse;
import com.safetynet.alerts.service.PersonService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/person")

public class PersonController {

    private final Logger logger = LogManager.getLogger(PersonController.class);

    private final PersonService personService;

    public PersonController(PersonService personService) {this.personService = personService;}

    @PostMapping
    public ResponseEntity<PersonResponse> savePerson(@RequestBody PersonRequest personRequest) {
        logger.info("Trying to save person");
        PersonResponse personResponse = personService.savePerson(personRequest);
        if (personResponse != null) {
            logger.info("Person saved");
            return new ResponseEntity<>(personResponse, HttpStatus.CREATED);
        } else {
            logger.warn("Person not saved");
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping
    public ResponseEntity<PersonResponse> updatePerson(@RequestBody PersonRequest personRequest) {
        logger.info("Trying to update person");
        PersonResponse personResponse = personService.updatePerson(personRequest);
        if (personResponse != null) {
            logger.info("Person updated");
            return new ResponseEntity<>(personResponse, HttpStatus.OK);
        } else {
            logger.warn("Person not updated");
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping
    public ResponseEntity<Boolean> deletePerson(@RequestBody PersonMinimalRequest personMinimalRequest) {
        logger.info("Trying to delete person");
        boolean isDeleted = personService.deletePerson(personMinimalRequest);
        if (isDeleted) {
            logger.info("Person deleted");
            return new ResponseEntity<>(true, HttpStatus.OK);
        } else {
            logger.warn("Person not deleted");
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
        }
    }
}
