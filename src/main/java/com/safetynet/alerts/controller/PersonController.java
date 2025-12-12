package com.safetynet.alerts.controller;


import com.safetynet.alerts.model.request.NewPersonRequest;
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
    public ResponseEntity<Boolean> savePerson(@RequestBody NewPersonRequest personRequest) {
        logger.info("Trying to save person");
        Boolean isSaved = personService.savePerson(personRequest);
        if (isSaved) {
            logger.info("Person saved");
            return new ResponseEntity<>(true, HttpStatus.CREATED);
        } else {
            logger.warn("Person not saved");
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping
    public ResponseEntity<Boolean> updatePerson(@RequestBody PersonRequest personRequest) {
        logger.info("Trying to update person");
        Boolean isUpdated = personService.updatePerson(personRequest);
        if (isUpdated) {
            logger.info("Person updated");
            return new ResponseEntity<>(true, HttpStatus.OK);
        } else {
            logger.warn("Person not updated");
            return ResponseEntity.badRequest().build();
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
            return ResponseEntity.badRequest().build();
        }
    }
}
