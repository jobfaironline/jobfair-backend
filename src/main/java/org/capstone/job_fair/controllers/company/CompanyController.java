package org.capstone.job_fair.controllers.company;

import org.capstone.job_fair.constants.ApiEndPoint;
import org.capstone.job_fair.constants.MessageConstant;
import org.capstone.job_fair.controllers.payload.requests.CompanyJobFairRegistrationRequest;
import org.capstone.job_fair.controllers.payload.requests.CreateCompanyRequest;
import org.capstone.job_fair.controllers.payload.requests.UpdateCompanyRequest;
import org.capstone.job_fair.controllers.payload.responses.GenericResponse;
import org.capstone.job_fair.models.dtos.company.CompanyDTO;
import org.capstone.job_fair.models.dtos.company.CompanyRegistrationDTO;
import org.capstone.job_fair.models.dtos.company.job.RegistrationJobPositionDTO;
import org.capstone.job_fair.models.entities.company.CompanyEntity;
import org.capstone.job_fair.services.interfaces.company.CompanyService;
import org.capstone.job_fair.services.mappers.CompanyMapper;
import org.capstone.job_fair.utils.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    @Autowired
    private CompanyMapper companyMapper;


    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).ADMIN)")
    @GetMapping(ApiEndPoint.Company.COMPANY_ENDPOINT)
    public ResponseEntity<?> getCompanies() {
        return new ResponseEntity<>(companyService.getAllCompanies(), HttpStatus.OK);
    }

    @GetMapping(ApiEndPoint.Company.COMPANY_ENDPOINT + "/" + "{id}")
    public ResponseEntity<?> getByID(@PathVariable String id) {
        Optional<CompanyEntity> opt = companyService.getCompanyById(id);
        if (opt.isPresent()) {
            return new ResponseEntity<>(opt.get(), HttpStatus.OK);
        }
        return GenericResponse.build(MessageUtil.getMessage(MessageConstant.Company.NOT_FOUND), HttpStatus.NOT_FOUND);
    }

    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).ADMIN)")
    @DeleteMapping(ApiEndPoint.Company.COMPANY_ENDPOINT + "/" + "{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        Boolean result = companyService.deleteCompany(id);
        if (result) {
            return GenericResponse.build(MessageUtil.getMessage(MessageConstant.Company.DELETE_SUCCESSFULLY), HttpStatus.OK);
        }
        return GenericResponse.build(MessageUtil.getMessage(MessageConstant.Company.DELETE_FAILED), HttpStatus.NOT_FOUND);
    }

    @PostMapping(ApiEndPoint.Company.COMPANY_ENDPOINT)
    public ResponseEntity<?> create(@Validated @RequestBody CreateCompanyRequest request) {
        try {
            CompanyDTO dto = companyMapper.toDTO(request);
            companyService.createCompany(dto);
            return GenericResponse.build(MessageUtil.getMessage(MessageConstant.Company.CREATE_SUCCESSFULLY), HttpStatus.CREATED);
        } catch (IllegalArgumentException ex) {
            return GenericResponse.build(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).ADMIN) or hasAuthority(T(org.capstone.job_fair.models.enums.Role).COMPANY_MANAGER)")
    @PutMapping(ApiEndPoint.Company.COMPANY_ENDPOINT)
    public ResponseEntity<?> update(@Valid @RequestBody UpdateCompanyRequest request) {

        try {
            CompanyDTO dto = companyMapper.toDTO(request);
            companyService.updateCompany(dto);
            return GenericResponse.build(MessageUtil.getMessage(MessageConstant.Company.UPDATE_SUCCESSFULLY), HttpStatus.OK);
        } catch (IllegalArgumentException ex) {
            return GenericResponse.build(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).COMPANY_EMPLOYEE) or hasAuthority(T(org.capstone.job_fair.models.enums.Role).COMPANY_MANAGER)")
    @PostMapping(ApiEndPoint.JobFair.COMPANY_DRAFT_A_JOB_FAIR_REGISTRATION)
    public ResponseEntity<?> draftAJobFairRegistration(@Valid @RequestBody CompanyJobFairRegistrationRequest request) {
        try {


            //Map request to company registration dto and registration job position dto
            CompanyRegistrationDTO companyRegistrationDTO = new CompanyRegistrationDTO();
            companyRegistrationDTO.setJobFairId(request.getJobFairId());
            companyRegistrationDTO.setLocationId(request.getLocationId());
            companyRegistrationDTO.setDescription(request.getDescription());

            //Get job position entity list
            List<RegistrationJobPositionDTO> jobPositionDTOS = new ArrayList<>();
            for (CompanyJobFairRegistrationRequest.JobPosition jobPosition : request.getJobPositions()) {
                RegistrationJobPositionDTO registrationJobPositionDTO = new RegistrationJobPositionDTO();
                registrationJobPositionDTO.setId(jobPosition.getJobPositionId());
                registrationJobPositionDTO.setDescription(jobPosition.getDescription());
                registrationJobPositionDTO.setRequirements(jobPosition.getRequirements());
                registrationJobPositionDTO.setMinSalary(jobPosition.getMinSalary());
                registrationJobPositionDTO.setMaxSalary(jobPosition.getMaxSalary());
                registrationJobPositionDTO.setNumOfPosition(jobPosition.getNumOfPosition());

                jobPositionDTOS.add(registrationJobPositionDTO);
            }
            companyService.createCompanyRegistration(companyRegistrationDTO, jobPositionDTOS);
            return GenericResponse.build(MessageUtil.getMessage(MessageConstant.JobFair.COMPANY_REGISTER_SUCCESSFULLY), HttpStatus.OK);
        } catch (IllegalArgumentException ex) {
            return GenericResponse.build(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


}
