package com.safetynet.alerts.controller;

import com.safetynet.alerts.model.response.ChildrenWithFamilyResponse;
import com.safetynet.alerts.model.response.InhabitantsWithFireStationResponse;
import com.safetynet.alerts.model.response.PersonDetailResponse;
import com.safetynet.alerts.model.response.PersonWithMedicalRecordResponse;
import com.safetynet.alerts.service.PersonService;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(URLsController.class)
public class URLsControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonService personService;

    @Nested
    class GetChildrenWithFamilyTests {
        @Test
        void testGetChildrenWithFamily() throws Exception {
            String address = "123 Main St";
            when(personService.getChildrenWithFamilyByAddress(address)).thenReturn(new ChildrenWithFamilyResponse());
            mockMvc.perform(get("/childAlert")
                            .param("address", address))
                    .andExpect(status().isOk());

        }

        @Test
        void testGetChildrenWithFamilyNotFound() throws Exception {
            String address = "123 Main St";
            when(personService.getChildrenWithFamilyByAddress(address)).thenReturn(null);
            mockMvc.perform(get("/childAlert")
                            .param("address", address))
                    .andExpect(status().isOk());
        }
    }
    @Nested
    class GetPhoneNumbersByStationTests {
        @Test
        void testGetPhoneNumbersByStation() throws Exception {
            int stationNumber = 1;
            when(personService.getPhoneNumbersByStation(stationNumber)).thenReturn(Set.of("123-456-7890"));
            mockMvc.perform(get("/phoneAlert")
                            .param("firestation", String.valueOf(stationNumber)))
                    .andExpect(status().isOk());
        }
    }

    @Nested
    class GetInhabitantsWithFireStationTests {
        @Test
        void testGetInhabitantsWithFireStationNotFound() throws Exception {
            String address = "123 Main St";
            when(personService.getInhabitantsByAddress(address)).thenReturn(
                    InhabitantsWithFireStationResponse.builder().inhabitants(Set.of()).build());
            mockMvc.perform(get("/fire")
                            .param("address", address))
                    .andExpect(status().isNotFound());
        }
        @Test
        void testGetInhabitantsWithFireStationFound() throws Exception {
            String address = "123 Main St";
            when(personService.getInhabitantsByAddress(address)).thenReturn(
                    InhabitantsWithFireStationResponse.builder().inhabitants(Set.of(
                            PersonWithMedicalRecordResponse.builder().build())).station(1).build());
            mockMvc.perform(get("/fire")
                            .param("address", address))
                    .andExpect(status().isOk());
        }
        @Test
        void testGetInhabitantsWithFireStationEmpty() throws Exception {
            String address = "123 Main St";
            when(personService.getInhabitantsByAddress(address)).thenReturn(
                    InhabitantsWithFireStationResponse.builder().inhabitants(Set.of(
                            PersonWithMedicalRecordResponse.builder().build())).build());
            mockMvc.perform(get("/fire")
                            .param("address", address))
                    .andExpect(status().isNotFound());
        }

    }
    @Nested
    class GetInhabitantsByStationsTests {
        @Test
        void testGetInhabitantsByStations() throws Exception {
            Set<Integer> stations = Set.of(1, 2);
            when(personService.getInhabitantsByStations(stations)).thenReturn(Set.of());
            mockMvc.perform(get("/flood")
                            .param("stations", "1,2"))
                    .andExpect(status().isOk());
        }
        @Test
        void testGetInhabitantsByStationsNotFound() throws Exception {
            Set<Integer> stations = Set.of(1, 2);
            when(personService.getInhabitantsByStations(stations)).thenReturn(null);
            mockMvc.perform(get("/flood")
                            .param("stations", "1,2"))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    class GetPersonsInfoTests {
        @Test
        void testGetPersonsInfo() throws Exception {
            String lastName = "Doe";
            when(personService.getPersonsInfoByLastName(lastName)).thenReturn(Set.of(PersonDetailResponse.builder()
                    .build()));
            mockMvc.perform(get("/personInfo")
                            .param("lastName", lastName))
                    .andExpect(status().isOk());
        }
        @Test
        void testGetPersonsInfoNotFound() throws Exception {
            String lastName = "Doe";
            when(personService.getPersonsInfoByLastName(lastName)).thenReturn(Set.of());
            mockMvc.perform(get("/personInfo")
                            .param("lastName", lastName))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    class GetEmailsByCityTests {
        @Test
        void testGetEmailsByCity() throws Exception {
            String city = "test";
            when(personService.getEmailsByCity(city)).thenReturn(Set.of("test"));
            mockMvc.perform(get("/communityEmail")
                            .param("city", city))
                    .andExpect(status().isOk());
        }
        @Test
        void testGetEmailsByCityNotFound() throws Exception {
            String city = "test";
            when(personService.getEmailsByCity(city)).thenReturn(Set.of());
            mockMvc.perform(get("/communityEmail")
                            .param("city", city))
                    .andExpect(status().isNotFound());
        }
    }

}