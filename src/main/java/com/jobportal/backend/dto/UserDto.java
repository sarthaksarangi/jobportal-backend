package com.jobportal.backend.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString

public class UserDto {
    private Long userId;
    private String name;
    private String email;
    private String mobileNo;
    private String role;
    private String companyId;
    private String companyName;
    private String createdAt;

}
