package com.jobportal.backend.dto;

import java.io.Serializable;

/**
 * DTO for {@link com.jobportal.backend.entity.Contact}
 */
public record ContactRequestDto(String name, String email, String userType, String subject,
                                String message) implements Serializable {
}