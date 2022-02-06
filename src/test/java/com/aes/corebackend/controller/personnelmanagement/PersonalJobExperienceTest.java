package com.aes.corebackend.controller.personnelmanagement;

import com.aes.corebackend.dto.personnelmanagement.PersonalAddressInfoDTO;
import com.aes.corebackend.dto.personnelmanagement.PersonalJobExperienceDTO;
import com.aes.corebackend.dto.personnelmanagement.PersonnelManagementResponseDTO;
import com.aes.corebackend.entity.User;
import com.aes.corebackend.service.personnelmanagement.PersonalAddressService;
import com.aes.corebackend.service.personnelmanagement.PersonalJobExperienceService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PersonalJobExperienceTest {
    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    private JobExperienceController jobExperienceController;

    @Mock
    private PersonalJobExperienceService jobExperienceService;

    ObjectMapper om = new ObjectMapper();
    User user = new User();
    PersonalJobExperienceDTO jobExperienceDTO = new PersonalJobExperienceDTO();

    public void setup() throws ParseException {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders
                .standaloneSetup(jobExperienceController)
                .build();

        user.setId(1L);
        user.setDesignation("agm");
        user.setDepartment("accounts");
        user.setEmailAddress("mdahad118@gmail.com");
        user.setBusinessUnit("a1polymar");
        user.setEmployeeId("0101");

        jobExperienceDTO.setId(1L);
        jobExperienceDTO.setEmployerName("REVE");
        jobExperienceDTO.setDesignation("SDE");

        DateFormat formatter = new SimpleDateFormat("dd-mm-yyyy");
        jobExperienceDTO.setStartDate(formatter.parse("12-03-2017"));
        jobExperienceDTO.setEndDate(formatter.parse("12-12-2020"));
        jobExperienceDTO.setResponsibilities("development");
    }

    @Test
    public void createJobExperienceTest() throws Exception {
        PersonnelManagementResponseDTO response = new PersonnelManagementResponseDTO("Job experience creation successful", true, null);
        /** initialize service with expected response*/
        Mockito.when(jobExperienceService.create(jobExperienceDTO, user.getId())).thenReturn(response);

        String jsonRequestPayload = om.writeValueAsString(jobExperienceDTO);
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
                .post("/users/1/job-experiences")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonRequestPayload);

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Job experience creation successful"))
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    public void updateJobExperienceTest() throws Exception {
        PersonnelManagementResponseDTO responseDTO = new PersonnelManagementResponseDTO("Experience update successful", true, null);
        jobExperienceDTO.setResponsibilities("architecture design and development");

        /** initialize service with expected response*/
        Mockito.when(jobExperienceService.update(jobExperienceDTO, user.getId(), 1L)).thenReturn(responseDTO);

        String jsonRequestPayload = om.writeValueAsString(jobExperienceDTO);
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
                .put("/users/1/job-experiences/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonRequestPayload);

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Experience update successful"))
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    public void readSingleJobExperienceTest() throws Exception {
        PersonnelManagementResponseDTO responseDTO = new PersonnelManagementResponseDTO("Job experience read successful", true, null);

        /** initialize service with expected response*/
        Mockito.when(jobExperienceService.read(user.getId(), 1L)).thenReturn(responseDTO);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
                .get("/users/1/job-experiences/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Job experience read successful"))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").value(jobExperienceDTO));
    }
}
