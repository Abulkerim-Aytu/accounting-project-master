package com.cydeo.service;

import com.cydeo.dto.CompanyDto;
import org.springframework.validation.BindingResult;

import java.util.List;

import java.util.List;

public interface CompanyService {
    List<CompanyDto> getCompanyDtoByLoggedInUser();

    CompanyDto findById(Long companyId);

    List<CompanyDto> getCompanyList();

    CompanyDto updateCompany(CompanyDto company);

    CompanyDto createCompany(CompanyDto newCompany);

    void activateCompany(long companyId);

    void deactivateCompany(long companyId);
    BindingResult addTitleValidation(String title, BindingResult bindingResult);

    BindingResult addUpdateTitleValidation(CompanyDto company, BindingResult bindingResult);

    CompanyDto findByCompanyTitle(String companyTitle);

//    List<String> getCounties();

}