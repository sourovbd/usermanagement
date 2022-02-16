package com.aes.corebackend.unittest.service.personnelmanagement;

import com.aes.corebackend.service.personnelmanagement.PersonalIdentificationService;
import com.aes.corebackend.util.response.APIResponse;
import com.aes.corebackend.dto.personnelmanagement.PersonalIdentificationInfoDTO;
import com.aes.corebackend.entity.usermanagement.User;
import com.aes.corebackend.entity.personnelmanagement.PersonalIdentificationInfo;
import com.aes.corebackend.repository.personnelmanagement.PersonalIdentificationInfoRepository;
import com.aes.corebackend.service.usermanagement.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static com.aes.corebackend.util.response.APIResponseStatus.*;
import static com.aes.corebackend.util.response.PMAPIResponseMessage.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class PersonalIdentificationServiceTest {

    @Autowired
    private PersonalIdentificationService personalIdentificationService;

    @MockBean
    private PersonalIdentificationInfoRepository personalIdentificationRepository;

    @MockBean
    private UserService userService;

    private User user = new User();
    private PersonalIdentificationInfo personalIdentificationInfo = new PersonalIdentificationInfo();
    private PersonalIdentificationInfoDTO personalIdentificationInfoDTO = new PersonalIdentificationInfoDTO();
    private APIResponse expectedResponse = APIResponse.getApiResponse();

    @BeforeEach
    private void setup() {
        user.setId(1L);
        user.setDesignation("agm");
        user.setDepartment("accounts");
        user.setEmailAddress("mdahad118@gmail.com");
        user.setBusinessUnit("a1polymar");
        user.setEmployeeId("0101");

        personalIdentificationInfoDTO.setNationalID("1507359000");
        personalIdentificationInfoDTO.setEtin("751983938333");

        personalIdentificationInfo.setId(1L);
        personalIdentificationInfo.setEtin("751983938333");
        personalIdentificationInfo.setNationalID("1507359000");
        personalIdentificationInfo.setUser(user);
    }

    @Test
    @DisplayName("This is create - success")
    public void createSuccessTest() {

        expectedResponse.setResponse(IDENTIFICATION_CREATE_SUCCESS, TRUE, null, SUCCESS);
        Mockito.when(userService.getUserByUserId(1L)).thenReturn(user);
        Mockito.when(personalIdentificationRepository.save(personalIdentificationInfo))
                .thenReturn(personalIdentificationInfo);

        APIResponse actualResponse = personalIdentificationService.create(personalIdentificationInfoDTO, 1L);
        assertEquals(actualResponse, expectedResponse);
    }

    @Test
    @DisplayName("This is create - failed")
    public void createFailedTest() {

        expectedResponse.setResponse(IDENTIFICATION_CREATE_FAIL, FALSE, null, ERROR);
        Mockito.when(userService.getUserByUserId(1L)).thenReturn(user);
        Mockito.when(personalIdentificationRepository.save(Mockito.any(PersonalIdentificationInfo.class)))
                .thenThrow(new RuntimeException());

        APIResponse actualResponse = personalIdentificationService.create(personalIdentificationInfoDTO, 1L);
        assertEquals(actualResponse, expectedResponse);
    }

    @Test
    @DisplayName("This is update - success")
    public void updateSuccessTest() {

        expectedResponse.setResponse(IDENTIFICATION_UPDATE_SUCCESS, TRUE, null, SUCCESS);
        Mockito.when(userService.getUserByUserId(1L)).thenReturn(user);
        Mockito.when(personalIdentificationRepository.findPersonalIdentificationInfoByUserId(1L))
                .thenReturn(personalIdentificationInfo);

        APIResponse actualResponse = personalIdentificationService.update(personalIdentificationInfoDTO, 1L);
        assertEquals(actualResponse, expectedResponse);
    }

    @Test
    @DisplayName("This is update - failed")
    public void updateFailedTest() {

        expectedResponse.setResponse(IDENTIFICATION_UPDATE_FAIL, FALSE, null, ERROR);
        Mockito.when(userService.getUserByUserId(1L)).thenReturn(user);
        Mockito.when(personalIdentificationRepository.findPersonalIdentificationInfoByUserId(1L))
                .thenReturn(personalIdentificationInfo);
        Mockito.when(personalIdentificationRepository.save(Mockito.any(PersonalIdentificationInfo.class)))
                .thenThrow(new RuntimeException());

        APIResponse actualResponse = personalIdentificationService.update(personalIdentificationInfoDTO, 1L);
        assertEquals(actualResponse, expectedResponse);
    }

    @Test
    @DisplayName("This is read - success")
    public void readSuccessTest() {

        expectedResponse.setResponse(IDENTIFICATION_RECORD_FOUND, TRUE, PersonalIdentificationInfoDTO.getPersonalIdentificationDTO(personalIdentificationInfo), SUCCESS);
        Mockito.when(userService.getUserByUserId(1L)).thenReturn(user);
        Mockito.when(personalIdentificationRepository.findPersonalIdentificationInfoByUserId(1L))
                .thenReturn(personalIdentificationInfo);

        APIResponse actualResponse = personalIdentificationService.read(1L);
        assertEquals(actualResponse, expectedResponse);
    }

    @Test
    @DisplayName("This is read - failed")
    public void readFailedTest() {

        expectedResponse.setResponse(IDENTIFICATION_RECORD_NOT_FOUND, FALSE, null, ERROR);
        Mockito.when(userService.getUserByUserId(1L)).thenReturn(user);
        Mockito.when(personalIdentificationRepository.findPersonalIdentificationInfoByUserId(1L))
                .thenReturn(null);

        APIResponse actualResponse = personalIdentificationService.read(1L);
        assertEquals(actualResponse, expectedResponse);
    }
}