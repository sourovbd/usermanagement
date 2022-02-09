package com.aes.corebackend.service;

import com.aes.corebackend.dto.UserCredentialDTO;
import com.aes.corebackend.entity.User;
import com.aes.corebackend.entity.UserCredential;
import com.aes.corebackend.exception.ResourceNotFoundException;
import com.aes.corebackend.repository.UserCredentialRepository;
import com.aes.corebackend.repository.UserRepository;
import com.aes.corebackend.util.Constants;
import com.aes.corebackend.util.UserCredentialUtils;
import com.aes.corebackend.util.response.APIResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Objects;

import static com.aes.corebackend.util.response.APIResponseMessage.*;
import static com.aes.corebackend.util.response.APIResponse.getApiResponse;
import static com.aes.corebackend.util.response.AjaxResponseStatus.ERROR;
import static com.aes.corebackend.util.response.AjaxResponseStatus.SUCCESS;

@Service
@RequiredArgsConstructor
public class UserCredentialService {

    final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private final UserCredentialRepository userCredentialRepository;

    private final UserRepository userRepository;

    private final EmailSender emailSender;

    private APIResponse apiResponse = getApiResponse();

    public APIResponse save(UserCredential userCredential) {

        userCredential.setPassword(passwordEncoder.encode(userCredential.getPassword()));
        UserCredential updatedUserCredential = userCredentialRepository.save(userCredential);

        return Objects.nonNull(updatedUserCredential) ?
                apiResponse.setResponse(USER_CREDENTIAL_CREATED_SUCCESSFULLY, TRUE, updatedUserCredential, SUCCESS) :
                apiResponse.setResponse(USER_CREDENTIAL_CREATION_FAILED, TRUE, NULL, ERROR);
    }

    public APIResponse update(UserCredential userCredential) {

        UserCredential existingUserCredential = getEmployee(userCredential.getEmployeeId());
        if (Objects.nonNull(existingUserCredential)) {
            existingUserCredential.setPassword(passwordEncoder.encode(userCredential.getPassword()));
        }
        UserCredential updatedUserCredential = userCredentialRepository.save(existingUserCredential);

        return Objects.nonNull(updatedUserCredential) ?
                apiResponse.setResponse(USER_CREDENTIAL_UPDATED_SUCCESSFULLY, TRUE, updatedUserCredential, SUCCESS) :
                apiResponse.setResponse(USER_CREDENTIAL_UPDATE_FAILED, FALSE, NULL, ERROR);
    }

    public APIResponse verifyPassword(UserCredentialDTO userCredentialDTO) {

        UserCredential userCredential = userCredentialRepository.findByEmployeeId(userCredentialDTO.getEmployeeId())
                .orElseThrow(ResourceNotFoundException::new);
        return passwordEncoder.matches(userCredentialDTO.getPassword(), userCredential.getPassword()) ?
                apiResponse.setResponse(VALID_PASSWORD, TRUE, userCredential, SUCCESS) :
                apiResponse.setResponse(INVALID_PASSWORD, FALSE, NULL, ERROR);
    }

    public UserCredential getEmployee(String employeeId) {
        return userCredentialRepository.findByEmployeeId(employeeId).orElseThrow(ResourceNotFoundException::new);
    }

    public APIResponse generateAndSendTempPass(String email) {
        apiResponse.setResponse(EMPLOYEE_NOT_FOUND, FALSE, NULL,ERROR);
        User user = userRepository.findByEmailAddress(email).orElse(null);
        if (Objects.nonNull(user)) {
            String password = UserCredentialUtils.generatePassword(Constants.PASSWORD_MIN_LENGTH);

            UserCredential userCredential = user.getUserCredential();
            userCredential.setPassword(password);

            sendEmail(user, userCredential);

            userCredential.setPassword(passwordEncoder.encode(userCredential.getPassword()));
            userCredentialRepository.save(userCredential);
            apiResponse.setResponse(NEW_PASSWORD_SENT, TRUE, user, SUCCESS);
        }
        return apiResponse;
    }

    public void sendEmail(User user, UserCredential userCredential) {
        String messageBody = emailSender.buildEmailText(userCredential);
        emailSender.send(user.getEmailAddress(), messageBody);
    }
}
