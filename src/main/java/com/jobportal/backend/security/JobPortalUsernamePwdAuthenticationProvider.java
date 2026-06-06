package com.jobportal.backend.security;

import com.jobportal.backend.entity.JobPortalUser;
import com.jobportal.backend.repository.JobPortalUserRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;


@RequiredArgsConstructor
@Component
public class JobPortalUsernamePwdAuthenticationProvider implements AuthenticationProvider {

    private final JobPortalUserRepository jobPortalUserRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public @Nullable Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        JobPortalUser jobPortalUser = jobPortalUserRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "User details now found for the user " + username
                ));
        List<SimpleGrantedAuthority> authorityList = List.of(
                new SimpleGrantedAuthority(jobPortalUser.getRole().getName()));

        if(passwordEncoder.matches(password, jobPortalUser.getPasswordHash())){
            return new UsernamePasswordAuthenticationToken(jobPortalUser,null, authorityList );

        }
        else {
            throw new BadCredentialsException("Invalid User");
        }

    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
