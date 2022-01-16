package org.capstone.job_fair.controllers.company;

import org.capstone.job_fair.constants.ApiEndPoint;
import org.capstone.job_fair.constants.MessageConstant;
import org.capstone.job_fair.controllers.payload.requests.CreateCompanyRequest;
import org.capstone.job_fair.controllers.payload.requests.UpdateCompanyRequest;
import org.capstone.job_fair.controllers.payload.responses.GenericResponse;
import org.capstone.job_fair.models.dtos.company.BenefitDTO;
import org.capstone.job_fair.models.dtos.company.CompanyDTO;
import org.capstone.job_fair.models.dtos.company.MediaDTO;
import org.capstone.job_fair.models.dtos.company.SubCategoryDTO;
import org.capstone.job_fair.models.entities.company.CompanyEntity;
import org.capstone.job_fair.services.interfaces.company.CompanyService;
import org.capstone.job_fair.services.interfaces.company.CompanySizeService;
import org.capstone.job_fair.utils.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    @Autowired
    private CompanySizeService companySizeService;

    private boolean isEmailExisted(String email) {
        return companyService.getCountByEmail(email) != 0;
    }

    private boolean isTaxIDExisted(String taxID) {
        return companyService.getCountByTaxId(taxID) != 0;
    }


    private boolean isSizeIdValid(int id) {
        return companySizeService.getCountBySizeId(id) == 0;
    }


    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).ADMIN)")
    @GetMapping(ApiEndPoint.Company.COMPANY_ENDPOINT)
    public ResponseEntity<?> getCompanies() {
        return new ResponseEntity<>(companyService.getAllCompanies(), HttpStatus.OK);
    }

    @GetMapping(ApiEndPoint.Company.COMPANY_ENDPOINT + "/" + "{id}")
    public ResponseEntity<?> getByID(@PathVariable String id) {
        Optional<CompanyEntity> opt = companyService.getCompanyById(id);
        return opt.isPresent() ?
                new ResponseEntity<>(opt.get(), HttpStatus.OK) :
                GenericResponse.build(MessageUtil.getMessage(MessageConstant.Company.NOT_FOUND), HttpStatus.NOT_FOUND);
    }

    @PostMapping(ApiEndPoint.Company.COMPANY_ENDPOINT)
    public ResponseEntity<?> create(@Validated @RequestBody CreateCompanyRequest request) {
        if (isEmailExisted(request.getEmail())) {
            return GenericResponse.build(MessageUtil.getMessage(MessageConstant.Company.EMAIL_EXISTED), HttpStatus.BAD_REQUEST);
        }
        if (isTaxIDExisted(request.getTaxID())) {
            return GenericResponse.build(MessageUtil.getMessage(MessageConstant.Company.TAX_ID_EXISTED), HttpStatus.BAD_REQUEST);
        }

        if (isSizeIdValid(request.getSizeId())) {
            return GenericResponse.build(MessageUtil.getMessage(MessageConstant.Company.SIZE_INVALID), HttpStatus.BAD_REQUEST);
        }


        CompanyDTO dto = CompanyDTO.builder()
                .taxId(request.getTaxID())
                .name(request.getName())
                .address(request.getAddress())
                .phone(request.getPhone())
                .email(request.getEmail())
                .employeeMaxNum(request.getEmployeeMaxNum())
                .websiteUrl(request.getUrl())
                .sizeId(request.getSizeId())
                .mediaDTOS(request.getMediaUrls().stream().map(MediaDTO::new).collect(Collectors.toList()))
                .subCategoryDTOs(request.getSubCategoriesIds().stream().map(SubCategoryDTO::new).collect(Collectors.toList()))
                .benefitDTOs(request.getBenefitIds().stream().map(BenefitDTO::new).collect(Collectors.toList()))
                .build();
        companyService.createCompany(dto);
        return GenericResponse.build(MessageUtil.getMessage(MessageConstant.Company.CREATE_SUCCESSFULLY), HttpStatus.CREATED);
    }

    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).ADMIN) or hasAuthority(T(org.capstone.job_fair.models.enums.Role).COMPANY_MANAGER)")
    @PutMapping(ApiEndPoint.Company.COMPANY_ENDPOINT)
    public ResponseEntity<?> update(@Valid @RequestBody UpdateCompanyRequest request) {
        //check if email has changed and email is existed ?
        Optional<CompanyEntity> opt = companyService.getCompanyById(request.getId());
        if (opt.isPresent() && opt.get().getEmail() != request.getEmail() && isEmailExisted(request.getEmail())) {
            return GenericResponse.build(MessageUtil.getMessage(MessageConstant.Company.EMAIL_EXISTED), HttpStatus.BAD_REQUEST);
        }
        if (opt.isPresent() && opt.get().getTaxId() != request.getTaxId() && isTaxIDExisted(request.getEmail())) {
            return GenericResponse.build(MessageUtil.getMessage(MessageConstant.Company.TAX_ID_EXISTED), HttpStatus.BAD_REQUEST);
        }
        CompanyDTO dto = CompanyDTO.builder()
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
        return result != null
                ? GenericResponse.build(MessageUtil.getMessage(MessageConstant.Company.UPDATE_SUCCESSFULLY), HttpStatus.OK)
                : GenericResponse.build(MessageUtil.getMessage(MessageConstant.Company.UPDATE_FAILED), HttpStatus.NOT_FOUND);

    }

    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).ADMIN)")
    @DeleteMapping(ApiEndPoint.Company.COMPANY_ENDPOINT + "/" + "{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        Boolean result = companyService.deleteCompany(id);
        return result
                ? GenericResponse.build(MessageUtil.getMessage(MessageConstant.Company.DELETE_SUCCESSFULLY), HttpStatus.OK)
                : GenericResponse.build(MessageUtil.getMessage(MessageConstant.Company.DELETE_FAILED), HttpStatus.NOT_FOUND);
    }


}
