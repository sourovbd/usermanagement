package com.aes.corebackend.controller.usermanagement;

import com.aes.corebackend.dto.usermanagement.UserDTO;
import com.aes.corebackend.entity.usermanagement.User;
import com.aes.corebackend.service.usermanagement.UserService;
import com.aes.corebackend.util.response.APIResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.Objects;

import static com.aes.corebackend.util.response.APIResponse.prepareErrorResponse;
import static org.springframework.http.ResponseEntity.badRequest;
import static org.springframework.http.ResponseEntity.ok;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * static is used so that it only happens once
     */
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @PostMapping("/users")
    @PreAuthorize("hasAuthority('SYS_ADMIN')")
    public ResponseEntity<?> createUser(@Valid @RequestBody UserDTO userDto, BindingResult result) {
        if (result.hasErrors()) {
            return badRequest().body(prepareErrorResponse(result));
        }
        APIResponse apiResponse = userService.create(userDto);

        return apiResponse.isSuccess() ? ok(apiResponse) : badRequest().body(apiResponse);
    }

    @PutMapping("/users/{id}")
    @PreAuthorize("hasAuthority('SYS_ADMIN')")
    public ResponseEntity<?> updateUser(@RequestBody @Valid UserDTO userDto, @PathVariable long id) {
        APIResponse apiResponse = userService.update(userDto, id);
        return apiResponse.isSuccess() ? ok(apiResponse) : badRequest().body(apiResponse);
    }

    @GetMapping("/users/{id}")
    @PreAuthorize("hasAnyAuthority('SYS_ADMIN', 'EMPLOYEE')")
    public ResponseEntity<?> getUserDetails(@PathVariable int id, Authentication authentication) {

        APIResponse apiResponse = userService.read(id);
        User user = (User) apiResponse.getData();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        if (apiResponse.isSuccess()) {
            if (Objects.nonNull(user)) {
                boolean isSameUser = user.getEmployeeId().equals(userDetails.getUsername());
                boolean isSysAdmin = userDetails.getAuthorities().stream().findAny().get().toString().equals("SYS_ADMIN");
                if (!isSameUser && !isSysAdmin) {
                    apiResponse = new APIResponse();
                    apiResponse.setMessage("You don't have permission to view another users details.");
                    return badRequest().body(apiResponse);
                }
                return ok(apiResponse);
            }
        }
        return badRequest().body(apiResponse);
    }

    @GetMapping("/users")
    @PreAuthorize("hasAuthority('SYS_ADMIN')")
    public ResponseEntity<?> getAllUsers() {
        APIResponse apiResponse = userService.read();
        return apiResponse.isSuccess() ? ok(apiResponse) : badRequest().body(apiResponse);
    }
}
