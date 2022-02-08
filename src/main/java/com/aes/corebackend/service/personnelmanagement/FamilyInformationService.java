package com.aes.corebackend.service.personnelmanagement;

import com.aes.corebackend.dto.APIResponse;
import com.aes.corebackend.dto.personnelmanagement.PersonalAttributesDTO;
import com.aes.corebackend.dto.personnelmanagement.PersonalFamilyInfoDTO;
import com.aes.corebackend.dto.personnelmanagement.PersonnelManagementResponseDTO;
import com.aes.corebackend.entity.User;
import com.aes.corebackend.entity.personnelmanagement.PersonalAttributes;
import com.aes.corebackend.entity.personnelmanagement.PersonalFamilyInfo;
import com.aes.corebackend.repository.personnelmanagement.PersonalFamilyInfoRepository;
import com.aes.corebackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static com.aes.corebackend.util.response.PersonnelManagementAPIResponseDescription.*;

@Service
public class FamilyInformationService {
    @Autowired
    PersonalFamilyInfoRepository personalFamilyInfoRepository;
    @Autowired
    UserService userService;

///     CREATE

    public APIResponse create(PersonalFamilyInfoDTO familyInfoDTO, Long userId) {
        //Check if User Exists
        APIResponse responseDTO = new APIResponse(USER_NOT_FOUND, false, null);
        User user = userService.getUserByUserId(userId);
        if(Objects.nonNull(user)){
            //convert DTO to Entity
            if(this.create(familyInfoDTO, user)){
                responseDTO.setMessage(FAMILY_CREATE_SUCCESS);
                responseDTO.setSuccess(true);
            }else{
                responseDTO.setMessage(FAMILY_CREATE_FAIL);
            }

        }
        return responseDTO;
    }

    private boolean create(PersonalFamilyInfoDTO familyInfoDTO, User user) {
        PersonalFamilyInfo familyInfo = PersonalFamilyInfoDTO.getPersonalFamilyEntity(familyInfoDTO);
        familyInfo.setUser(user);
        try {
            personalFamilyInfoRepository.save(familyInfo);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    ///     UPDATE


    public APIResponse update(PersonalFamilyInfoDTO familyInfoDTO, Long userId) {
        //Check if User Exists
        APIResponse responseDTO = new APIResponse(USER_NOT_FOUND, false, null);
        User user = userService.getUserByUserId(userId);
        if(Objects.nonNull(user)){
            PersonalFamilyInfo currentData = personalFamilyInfoRepository.findPersonalFamilyInfoByUserId(userId);
            if(Objects.nonNull(currentData)) {//Check if record exists to update
                if (update(familyInfoDTO, currentData)) {
                    responseDTO.setMessage(FAMILY_UPDATE_SUCCESS);
                    responseDTO.setSuccess(true);
                } else {
                    responseDTO.setMessage(FAMILY_UPDATE_FAIL);
                }
            } else {
                responseDTO.setMessage(FAMILY_RECORD_NOT_FOUND);
            }
        }
        return responseDTO;
    }

    private boolean update(PersonalFamilyInfoDTO familyInfoDTO, PersonalFamilyInfo currentData) {//seems like problem in the familyInfoDTO being passed in
        PersonalFamilyInfo familyInfo = PersonalFamilyInfoDTO.updateEntityFromDTO(familyInfoDTO, currentData);
        try{
            personalFamilyInfoRepository.save(familyInfo);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    ///// READ

    public APIResponse read(Long userId) {// also not returning the right value
        APIResponse responseDTO = new APIResponse(USER_NOT_FOUND, false, null);
        User user = userService.getUserByUserId(userId);
        //if exists: convert DTO to entity and call create service
        if(Objects.nonNull(user)){
            //Get data by user
            PersonalFamilyInfo familyInfo = fetchData(userId);
            //If data is NonNull--> respond responstDTO with data
            if(Objects.nonNull(familyInfo)){
                PersonalFamilyInfoDTO familyInfoDTO = PersonalFamilyInfoDTO.getPersonalFamilyDTO(familyInfo);
                responseDTO.setMessage(FAMILY_RECORD_FOUND);
                responseDTO.setSuccess(true);
                responseDTO.setData(familyInfoDTO);
                return responseDTO;
            }else{
                responseDTO.setMessage(FAMILY_RECORD_NOT_FOUND);
                responseDTO.setSuccess(true);
                return responseDTO;
            }
        }else{
            return responseDTO;
        }
    }

    private PersonalFamilyInfo fetchData(Long userId){
        try {
            PersonalFamilyInfo familyInfo = personalFamilyInfoRepository.findPersonalFamilyInfoByUserId(userId);
            return familyInfo;
        }catch (Exception e){
            return null;
        }
    }


}
