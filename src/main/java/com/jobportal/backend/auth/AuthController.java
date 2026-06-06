package com.jobportal.backend.auth;

import com.jobportal.backend.constants.ApplicationConstants;
import com.jobportal.backend.dto.LoginRequestDto;
import com.jobportal.backend.dto.LoginResponseDto;
import com.jobportal.backend.dto.RegisterRequestDto;
import com.jobportal.backend.dto.UserDto;
import com.jobportal.backend.entity.JobPortalUser;
import com.jobportal.backend.repository.JobPortalUserRepository;
import com.jobportal.backend.repository.RoleRepository;
import com.jobportal.backend.security.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.authentication.password.CompromisedPasswordDecision;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor

public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final JobPortalUserRepository jobPortalUserRepository;
    private final RoleRepository roleRepository;
    private final CompromisedPasswordChecker compromisedPasswordChecker;

    @PostMapping(value = "/login/public", version = "1.0")
    public ResponseEntity<LoginResponseDto> apiLogin(@RequestBody LoginRequestDto loginRequestDto) {
        try {
            var resultAuthentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequestDto.username(),
                    loginRequestDto.password()));
            // Generate JWT token

            String jwtToken = jwtUtil.generateJwtToken(resultAuthentication);
            var userDto = new UserDto();
            var loggedInUser = (JobPortalUser) resultAuthentication.getPrincipal();
            BeanUtils.copyProperties(loggedInUser, userDto);
            userDto.setRole(loggedInUser.getRole().getName());
            userDto.setUserId(loggedInUser.getId());
            userDto.setMobileNo(loggedInUser.getMobileNumber());

            return ResponseEntity.status(HttpStatus.OK)
                    .body(new LoginResponseDto(HttpStatus.OK.getReasonPhrase(),
                            userDto, jwtToken));
        } catch (BadCredentialsException ex) {
            return buildErrorResponse(HttpStatus.UNAUTHORIZED,
                    "Invalid username or password");
        } catch (AuthenticationException ex) {
            return buildErrorResponse(HttpStatus.UNAUTHORIZED,
                    "Authentication failed");
        } catch (Exception ex) {
            ex.printStackTrace();
            return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                    "An unexpected error occurred: " + ex.getClass().getName() + " - " + ex.getMessage());
        }


    }
    @PostMapping(value = "/register/public", version = "1.0")
    public ResponseEntity<?> registerUser(@RequestBody @Valid RegisterRequestDto registerRequestDto) {
        CompromisedPasswordDecision decision = compromisedPasswordChecker.check(registerRequestDto.password());

        if(decision.isCompromised()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("password", "Choose a strong password"));
        }

        Optional<JobPortalUser> existingUser = jobPortalUserRepository.readUserByEmailAndMobileNumber(registerRequestDto.email(), registerRequestDto.mobileNumber());
        if(existingUser.isPresent()){
            Map<String, String> errors = new HashMap<>();
            JobPortalUser jobPortalUser = existingUser.get();
            if(jobPortalUser.getEmail().equalsIgnoreCase(registerRequestDto.email())){
                errors.put("email", "Email is already registered");
            }
            if(jobPortalUser.getMobileNumber().equals(registerRequestDto.mobileNumber())){
                errors.put("mobileNumber", "Mobile number is already registered");
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }
        JobPortalUser jobPortalUser = new JobPortalUser();
        jobPortalUser.setName(registerRequestDto.name());
        jobPortalUser.setEmail(registerRequestDto.email());
        jobPortalUser.setMobileNumber(registerRequestDto.mobileNumber());
        jobPortalUser.setPasswordHash(passwordEncoder.encode(registerRequestDto.password()));
        jobPortalUser.setCreatedAt(java.time.Instant.now());
        jobPortalUser.setCreatedBy("anonymous");

        roleRepository.findRoleByName(ApplicationConstants.ROLE_JOB_SEEKER).ifPresent(jobPortalUser::setRole);
        jobPortalUserRepository.save(jobPortalUser);
        return ResponseEntity.status(HttpStatus.CREATED).body("User created Successfully!");

    }

        private ResponseEntity<LoginResponseDto> buildErrorResponse(HttpStatus status,
                                                                String message) {
        return ResponseEntity
                .status(status)
                .body(new LoginResponseDto(message, null, null));
    }
}
