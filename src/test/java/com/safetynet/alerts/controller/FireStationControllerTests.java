package com.safetynet.alerts.controller;

import com.safetynet.alerts.model.response.FireStationResponse;
import com.safetynet.alerts.model.response.PersonsWithCountResponse;
import com.safetynet.alerts.service.FireStationService;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FireStationController.class)
class FireStationControllerTests {

        @Autowired
        private MockMvc mockMvc;

        @MockBean
        private FireStationService fireStationService;

        @Nested
        class GetPersonsByStationTests {

                @Test
                void testGetPersonsByStationWithValidStationNumber() throws Exception {
                        int stationNumber = 1;
                        PersonsWithCountResponse mockResponse = new PersonsWithCountResponse();
                        mockResponse.setAdultCount(2);
                        mockResponse.setChildrenCount(1);

                        when(fireStationService.getPersonsWithCountByStation(stationNumber)).thenReturn(mockResponse);

                        mockMvc.perform(get("/firestation")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .param("stationNumber", String.valueOf(stationNumber)))
                                        .andExpect(status().isOk())
                                        .andExpect(jsonPath("$.adultCount").value(2))
                                        .andExpect(jsonPath("$.childrenCount").value(1));
                }

                @Test
                void testGetPersonsByStationWithInvalidStationNumber() throws Exception {
                        int stationNumber = -1;

                        when(fireStationService.getPersonsWithCountByStation(stationNumber)).thenReturn(null);

                        mockMvc.perform(get("/firestation")
                                        .param("stationNumber", String.valueOf(stationNumber))
                                        .contentType(MediaType.APPLICATION_JSON))
                                        .andExpect(status().isBadRequest());
                }
        }

        @Nested
        class SaveFireStationTests {

                @Test
                void testSaveFireStation() throws Exception {
                        String fireStationRequestJson = "{ \"station\": 1, \"address\": \"123 Main St\" }";

                        when(fireStationService.saveFireStation(any()))
                                        .thenReturn(true);

                        mockMvc.perform(post("/firestation")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(fireStationRequestJson))
                                        .andExpect(status().isCreated());
                }

                @Test
                void testSaveFireStationWithInvalidData() throws Exception {
                        String fireStationRequestJson = "{ \"station\": -1, \"address\": \"\" }";

                        when(fireStationService.saveFireStation(any()))
                                        .thenReturn(false);

                        mockMvc.perform(post("/firestation")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(fireStationRequestJson))
                                        .andExpect(status().isBadRequest());
                }
        }

        @Nested
        class UpdateFireStationTests {

                @Test
                void testUpdateFireStation() throws Exception {
                        String fireStationRequestJson = "{ \"station\": 1, \"address\": \"123 Main St\" }";

                        when(fireStationService.updateFireStation(any()))
                                        .thenReturn(true);

                        mockMvc.perform(put("/firestation")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(fireStationRequestJson))
                                        .andExpect(status().isOk());
                }

                @Test
                void testUpdateFireStationWithInvalidData() throws Exception {
                        String fireStationRequestJson = "{ \"station\": -1, \"address\": \"\" }";

                        when(fireStationService.updateFireStation(any()))
                                        .thenReturn(false);

                        mockMvc.perform(put("/firestation")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(fireStationRequestJson))
                                        .andExpect(status().isBadRequest());
                }
        }

        @Nested
        class DeleteFireStationTests {

                @Test
                void testDeleteFireStation() throws Exception {
                        String fireStationRequestJson = "{ \"station\": 1, \"address\": \"123 Main St\" }";

                        when(fireStationService.deleteFireStation(any()))
                                        .thenReturn(true);

                        mockMvc.perform(delete("/firestation")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(fireStationRequestJson))
                                        .andExpect(status().isOk());
                }

                @Test
                void testDeleteFireStationWithInvalidData() throws Exception {
                        String fireStationRequestJson = "{ \"station\": -1, \"address\": \"\" }";

                        when(fireStationService.deleteFireStation(any()))
                                        .thenReturn(false);

                        mockMvc.perform(delete("/firestation")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(fireStationRequestJson))
                                        .andExpect(status().isBadRequest());
                }
        }

}