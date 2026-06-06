package com.jobportal.backend.repository;

import com.jobportal.backend.entity.JobPortalUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JobPortalUserRepository extends JpaRepository<JobPortalUser, Long> {
    Optional<JobPortalUser> readUserByEmailAndMobileNumber(String email, String mobileNumber);
    Optional<JobPortalUser> findByEmail(String email);

}