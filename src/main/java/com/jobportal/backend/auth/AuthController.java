package com.jobportal.backend.auth;

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
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor

public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final JobPortalUserRepository jobPortalUserRepository;
    private final RoleRepository roleRepository;

    @PostMapping(value = "/login/public", version = "1.0")
    public ResponseEntity<LoginResponseDto> apiLogin(@RequestBody LoginRequestDto loginRequestDto) {
        try {
            var resultAuthentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequestDto.username(),
                    loginRequestDto.password()));
            // Generate JWT token

            String jwtToken = jwtUtil.generateJwtToken(resultAuthentication);
            var userDto = new UserDto();
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
            return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                    "An unexpected error occurred");
        }


    }
    @PostMapping(value = "/register/public", version = "1.0")
    public ResponseEntity<String> registerUser(@RequestBody RegisterRequestDto registerRequestDto) {
        JobPortalUser jobPortalUser = new JobPortalUser();
        BeanUtils.copyProperties(registerRequestDto, jobPortalUser);
        jobPortalUser.setPasswordHash(passwordEncoder.encode(registerRequestDto.password()));
        roleRepository.findRoleById(1L).ifPresent(jobPortalUser::setRole);
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
