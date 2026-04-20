package com.jobportal.backend.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.security.autoconfigure.web.servlet.SecurityFilterProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;

import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class JobPortalSecurityConfig {

    @Qualifier("publicPaths")
    private final List<String> publicPaths;

    @Qualifier("securePaths")
    private final List<String> securePaths;
    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) {
        return http.authorizeHttpRequests((requests) -> {
                publicPaths.forEach(path -> requests.requestMatchers(path).permitAll());
                securePaths.forEach(path -> requests.requestMatchers(path).authenticated());
                })
                .formLogin(flc ->flc.disable())
                .httpBasic(withDefaults())
                .build();

    }
}
