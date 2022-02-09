package com.aes.corebackend.controller.personnelmanagement;

import com.aes.corebackend.dto.APIResponse;
import com.aes.corebackend.dto.personnelmanagement.PersonalIdentificationInfoDTO;
import com.aes.corebackend.service.personnelmanagement.PersonalIdentificationService;
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
public class PersonalIdentificationController {

    private final PersonalIdentificationService service;

    @PostMapping(value = "/users/{userId}/identification-information")
    @PreAuthorize("hasAuthority('EMPLOYEE')")
    public ResponseEntity<?> createAttributesInfo(@Valid @RequestBody PersonalIdentificationInfoDTO idDTO, BindingResult result, @PathVariable Long userId) {
        if (result.hasErrors()) {
            return badRequest().body(prepareErrorResponse(result));
        }

        APIResponse apiResponse = service.create(idDTO, userId);
        return apiResponse.isSuccess() ? ok(apiResponse) : badRequest().body(apiResponse);
    }

    @PutMapping(value = "/users/{userId}/identification-information")
    @PreAuthorize("hasAuthority('EMPLOYEE')")
    public ResponseEntity<?> updateAttributesInfo(@Valid @RequestBody PersonalIdentificationInfoDTO idDTO, BindingResult result, @PathVariable Long userId) {
        if (result.hasErrors()) {
            return badRequest().body(prepareErrorResponse(result));
        }

        APIResponse apiResponse = service.update(idDTO, userId);
        return apiResponse.isSuccess() ? ok(apiResponse) : badRequest().body(apiResponse);
    }


    @GetMapping(value = "/users/{userId}/identification-information")
    @PreAuthorize("hasAuthority('EMPLOYEE')")
    public ResponseEntity<?> getAttributesInfo(@PathVariable Long userId) {

        APIResponse apiResponse = service.read(userId);
        return apiResponse.isSuccess() ? ok(apiResponse) : badRequest().body(apiResponse);
    }

}
