package com.aes.corebackend.controller;

import com.aes.corebackend.dto.APIResponse;
import com.aes.corebackend.dto.UserDTO;
import com.aes.corebackend.entity.User;
import com.aes.corebackend.entity.UserCredential;
import com.aes.corebackend.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.assertj.core.internal.bytebuddy.matcher.ElementMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    ObjectMapper om = new ObjectMapper();
    UserCredential userCredential_1 = new UserCredential(1, "101", "a1wq", true, "EMPLOYEE");
    UserCredential userCredential_2 = new UserCredential(2, "102", "a1wq", true, "EMPLOYEE");
    UserCredential userCredential_3 = new UserCredential(3, "103", "a1wq", true, "EMPLOYEE");
    User user_1 = new User(1L, "abc@gmail.com", "agm", "101", "a1polymar", "accounts", "EMPLOYEE", userCredential_1, null, null);
    User user_2 = new User(2L, "abd@gmail.com", "agm", "102", "a1polymar", "accounts", "EMPLOYEE", userCredential_2, null, null);
    User user_3 = new User(3L, "abe@gmail.com", "agm", "103", "a1polymar", "accounts", "EMPLOYEE", userCredential_3, null, null);

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders
                .standaloneSetup(userController)
                .build();
    }

    @Test
    public void createUserTest() throws Exception {
        UserCredential userCredential = new UserCredential(1, "101", "a1wq", true, "EMPLOYEE");
        User user = new User();
        user.setId(1L);
        user.setDesignation("agm");
        user.setDepartment("accounts");
        user.setEmailAddress("mdahad118@gmail.com");
        user.setBusinessUnit("a1polymar");
        user.setEmployeeId("0101");
        user.setRoles("EMPLOYEE");
        user.setUserCredential(userCredential);

        UserDTO userDto = new UserDTO();
        userDto.setDesignation("agm");
        userDto.setDepartment("accounts");
        userDto.setEmailAddress("mdahad118@gmail.com");
        userDto.setBusinessUnit("a1polymar");
        userDto.setEmployeeId("0101");
        userDto.setRoles("EMPLOYEE");
        APIResponse responseDTO = new APIResponse();
        responseDTO.setMessage("user created successfully");
        responseDTO.setSuccess(true);
        responseDTO.setData(user);
        Mockito.when(userService.create(user,userDto)).thenReturn(responseDTO);

        String jsonRequest = om.writeValueAsString(user);
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
                .post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonRequest);
        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("user created successfully"));

    }

    @Test
    public void getAllUsers_success() throws Exception {
        List<User> users = Lists.newArrayList(user_1,user_2,user_3);
        APIResponse responseDTO =  new APIResponse();
        responseDTO.setMessage("user fetch ok");
        responseDTO.setSuccess(true);
        responseDTO.setData(users);
        Mockito.when(userService.read()).thenReturn(responseDTO);
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/users")
                        .header(HttpHeaders.AUTHORIZATION, "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIwMTI1MTkiLCJleHAiOjE2NDM4MTk4ODAsImlhdCI6MTY0Mzc4Mzg4MH0.5LF-tn-BGh20YpushocQv9pNLPaI1P_MDsxriO6w3zc")
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.message").value("user fetch ok"))
                        .andExpect(jsonPath("$.data[0].id").value(1L))
                        .andExpect(jsonPath("$.data[0].emailAddress").value("abc@gmail.com"))
                        .andExpect(jsonPath("$.data[0].designation").value("agm"))
                        .andExpect(jsonPath("$.data[0].employeeId").value("101"))
                        .andExpect(jsonPath("$.data[0].businessUnit").value("a1polymar"))
                        .andExpect(jsonPath("$.data[0].department").value("accounts"))
                        .andExpect(jsonPath("$.data[0].roles").value("EMPLOYEE"))
                        .andExpect(jsonPath("$.data[0].userCredential.active").value(true))
                        .andExpect(jsonPath("$.data[0].userCredential.password").value("a1wq"));
    }

    @Test
    public void getUserDetailsTest() throws Exception {
        APIResponse responseDTO =  new APIResponse();
        responseDTO.setMessage("user found");
        responseDTO.setSuccess(true);
        responseDTO.setData(user_1);
        Mockito.when(userService.read(1L)).thenReturn(responseDTO);
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/users/1")
                        .header(HttpHeaders.AUTHORIZATION, "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIwMTI1MTkiLCJleHAiOjE2NDM4MTk4ODAsImlhdCI6MTY0Mzc4Mzg4MH0.5LF-tn-BGh20YpushocQv9pNLPaI1P_MDsxriO6w3zc")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("user found"))
                .andExpect(jsonPath("$.data").value(user_1))
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.emailAddress").value("abc@gmail.com"))
                .andExpect(jsonPath("$.data.designation").value("agm"))
                .andExpect(jsonPath("$.data.employeeId").value("101"))
                .andExpect(jsonPath("$.data.businessUnit").value("a1polymar"))
                .andExpect(jsonPath("$.data.department").value("accounts"))
                .andExpect(jsonPath("$.data.roles").value("EMPLOYEE"))
                .andExpect(jsonPath("$.data.userCredential.active").value(true))
                .andExpect(jsonPath("$.data.userCredential.password").value("a1wq"));

    }

    @Test
    public void updateUserById() throws Exception {
        UserCredential userCredential = new UserCredential(1,"101","a1wq",true,"EMPLOYEE");
        User user = new User();
        user.setId(1L);
        user.setDesignation("agm");
        user.setDepartment("accounts");
        user.setEmailAddress("mdahad118@gmail.com");
        user.setBusinessUnit("a1polymar");
        user.setEmployeeId("0101");
        user.setRoles("EMPLOYEE");
        user.setUserCredential(userCredential);
        String jsonRequest = om.writeValueAsString(user);
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
                .put("/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest);
        mockMvc.perform(mockRequest).andExpect(status().isOk());
    }

}
