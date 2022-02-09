package com.aes.corebackend.controller.personnelmanagement;

import com.aes.corebackend.dto.APIResponse;
import com.aes.corebackend.dto.personnelmanagement.PersonalBasicInfoDTO;
import com.aes.corebackend.service.personnelmanagement.PersonalBasicInformationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.aes.corebackend.util.response.AjaxResponse.prepareErrorResponse;
import static org.springframework.http.ResponseEntity.badRequest;
import static org.springframework.http.ResponseEntity.ok;

@Controller
@RequiredArgsConstructor
public class PersonalBasicInfoController {

    private final PersonalBasicInformationService personalBasicInformationService;

    @PostMapping(value = "/users/{userId}/basic-information")
    @PreAuthorize("hasAuthority('EMPLOYEE')")
    public ResponseEntity<?> createPersonalBasicInfo(@RequestBody @Valid PersonalBasicInfoDTO personalBasicInfoDTO, BindingResult result, @PathVariable Long userId) {
        if (result.hasErrors()) {
            return badRequest().body(prepareErrorResponse(result));
        }
        APIResponse apiResponse = personalBasicInformationService.create(personalBasicInfoDTO, userId);
        return apiResponse.isSuccess() ? ok(apiResponse) : badRequest().body(apiResponse);
    }

    @PutMapping(value = "/users/{userId}/basic-information")
    @PreAuthorize("hasAuthority('EMPLOYEE')")
    public ResponseEntity<?> updatePersonalBasicInfo(@Valid @RequestBody PersonalBasicInfoDTO basicInfoDTO, BindingResult result, @PathVariable Long userId) {
        if (result.hasErrors()) {
            return badRequest().body(prepareErrorResponse(result));
        }
        APIResponse apiResponse = personalBasicInformationService.update(basicInfoDTO, userId);
        return apiResponse.isSuccess() ? ok(apiResponse) : badRequest().body(apiResponse);
    }

    @GetMapping(value = "/users/{userId}/basic-information")
    @PreAuthorize("hasAuthority('EMPLOYEE')")
    public ResponseEntity<?> getPersonalBasicInfo(@PathVariable Long userId) {
        APIResponse apiResponse = personalBasicInformationService.read(userId);
        return apiResponse.isSuccess() ? ok(apiResponse) : badRequest().body(apiResponse);
    }
}
