package com.aes.corebackend.dto.usermanagement;

import com.aes.corebackend.entity.usermanagement.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@Valid
public class UserDTO {

    private long id;
    @Pattern(regexp = "[a-zA-Z0-9.]*[@][a-zA-Z]+\\.(com|net|org)", message = "email id is invalid")
    @NotBlank(message = "email is mandatory")
    private String emailAddress;

    @NotBlank(message = "designation id is mandatory")
    private String designation;

    @NotBlank(message = "employee id is mandatory")
    private String employeeId;

    @NotBlank(message = "business unit is mandatory")
    private String businessUnit;

    @NotBlank(message = "department is mandatory")
    private String department;

    private String roles;

    public User dtoToEntity(UserDTO userDTO){

        User user = new User();
        user.setEmployeeId(userDTO.getEmployeeId());
        user.setEmailAddress(userDTO.getEmailAddress());
        user.setBusinessUnit(userDTO.getBusinessUnit());
        user.setDepartment(userDTO.getDepartment());
        user.setDesignation(userDTO.getDesignation());
        user.setRoles(userDTO.getRoles());

        return user;
    }
}