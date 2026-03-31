package com.jobportal.backend.company.controller;

import com.jobportal.backend.dto.CompanyDto;
import com.jobportal.backend.company.service.ICompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/companies")
@RequiredArgsConstructor

public class CompanyController {
    private final ICompanyService companyService;

//    @Autowired
//    public CompanyController(ICompanyService companyService){
//        this.companyService = companyService;
//    }

    @GetMapping(version = "1.0")
    public ResponseEntity<List<CompanyDto>> getAllCompanies(){
        List<CompanyDto> companyList = companyService.getAllCompanies();
        return ResponseEntity.ok().body(companyList);
    }
}
