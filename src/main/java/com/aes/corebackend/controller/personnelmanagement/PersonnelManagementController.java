package com.aes.corebackend.controller.personnelmanagement;

import com.aes.corebackend.dto.personnelmanagement.*;
import com.aes.corebackend.entity.User;
import com.aes.corebackend.entity.personnelmanagement.*;
import com.aes.corebackend.service.UserService;
import com.aes.corebackend.service.personnelmanagement.PersonalInformationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class PersonnelManagementController {

    @Autowired
    PersonalInformationService personalInformationService;
    @Autowired
    UserService userService;

    @PostMapping(value = "/users/{userId}/update-personal-basic-info")
    public ResponseEntity<?> updatePersonalBasicInfo(@RequestBody PersonalBasicInfoDTO basicInfoDTO, @PathVariable Long userId) {
        boolean success = false;
        String message = "Update failed";

        PersonalBasicInfo basicInfo = basicInfoDTO.getPersonalBasicInfoEntity(basicInfoDTO);
        User user = userService.getUserByUserId(userId);

        if (user != null) {
            basicInfo.setUser(user);
            success = personalInformationService.updatePersonalBasicInfo(basicInfo);
            if (success) {
                message = "Update successful";
            }
        } else {
            message = "User not found";
        }

        return ResponseEntity.ok(new PersonnelManagementResponseDTO(message, success));
    }


    @PostMapping(value = "/users/{id}/update-personal-attributes")
    public ResponseEntity<?> updatePersonalAttributes(@RequestBody PersonalAttributesDTO attributesDTO, @PathVariable String id) {
        //TODO convert dto to entity
        PersonalAttributes attributes = attributesDTO.getPersonalAttributesEntity(attributesDTO);//new PersonalAttributes();//
        boolean success = personalInformationService.updatePersonalAttributes(attributes);
        return ResponseEntity.ok(new PersonnelManagementResponseDTO("Update successfull!", success));
    }

    @PostMapping(value = "/users/{id}/update-personal-family-info")
    public ResponseEntity<?> updatePersonalFamilyInfo(@RequestBody PersonalFamilyInfoDTO familyInfoDTO, @PathVariable String id) {
        //TODO convert dto to entity
        PersonalFamilyInfo familyInfo = familyInfoDTO.getPersonalFamilyEntity(familyInfoDTO);
        boolean success = personalInformationService.updatePersonalFamilyInfo(familyInfo);
        return ResponseEntity.ok(new PersonnelManagementResponseDTO("Update successfull!", success));
    }

    @PostMapping(value = "/users/{id}/update-personal-identification-info")
    public ResponseEntity<?> updatePersonalIdentificationInfo(@RequestBody PersonalIdentificationInfoDTO identificationInfoDTO, @PathVariable String id) {
        //TODO convert dto to entity
        PersonalIdentificationInfo identificationInfo = identificationInfoDTO.getPersonalIdentificationEntity(identificationInfoDTO);
        boolean success = personalInformationService.updatePersonalIdentificationInfo(identificationInfo);
        return ResponseEntity.ok(new PersonnelManagementResponseDTO("Update successfull!", success));
    }

    @PostMapping(value = "/users/{id}/update-personal-address")
    public ResponseEntity<?> updatePersonalAddress(@RequestBody PersonalAddressInfoDTO addressInfoDTO, @PathVariable String id) {
        //TODO convert dto to entity
        PersonalAddressInfo addressInfo = addressInfoDTO.getPersonalAddressInfoEntity(addressInfoDTO);
        boolean success = personalInformationService.updatePersonalAddress(addressInfo);
        return ResponseEntity.ok(new PersonnelManagementResponseDTO("Update successfull!", success));
    }

    @PostMapping(value = "/users/{id}/update-personal-education")
    public ResponseEntity<?> updatePersonalEducation(@RequestBody PersonalEducationDTO educationDTO, @PathVariable String id) {
        //TODO convert dto to entity
        PersonalEducationInfo educationInfo = educationDTO.getPersonalEducationEntity(educationDTO);
        boolean success = personalInformationService.updatePersonalEducation(educationInfo);
        return ResponseEntity.ok(new PersonnelManagementResponseDTO("Update successfull!", success));
    }
}
