package com.cydeo.service.impl;

import com.cydeo.dto.CompanyDto;
import com.cydeo.dto.UserDto;
import com.cydeo.entity.Company;
import com.cydeo.mapper.MapperUtil;
import com.cydeo.repository.CompanyRepository;
import com.cydeo.enums.CompanyStatus;

import com.cydeo.service.CompanyService;
import com.cydeo.service.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {
    private final SecurityService securityService;
    private final MapperUtil mapperUtil;
    private final CompanyRepository repository;
    @Value("${COUNTRIES_API_KEY}")
    private String countriesApiKey;

    @Override
    public CompanyDto findById(Long companyId) {
        if (companyId !=1) {
            Company company = repository.findById(companyId)
                    .orElseThrow(() -> new NoSuchElementException("Company with id: " + companyId + " Not Found "));
            return mapperUtil.convert(company, new CompanyDto());
        }
        return null;
    }

    @Override
    public List<CompanyDto> getCompanyList() {
        List<Company> companies = repository.findAllCompanyIdNot1();
        if (companies.size() !=0){
            return companies.stream()
                    .map(company -> mapperUtil.convert(company,new CompanyDto()))
                    .collect(Collectors.toList());
        }
        return null;
    }

    @Override
    public List<CompanyDto> getCompanyDtoByLoggedInUser() {
        UserDto loggedInUser = securityService.getLoggedInUser();
        if (loggedInUser.getRole().getId() == 1) {
            List<Company> companies = repository.getAllCompanyForRoot(loggedInUser.getCompany().getId());
            return companies.stream().map(company -> mapperUtil.convert(company, new CompanyDto()))
                    .collect(Collectors.toList());
        } else {
            Company company = repository.getCompanyForCurrent(loggedInUser.getCompany().getId());
            return Collections.singletonList(mapperUtil.convert(company, new CompanyDto()));
        }
    }

    public CompanyDto updateCompany (CompanyDto newCompanyDto){
        Optional<Company> oldCompany = repository.findById(newCompanyDto.getId());
        if (oldCompany.isPresent()) {
            CompanyStatus oldCompanyStatus = oldCompany.get().getCompanyStatus();
            newCompanyDto.setCompanyStatus(oldCompanyStatus);
            Company savedCompany = repository.save(mapperUtil.convert(newCompanyDto, new Company()));

            return mapperUtil.convert(savedCompany, newCompanyDto);
        }
        return null;

    }

    @Override
    public CompanyDto createCompany (CompanyDto newCompany){
        newCompany.setCompanyStatus(CompanyStatus.PASSIVE);
        Company savedCompany = repository.save(mapperUtil.convert(newCompany, new Company()));

        return mapperUtil.convert(savedCompany, new CompanyDto());
    }

    @Override
    public void activateCompany ( long companyId){
        Company companyToBeActivate = repository.findById(companyId)
                .orElseThrow(() -> new NoSuchElementException("Company with id: " + companyId + " Not Found "));
        companyToBeActivate.setCompanyStatus(CompanyStatus.ACTIVE);
        repository.save(companyToBeActivate);

    }

    @Override
    public void deactivateCompany ( long companyId){
        Company companyToBeDeactivate = repository.findById(companyId)
                .orElseThrow(() -> new NoSuchElementException("Company with id: " + companyId + " Not Found "));
        companyToBeDeactivate.setCompanyStatus(CompanyStatus.PASSIVE);
        repository.save(companyToBeDeactivate);
    }

    @Override
    public BindingResult addTitleValidation(String title, BindingResult bindingResult) {
        if (repository.existsByTitle(title)){
            bindingResult.addError(new FieldError("newCompany", "title", "This title already exists."));
        }
        return bindingResult;
    }

    @Override
    public BindingResult addUpdateTitleValidation(CompanyDto company, BindingResult bindingResult) {

        if (repository.existsByTitleAndIdNot(company.getTitle(),company.getId())){
            bindingResult.addError(new FieldError("company", "title", "This title already exists."));
        }
        return bindingResult;
    }

    @Override
    public CompanyDto findByCompanyTitle(String companyTitle) {

        Company foundCompany = repository.findByTitle(companyTitle);

        return mapperUtil.convert(foundCompany,new CompanyDto());

    }
//    @Override
//    public List<String> getCounties() {
//        ResponseEntity<List<CountryInfoDTO>> countries = countryClient.getCountries(countriesApiKey);
//        if (countries.getStatusCode().is2xxSuccessful()){
//            return countries.getBody().stream()
//                    .map(CountryInfoDTO::getName)
//                    .collect(Collectors.toList());
//        }
////        throw new CountryServiceException("Countries didn't fetched"); // add exception when we do exception handling
//        return List.of();
//
//    }
}