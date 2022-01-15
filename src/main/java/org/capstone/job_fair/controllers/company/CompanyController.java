package org.capstone.job_fair.controllers.company;

import org.capstone.job_fair.constants.ApiEndPoint;
import org.capstone.job_fair.controllers.payload.*;
import org.capstone.job_fair.models.dtos.company.CompanyDTO;
import org.capstone.job_fair.models.entities.company.CompanyEntity;
import org.capstone.job_fair.models.entities.company.CompanySizeEntity;
import org.capstone.job_fair.repositories.company.CompanySizeRepository;
import org.capstone.job_fair.services.interfaces.company.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@RestController
public class CompanyController {

    private static final String TAX_ID_EXIST_MSG = "Tax Id has already existed. Please choose another one.";
    private static final String EMAIL_EXIST_MSG = "Email has already existed. Please choose another one.";
    private static final String NOT_FOUND_COMPANY = "Company not found with id: ";
    private static final String CREATE_COMPANY_SUCCESS = "Create company successfully.";
    private static final String CREATE_COMPANY_FAIL = "Fail to create company.";
    private static final String UPDATE_COMPANY_SUCCESS = "Update company successfully.";
    private static final String UPDATE_COMPANY_FAIL = "CompanyId Not Found. Update company failed.";
    private static final String DELETE_COMPANY_SUCCESS = "Delete company successfully.";
    private static final String DELETE_COMPANY_FAIL = "Delete company failed.";
    private static final String SIZE_INVALID = "Chosen size is invalid";

    @Autowired
    private CompanyService companyService;

    private boolean isEmailExisted(String email) {
        return companyService.getCountByEmail(email) != 0;
    }

    private boolean isTaxIDExisted(String taxID) {
        return companyService.getCountByTaxId(taxID) != 0;
    }


    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping(ApiEndPoint.Company.COMPANY_ENDPOINT)
    public ResponseEntity<?> getCompanies() {
        return new ResponseEntity<>(companyService.getAllCompanies(), HttpStatus.OK);
    }

    @GetMapping(ApiEndPoint.Company.COMPANY_ENDPOINT + "/" + "{id}")
    public ResponseEntity<?> getByID(@PathVariable String id) {
        Optional<CompanyEntity> opt = companyService.getCompanyById(id);
        return opt.isPresent() ?
                new ResponseEntity<>(opt.get(), HttpStatus.OK) :
                GenericMessageResponseEntity.build(NOT_FOUND_COMPANY + id, HttpStatus.NOT_FOUND);
    }


    @PostMapping(ApiEndPoint.Company.COMPANY_ENDPOINT)
    public ResponseEntity<?> create(@Validated @RequestBody CreateCompanyRequest request) {
        try {
            if (isEmailExisted(request.getEmail())) {
                return GenericMessageResponseEntity.build(EMAIL_EXIST_MSG, HttpStatus.BAD_REQUEST);
            }
            if (isTaxIDExisted(request.getTaxID())) {
                return GenericMessageResponseEntity.build(TAX_ID_EXIST_MSG, HttpStatus.BAD_REQUEST);
            }
            CompanyDTO dto = CompanyDTO.builder()
                    .id(UUID.randomUUID().toString())
                    .taxId(request.getTaxID())
                    .name(request.getName())
                    .address(request.getAddress())
                    .phone(request.getPhone())
                    .email(request.getEmail())
                    .employeeMaxNum(request.getEmployeeMaxNum())
                    .websiteUrl(request.getUrl())
                    .sizeId(request.getSizeId())
                    .build();
            CompanyEntity result = companyService.createCompany(dto);
            return result != null ? GenericMessageResponseEntity.build(CREATE_COMPANY_SUCCESS, HttpStatus.CREATED)
                    : GenericMessageResponseEntity.build(CREATE_COMPANY_FAIL, HttpStatus.NOT_FOUND);
        } catch (NoSuchElementException ex) {
            return GenericMessageResponseEntity.build(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('COMPANY_MANAGER')")
    @PutMapping(ApiEndPoint.Company.COMPANY_ENDPOINT)
    public ResponseEntity<?> update(@Valid @RequestBody UpdateCompanyRequest request) {
        try {
            if (request.getEmail() != null && isEmailExisted(request.getEmail())) {
                return GenericMessageResponseEntity.build(EMAIL_EXIST_MSG, HttpStatus.BAD_REQUEST);
            }
            if (request.getTaxId() != null && isTaxIDExisted(request.getTaxId())) {
                return GenericMessageResponseEntity.build(TAX_ID_EXIST_MSG, HttpStatus.BAD_REQUEST);
            }
            CompanyDTO dto = CompanyDTO.builder()
                    .id(request.getId())
                    .name(request.getName())
                    .address(request.getAddress())
                    .phone(request.getPhone())
                    .email(request.getEmail())
                    .employeeMaxNum(request.getEmployeeMaxNum())
                    .websiteUrl(request.getUrl())
                    .sizeId(request.getSizeId())
                    .taxId(request.getTaxId())
                    .build();
            CompanyEntity result = companyService.updateCompany(dto);
            return result != null ? GenericMessageResponseEntity.build(UPDATE_COMPANY_SUCCESS, HttpStatus.OK)
                    : GenericMessageResponseEntity.build(UPDATE_COMPANY_FAIL, HttpStatus.NOT_FOUND);
        } catch (NoSuchElementException ex) {
            return GenericMessageResponseEntity.build(ex.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping(ApiEndPoint.Company.COMPANY_ENDPOINT + "/" + "{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        Boolean result = companyService.deleteCompany(id);
        return result ? new ResponseEntity<>(new UpdateCompanyResponse(DELETE_COMPANY_SUCCESS), HttpStatus.OK)
                : new ResponseEntity<>(new UpdateCompanyResponse(DELETE_COMPANY_FAIL), HttpStatus.BAD_REQUEST);
    }


}
