package org.capstone.job_fair.controllers.company;

import org.capstone.job_fair.config.jwt.details.UserDetailsImpl;
import org.capstone.job_fair.constants.ApiEndPoint;
import org.capstone.job_fair.constants.MessageConstant;
import org.capstone.job_fair.controllers.payload.requests.company.CancelCompanyJobFairRegistrationRequest;
import org.capstone.job_fair.controllers.payload.requests.company.DraftCompanyJobFairRegistrationRequest;
import org.capstone.job_fair.controllers.payload.requests.company.StaffEvaluateCompanyRegistrationRequest;
import org.capstone.job_fair.controllers.payload.requests.company.UpdateCompanyJobFairRegistrationRequest;
import org.capstone.job_fair.controllers.payload.responses.GenericResponse;
import org.capstone.job_fair.models.dtos.company.CompanyEmployeeDTO;
import org.capstone.job_fair.models.dtos.company.CompanyRegistrationDTO;
import org.capstone.job_fair.models.dtos.company.job.RegistrationJobPositionDTO;
import org.capstone.job_fair.services.interfaces.company.CompanyEmployeeService;
import org.capstone.job_fair.services.interfaces.company.CompanyRegistrationService;
import org.capstone.job_fair.utils.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class CompanyRegistrationController {

    @Autowired
    private CompanyRegistrationService companyRegistrationService;

    @Autowired
    private CompanyEmployeeService companyEmployeeService;


    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).COMPANY_EMPLOYEE) or hasAuthority(T(org.capstone.job_fair.models.enums.Role).COMPANY_MANAGER)")
    @PostMapping(ApiEndPoint.CompanyRegistration.DRAFT)
    public ResponseEntity<?> draftAJobFairRegistration(@Valid @RequestBody DraftCompanyJobFairRegistrationRequest request) {
        try {
            //Map request to company registration dto and registration job position dto
            CompanyRegistrationDTO companyRegistrationDTO = new CompanyRegistrationDTO();
            companyRegistrationDTO.setJobFairId(request.getJobFairId());
            companyRegistrationDTO.setDescription(request.getDescription());

            List<RegistrationJobPositionDTO> jobPositionDTOS = new ArrayList<>();
            for (DraftCompanyJobFairRegistrationRequest.JobPosition jobPosition : request.getJobPositions()) {
                RegistrationJobPositionDTO registrationJobPositionDTO = new RegistrationJobPositionDTO();
                registrationJobPositionDTO.setId(jobPosition.getJobPositionId());
                registrationJobPositionDTO.setDescription(jobPosition.getDescription());
                registrationJobPositionDTO.setRequirements(jobPosition.getRequirements());
                registrationJobPositionDTO.setMinSalary(jobPosition.getMinSalary());
                registrationJobPositionDTO.setMaxSalary(jobPosition.getMaxSalary());
                registrationJobPositionDTO.setNumOfPosition(jobPosition.getNumOfPosition());

                jobPositionDTOS.add(registrationJobPositionDTO);
            }
            companyRegistrationService.createDraftCompanyRegistration(companyRegistrationDTO, jobPositionDTOS);
            return GenericResponse.build(MessageUtil.getMessage(MessageConstant.CompanyRegistration.COMPANY_REGISTER_SUCCESSFULLY), HttpStatus.OK);
        } catch (IllegalArgumentException ex) {
            return GenericResponse.build(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).COMPANY_EMPLOYEE) or hasAuthority(T(org.capstone.job_fair.models.enums.Role).COMPANY_MANAGER)")
    @PutMapping(ApiEndPoint.CompanyRegistration.DRAFT)
    public ResponseEntity<?> updateAJobFairRegistrationDraft(@Valid @RequestBody UpdateCompanyJobFairRegistrationRequest request) {
        //Map request to company registration dto and registration job position dto
        CompanyRegistrationDTO companyRegistrationDTO = new CompanyRegistrationDTO();
        companyRegistrationDTO.setId(request.getJobFairRegistrationId());
        companyRegistrationDTO.setDescription(request.getDescription());

        List<RegistrationJobPositionDTO> jobPositionDTOS = new ArrayList<>();
        if (!request.getJobPositions().isEmpty()) {
            for (UpdateCompanyJobFairRegistrationRequest.JobPosition jobPosition : request.getJobPositions()) {
                RegistrationJobPositionDTO registrationJobPositionDTO = new RegistrationJobPositionDTO();
                registrationJobPositionDTO.setId(jobPosition.getJobPositionId());
                registrationJobPositionDTO.setDescription(jobPosition.getDescription());
                registrationJobPositionDTO.setRequirements(jobPosition.getRequirements());
                registrationJobPositionDTO.setMinSalary(jobPosition.getMinSalary());
                registrationJobPositionDTO.setMaxSalary(jobPosition.getMaxSalary());
                registrationJobPositionDTO.setNumOfPosition(jobPosition.getNumOfPosition());

                jobPositionDTOS.add(registrationJobPositionDTO);
            }
        }

        //Get company from user information in security context
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        companyRegistrationDTO.setCompanyId(userDetails.getCompanyId());
        companyRegistrationService.updateDraftCompanyRegistration(companyRegistrationDTO, jobPositionDTOS);
        return GenericResponse.build(MessageUtil.getMessage(MessageConstant.CompanyRegistration.UPDATE_COMPANY_REGISTRATION_DRAF_SUCCESSFULLY), HttpStatus.OK);
    }

    @GetMapping(ApiEndPoint.CompanyRegistration.SUBMIT + "/{id}")
    public ResponseEntity<?> submitJobFairRegistration(@PathVariable("id") String registrationId) {
        try {
            companyRegistrationService.submitCompanyRegistration(registrationId);
            return GenericResponse.build(MessageUtil.getMessage(MessageConstant.CompanyRegistration.SUBMIT_SUCCESSFULLY)
                    , HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return GenericResponse.build(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).COMPANY_EMPLOYEE) or hasAuthority(T(org.capstone.job_fair.models.enums.Role).COMPANY_MANAGER)")
    @PostMapping(ApiEndPoint.CompanyRegistration.CANCEL)
    public ResponseEntity<?> cancleJobFairRegistration(@Valid @RequestBody CancelCompanyJobFairRegistrationRequest request) {
        try {
            companyRegistrationService.cancelCompanyRegistration(request.getCompanyRegistrationId(), request.getCancelReason());
            return GenericResponse.build(MessageUtil.getMessage(MessageConstant.CompanyRegistration.CANCEL_SUCCESSFULLY)
                    , HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return GenericResponse.build(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).STAFF) or hasAuthority(T(org.capstone.job_fair.models.enums.Role).ADMIN)")
    @PostMapping(ApiEndPoint.CompanyRegistration.EVALUATE)
    public ResponseEntity<?> evaluateCompanyRegistration(@Valid @RequestBody StaffEvaluateCompanyRegistrationRequest request) {
        try {
            companyRegistrationService.staffEvaluateCompanyRegistration(request.getCompanyRegistrationId(), request.getStatus(), request.getMessage());
            return GenericResponse.build(MessageUtil.getMessage(MessageConstant.CompanyRegistration.EVALUATE_SUCCESSFULLY)
                    , HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return GenericResponse.build(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).COMPANY_EMPLOYEE) or hasAuthority(T(org.capstone.job_fair.models.enums.Role).COMPANY_MANAGER)")
    @GetMapping(ApiEndPoint.CompanyRegistration.GET_ALL_OWN_COMPANY_REGISTRATION + "/{jobFairId}")
    public ResponseEntity<?> getAllOwnCompanyRegistration(@PathVariable String jobFairId) {
        try {
            UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext()
                    .getAuthentication().getPrincipal();
            Optional<CompanyEmployeeDTO> companyEmployeeDTOOpt = companyEmployeeService.getCompanyEmployeeByAccountId(userDetails.getId());
            if (!companyEmployeeDTOOpt.isPresent()) {
                return GenericResponse.build(MessageUtil.getMessage(MessageConstant.CompanyEmployee.EMPLOYEE_NOT_FOUND), HttpStatus.BAD_REQUEST);
            }
            String companyID = companyEmployeeDTOOpt.get().getCompanyDTO().getId();
            List<CompanyRegistrationDTO> companyRegistrationDTOS = companyRegistrationService.getCompanyRegistrationByJobFairIDAndCompanyID(jobFairId, companyID);
            if (companyRegistrationDTOS.isEmpty()) return ResponseEntity.notFound().build();
            return new ResponseEntity<>(companyRegistrationDTOS, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return GenericResponse.build(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).ADMIN) or hasAuthority(T(org.capstone.job_fair.models.enums.Role).STAFF)")
    @GetMapping(ApiEndPoint.CompanyRegistration.GET_ALL_COMPANY_REGISTRATION_OF_JOB_FAIR + "/{jobFairId}")
    public ResponseEntity<?> getAllCompanyRegistrationOfJobFair(@PathVariable String jobFairId) {
        try {
            List<CompanyRegistrationDTO> companyRegistrationDTOS = companyRegistrationService.getCompanyRegistrationOfAJobFair(jobFairId);
            if (companyRegistrationDTOS.isEmpty()) return ResponseEntity.notFound().build();
            return new ResponseEntity<>(companyRegistrationDTOS, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return GenericResponse.build(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}
