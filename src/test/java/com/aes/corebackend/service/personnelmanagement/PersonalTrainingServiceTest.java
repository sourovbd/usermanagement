package com.aes.corebackend.service.personnelmanagement;


import com.aes.corebackend.dto.APIResponse;
import com.aes.corebackend.dto.personnelmanagement.PersonalTrainingDTO;
import com.aes.corebackend.entity.User;
import com.aes.corebackend.entity.personnelmanagement.PersonalTrainingInfo;
import com.aes.corebackend.repository.personnelmanagement.PersonalTrainingRepository;
import com.aes.corebackend.service.UserService;
import com.aes.corebackend.service.personnelmanagement.PersonalTrainingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import static com.aes.corebackend.util.response.PersonnelManagementAPIResponseDescription.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class PersonalTrainingServiceTest {

    @Autowired
    private PersonalTrainingService personalTrainingService;

    @MockBean
    private PersonalTrainingRepository personalTrainingRepository;

    @MockBean
    private UserService userService;

    private User user = new User();
    private PersonalTrainingInfo personalTrainingInfo1 = new PersonalTrainingInfo();
    private PersonalTrainingInfo personalTrainingInfo2 = new PersonalTrainingInfo();
    private PersonalTrainingDTO personalTrainingDTO = new PersonalTrainingDTO();

    private DateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy");

    @BeforeEach
    private void personalTrainingSetup() throws ParseException {
        user.setId(1L);
        user.setDesignation("agm");
        user.setDepartment("accounts");
        user.setEmailAddress("mdahad118@gmail.com");
        user.setBusinessUnit("a1polymar");
        user.setEmployeeId("0101");

        personalTrainingDTO.setProgramName("TestProgram");
        personalTrainingDTO.setTrainingInstitute("TestInstitute");
        personalTrainingDTO.setDescription("TestDescription");
        personalTrainingDTO.setStartDate(dateFormatter.parse("01-01-2022"));
        personalTrainingDTO.setEndDate(dateFormatter.parse("31-01-2022"));

        personalTrainingInfo1.setId(1L);
        personalTrainingInfo1.setProgramName("Test program");
        personalTrainingInfo1.setTrainingInstitute("Test Institute");
        personalTrainingInfo1.setDescription("Test Description");
        personalTrainingInfo1.setStartDate(dateFormatter.parse("01-01-2022"));
        personalTrainingInfo1.setEndDate(dateFormatter.parse("31-01-2022"));
        personalTrainingInfo1.setUser(user);

        personalTrainingInfo2.setId(2L);
        personalTrainingInfo2.setProgramName("Test_02 program");
        personalTrainingInfo2.setTrainingInstitute("Test_02 Institute");
        personalTrainingInfo2.setDescription("Test_02 Description");
        personalTrainingInfo2.setStartDate(dateFormatter.parse("01-02-2022"));
        personalTrainingInfo2.setEndDate(dateFormatter.parse("28-02-2022"));
        personalTrainingInfo2.setUser(user);
    }

    @Test
    @DisplayName("This is create - success")
    public void createTest() {

        APIResponse expectedResponse = new APIResponse();
        expectedResponse.setResponse(TRAINING_CREATE_SUCCESS, TRUE, null);

        Mockito.when(userService.getUserByUserId(1L)).thenReturn(user);
        Mockito.when(personalTrainingRepository.save(personalTrainingInfo1))
                .thenReturn(personalTrainingInfo1);

        APIResponse actualResponse = personalTrainingService.create(personalTrainingDTO, 1L);
        assertEquals(actualResponse, expectedResponse);
    }

    @Test
    @DisplayName("Update training record")
    public void updateTest() {

        APIResponse expectedResponse = new APIResponse();
        expectedResponse.setResponse(TRAINING_UPDATE_SUCCESS, TRUE, null);

        Mockito.when(userService.getUserByUserId(1L)).thenReturn(user);
        Mockito.when(personalTrainingRepository.findPersonalTrainingInfoByIdAndUserId(1L, 1L))
                .thenReturn(personalTrainingInfo1);

        APIResponse actualResponse = personalTrainingService.update(personalTrainingDTO, 1L, 1L);
        assertEquals(actualResponse, expectedResponse);
    }

    @Test
    @DisplayName("All training records for a user")
    public void readTrainingsTest() throws Exception {

        ArrayList<PersonalTrainingInfo> trainingList = new ArrayList<>();
        trainingList.add(personalTrainingInfo1);
        trainingList.add(personalTrainingInfo2);

        ArrayList<PersonalTrainingDTO> trainingDTOs = new ArrayList<>();
        trainingDTOs.add(PersonalTrainingDTO.getPersonalTrainingDTO(personalTrainingInfo1));
        trainingDTOs.add(PersonalTrainingDTO.getPersonalTrainingDTO(personalTrainingInfo2));

        APIResponse expectedResponse = new APIResponse();
        expectedResponse.setResponse(TRAINING_RECORDS_FOUND, TRUE, trainingDTOs);

        Mockito.when(userService.getUserByUserId(1L)).thenReturn(user);
        Mockito.when(personalTrainingRepository.findPersonalTrainingInfosByUserId(1L))
                .thenReturn(trainingList);

        APIResponse actualResponse = personalTrainingService.read(1L);
        assertEquals(actualResponse, expectedResponse);
    }

    @Test
    @DisplayName("Single training record for a user")
    public void readTest() throws Exception {
        APIResponse expectedResponse = new APIResponse();
        expectedResponse.setResponse(TRAINING_RECORD_FOUND, TRUE, PersonalTrainingDTO.getPersonalTrainingDTO(personalTrainingInfo1));

        Mockito.when(userService.getUserByUserId(1L)).thenReturn(user);
        Mockito.when(personalTrainingRepository.findPersonalTrainingInfoByIdAndUserId(1l, 1L))
                .thenReturn(personalTrainingInfo1);

        APIResponse actualResponse = personalTrainingService.read(1L, 1L);
        assertEquals(actualResponse, expectedResponse);
    }
}