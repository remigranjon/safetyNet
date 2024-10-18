package com.safetynet.alerts.controller;

import com.safetynet.alerts.model.response.ChildrenWithFamilyResponse;
import com.safetynet.alerts.service.PersonService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class URLsController {

    private final PersonService personService;

    public URLsController(PersonService personService) {this.personService = personService;}


    @GetMapping("/childAlert")
    public ChildrenWithFamilyResponse getChildrenWithFamily(@RequestParam("address") String address) {
        return personService.getChildrenWithFamilyByAddress(address);
    }
}
