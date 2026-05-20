package com.jobportal.backend.repository;

import com.jobportal.backend.entity.JobPortalUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobPortalUserRepository extends JpaRepository<JobPortalUser, Long> {
}