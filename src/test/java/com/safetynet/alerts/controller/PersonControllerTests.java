package com.safetynet.alerts.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alerts.model.request.NewPersonRequest;
import com.safetynet.alerts.model.request.PersonMinimalRequest;
import com.safetynet.alerts.model.request.PersonRequest;
import com.safetynet.alerts.model.response.PersonResponse;
import com.safetynet.alerts.service.PersonService;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PersonController.class)
public class PersonControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonService personService;

    @Nested
    class SavePersonTests {
        @Test
        void testSavePerson() throws Exception {
            NewPersonRequest personRequest = NewPersonRequest.builder().firstName("John").lastName("Doe")
                    .birthdate("01/01/2000").address("123 Main St").city("Springfield").zip("12345")
                    .build();
            ObjectMapper objectMapper = new ObjectMapper();
            when(personService.savePerson(any(NewPersonRequest.class)))
                    .thenReturn(true);
            mockMvc.perform(post("/person")
                            .contentType("application/json")
                            .content(objectMapper.writeValueAsString(personRequest)))
                    .andExpect(status().isCreated());
        }

        @Test
        void testSavePersonBadRequest() throws Exception {
            NewPersonRequest personRequest = NewPersonRequest.builder().firstName("John").lastName("Doe")
                    .birthdate("01/01/2000").address("123 Main St").city("Springfield").zip("12345")
                    .build();
            ObjectMapper objectMapper = new ObjectMapper();
            when(personService.savePerson(any(NewPersonRequest.class)))
                    .thenReturn(false);
            mockMvc.perform(post("/person")
                            .contentType("application/json")
                            .content(objectMapper.writeValueAsString(personRequest)))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    class UpdatePersonTests {
        @Test
        void testUpdatePerson() throws Exception {
            PersonRequest personRequest = PersonRequest.builder().firstName("John").lastName("Doe")
                    .address("123 Main St").city("Springfield").zip("12345")
                    .build();
            ObjectMapper objectMapper = new ObjectMapper();
            when(personService.updatePerson(any(PersonRequest.class)))
                    .thenReturn(true);
            mockMvc.perform(put("/person")
                            .contentType("application/json")
                            .content(objectMapper.writeValueAsString(personRequest)))
                    .andExpect(status().isOk());
        }

        @Test
        void testUpdatePersonBadRequest() throws Exception {
            PersonRequest personRequest = PersonRequest.builder().firstName("John").lastName("Doe")
                    .address("123 Main St").city("Springfield").zip("12345")
                    .build();
            ObjectMapper objectMapper = new ObjectMapper();
            when(personService.updatePerson(any(PersonRequest.class)))
                    .thenReturn(false);
            mockMvc.perform(put("/person")
                            .contentType("application/json")
                            .content(objectMapper.writeValueAsString(personRequest)))
                    .andExpect(status().isBadRequest());
        }
    }
    @Nested
    class DeletePersonTests {
        @Test
        void testDeletePerson() throws Exception {
            PersonMinimalRequest personMinimalRequest = PersonMinimalRequest.builder()
                    .firstName("John").lastName("Doe").build();
            ObjectMapper objectMapper = new ObjectMapper();
            when(personService.deletePerson(any()))
                    .thenReturn(true);
            mockMvc.perform(delete("/person")
                            .contentType("application/json")
                            .content(objectMapper.writeValueAsString(personMinimalRequest)))
                    .andExpect(status().isOk());
        }

        @Test
        void testDeletePersonBadRequest() throws Exception {
            PersonMinimalRequest personMinimalRequest = PersonMinimalRequest.builder()
                    .firstName("John").lastName("Doe").build();
            ObjectMapper objectMapper = new ObjectMapper();
            when(personService.deletePerson(any()))
                    .thenReturn(false);
            mockMvc.perform(delete("/person")
                            .contentType("application/json")
                            .content(objectMapper.writeValueAsString(personMinimalRequest)))
                    .andExpect(status().isBadRequest());
        }
    }

}