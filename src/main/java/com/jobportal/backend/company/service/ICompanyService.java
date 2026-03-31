package com.jobportal.backend.company.service;

import com.jobportal.backend.dto.CompanyDto;

import java.util.List;

public interface ICompanyService {
    List<CompanyDto> getAllCompanies();
}
