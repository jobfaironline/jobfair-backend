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

import java.util.Optional;

@RestController
public class CompanyController {

    private static final String TAX_ID_EXIST_MSG = "Tax Id has already existed. Please choose another one.";
    private static final String NOT_FOUND_COMPANY = "Company not found with id: ";
    private static final String CREATE_COMPANY_SUCCESS = "Create company successfully.";
    private static final String UPDATE_COMPANY_SUCCESS = "Update company successfully.";
    private static final String UPDATE_COMPANY_FAIL = "Update company failed.";
    private static final String DELETE_COMPANY_SUCCESS = "Delete company sucessfully.";
    private static final String DELETE_COMPANY_FAIL= "Delete company failed.";
    private static final String SIZE_INVALID= "Chosen size is invalid";

    @Autowired
    private CompanyService companyService;

    @Autowired
    private CompanySizeRepository sizeRepository;


    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping(ApiEndPoint.Company.COMPANY_ENDPOINT)
    public ResponseEntity<?> getCompanies() {
        return new ResponseEntity<>(companyService.getAllCompanies(), HttpStatus.OK);
    }

    @GetMapping(ApiEndPoint.Company.COMPANY_ENDPOINT + "/"+ "{id}")
    public ResponseEntity<?> getByID(@PathVariable String id) {
        Optional<CompanyEntity> opt =  companyService.getCompanyById(id);
        return opt.isPresent() ?
                new ResponseEntity<>(opt.get(), HttpStatus.OK) :
             GenericMessageResponseEntity.build(NOT_FOUND_COMPANY + id, HttpStatus.NOT_FOUND);
    }

    //true : valid , false: invalid
    private boolean isSizeValid(String id) {
        return sizeRepository.findById(id).isPresent() ? true : false;
    }


    @PostMapping(ApiEndPoint.Company.COMPANY_ENDPOINT)
    public ResponseEntity<?> create(@Validated @RequestBody CreateCompanyRequest request) {
        //tax Id cannot be duplicated
        CompanyEntity company = companyService.findByTaxId(request.getTaxID().trim());
        if (company != null) {
            return GenericMessageResponseEntity.build(TAX_ID_EXIST_MSG, HttpStatus.BAD_REQUEST);
        }
        if (!isSizeValid(request.getSizeId())){
            return GenericMessageResponseEntity.build(SIZE_INVALID, HttpStatus.BAD_REQUEST);
        }
        CompanyDTO dto = mappingRequestToDTO(request);
        companyService.createCompany(dto);

        return new ResponseEntity<>(new CreateCompanyResponse(CREATE_COMPANY_SUCCESS), HttpStatus.CREATED);
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('COMPANY_MANAGER')")
    @PutMapping(ApiEndPoint.Company.COMPANY_ENDPOINT)
    public ResponseEntity<?> update(@Validated @RequestBody UpdateCompanyRequest request) {
        if (!isSizeValid(request.getSizeId())){
            return GenericMessageResponseEntity.build(SIZE_INVALID, HttpStatus.BAD_REQUEST);
        }
        CompanyDTO dto = mappingRequestToDTO(request);
        Boolean result = companyService.updateCompany(request.getId(), dto);
        return result ? new ResponseEntity<>(new UpdateCompanyResponse(UPDATE_COMPANY_SUCCESS), HttpStatus.OK)
            : new ResponseEntity<>(new UpdateCompanyResponse(UPDATE_COMPANY_FAIL), HttpStatus.BAD_REQUEST);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping(ApiEndPoint.Company.COMPANY_ENDPOINT + "/"+"{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        Boolean result = companyService.deleteCompany(id);
        return result ? new ResponseEntity<>(new UpdateCompanyResponse(DELETE_COMPANY_SUCCESS), HttpStatus.OK)
                : new ResponseEntity<>(new UpdateCompanyResponse(DELETE_COMPANY_FAIL), HttpStatus.BAD_REQUEST);
    }

    private CompanyDTO mappingRequestToDTO(CreateCompanyRequest request) {
        CompanyDTO dto = new CompanyDTO();
        dto.setTaxId(request.getTaxID().trim());
        dto.setName(request.getName().trim());
        dto.setAddress(request.getAddress().trim());
        dto.setPhone(request.getPhone().trim());
        dto.setEmail(request.getEmail().trim());
        dto.setWebsiteUrl(request.getUrl().trim());
        dto.setSizeId(request.getSizeId());

        return dto;
    }

    private CompanyDTO mappingRequestToDTO(UpdateCompanyRequest request) {
        CompanyDTO dto = new CompanyDTO();
        dto.setName(request.getName().trim());
        dto.setAddress(request.getAddress().trim());
        dto.setPhone(request.getPhone().trim());
        dto.setEmail(request.getEmail().trim());
        dto.setWebsiteUrl(request.getUrl().trim());
        dto.setSizeId(request.getSizeId());

        return dto;
    }
}
