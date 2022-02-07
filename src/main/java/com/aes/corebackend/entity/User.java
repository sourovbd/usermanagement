package com.aes.corebackend.entity;

import lombok.AllArgsConstructor;
import com.aes.corebackend.entity.personnelmanagement.PersonalAddressInfo;
import com.aes.corebackend.entity.personnelmanagement.PersonalBasicInfo;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @Column(name = "emailAddress", unique = true)
    @Pattern(regexp = "[a-zA-Z0-9.]*[@][a-zA-Z]+\\.(com|net|org)", message = "email id is invalid")
    @NotBlank(message = "email is mandatory")
    private String emailAddress;

    //@NotBlank(message = "designation id is mandatory")
    @Column(name = "designation")
    private String designation;

    @NotBlank(message = "employee id is mandatory")
    @Column(name = "employeeId", unique = true)
    private String employeeId;

    @NotBlank(message = "business unit is mandatory")
    @Column(name = "businessUnit")
    private String businessUnit;

    @NotBlank(message = "department is mandatory")
    @Column(name = "department")
    private String department;
    @Column(name = "roles")
    private String roles;

    @OneToOne(cascade = CascadeType.ALL)
    private UserCredential userCredential;

    /** mappedBy value points to the relationship owner */
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "user")
    private PersonalBasicInfo basicInfo;

    /** mappedBy value points to the relationship owner */
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "user")
    private PersonalAddressInfo address;
}
