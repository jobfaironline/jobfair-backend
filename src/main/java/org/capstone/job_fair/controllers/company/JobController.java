package org.capstone.job_fair.controllers.company;

import org.capstone.job_fair.config.jwt.details.UserDetailsImpl;
import org.capstone.job_fair.constants.ApiEndPoint;
import org.capstone.job_fair.constants.MessageConstant;
import org.capstone.job_fair.controllers.payload.requests.company.CreateJobPositionRequest;
import org.capstone.job_fair.controllers.payload.requests.company.UpdateJobPositionRequest;
import org.capstone.job_fair.controllers.payload.responses.GenericResponse;
import org.capstone.job_fair.models.dtos.company.CompanyEmployeeDTO;
import org.capstone.job_fair.models.dtos.company.job.JobPositionDTO;
import org.capstone.job_fair.services.interfaces.company.CompanyEmployeeService;
import org.capstone.job_fair.services.interfaces.company.JobPositionService;
import org.capstone.job_fair.services.mappers.company.JobPositionMapper;
import org.capstone.job_fair.utils.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class JobController {

    @Autowired
    private JobPositionService jobPositionService;

    @Autowired
    private JobPositionMapper jobPositionMapper;

    @Autowired
    private CompanyEmployeeService companyEmployeeService;

    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).COMPANY_MANAGER) or hasAuthority(T(org.capstone.job_fair.models.enums.Role).ADMIN) ")
    @PostMapping(ApiEndPoint.Job.JOB_POSITION_ENDPOINT)
    public ResponseEntity<?> createJobPosition(@Validated @RequestBody CreateJobPositionRequest request) {
        try {
            UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Optional<CompanyEmployeeDTO> companyEmployeeDTOOpt = companyEmployeeService.getCompanyEmployeeByAccountId(userDetails.getId());
            if (!companyEmployeeDTOOpt.isPresent()) {
                throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.AccessControlMessage.UNAUTHORIZED_ACTION));
            }
            String userCompanyID = companyEmployeeDTOOpt.get().getCompanyDTO().getId();
            if (!userCompanyID.equals(request.getCompanyId())) {
                throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Job.COMPANY_MISMATCH));
            }

            JobPositionDTO jobPositionDTO = jobPositionMapper.toDTO(request);
            jobPositionService.createNewJobPosition(jobPositionDTO);
            return GenericResponse.build(MessageUtil.getMessage(MessageConstant.Job.CREATE_JOB_SUCCESSFULLY), HttpStatus.CREATED);
        } catch (IllegalArgumentException ex) {
            return GenericResponse.build(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).COMPANY_MANAGER) or hasAuthority(T(org.capstone.job_fair.models.enums.Role).COMPANY_EMPLOYEE) ")
    @PutMapping(ApiEndPoint.Job.JOB_POSITION_ENDPOINT + "/{jobId}")
    public ResponseEntity<?> updateJobPosition(@Validated @RequestBody UpdateJobPositionRequest request, @PathVariable("jobId") String jobPositionId) {
        try {
            UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String userId = userDetails.getId();
            CompanyEmployeeDTO companyEmployeeDTO = companyEmployeeService.getCompanyEmployeeByAccountId(userId).get();
            String companyId = companyEmployeeDTO.getCompanyDTO().getId();
            JobPositionDTO jobPositionDTO = jobPositionMapper.toDTO(request);
            jobPositionDTO.setId(jobPositionId);
            jobPositionDTO = jobPositionService.updateJobPosition(jobPositionDTO, companyId);
            return ResponseEntity.ok(jobPositionDTO);
        } catch (IllegalArgumentException ex) {
            return GenericResponse.build(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping(ApiEndPoint.Job.JOB_POSITION_ENDPOINT + "/{jobId}")
    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).COMPANY_MANAGER) or hasAuthority(T(org.capstone.job_fair.models.enums.Role).COMPANY_EMPLOYEE) ")
    public ResponseEntity<?> deleteJobPosition(@PathVariable("jobId") String jobPositionId) {
        try {
            UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String userId = userDetails.getId();
            CompanyEmployeeDTO companyEmployeeDTO = companyEmployeeService.getCompanyEmployeeByAccountId(userId).get();
            String companyId = companyEmployeeDTO.getCompanyDTO().getId();
            jobPositionService.deleteJobPosition(jobPositionId, companyId);
            return GenericResponse.build(MessageUtil.getMessage(MessageConstant.Job.DELETE_JOB_SUCCESSFULLY), HttpStatus.OK);
        } catch (IllegalArgumentException ex) {
            return GenericResponse.build(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
