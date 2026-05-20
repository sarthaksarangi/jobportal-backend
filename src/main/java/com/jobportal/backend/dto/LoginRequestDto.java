package com.jobportal.backend.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

public record LoginRequestDto(String username, String password) {
}
