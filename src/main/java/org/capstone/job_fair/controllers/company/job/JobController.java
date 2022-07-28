package org.capstone.job_fair.controllers.company.job;

import org.capstone.job_fair.config.jwt.details.UserDetailsImpl;
import org.capstone.job_fair.constants.ApiEndPoint;
import org.capstone.job_fair.constants.JobPositionConstant;
import org.capstone.job_fair.constants.MessageConstant;
import org.capstone.job_fair.controllers.payload.requests.company.CreateJobPositionRequest;
import org.capstone.job_fair.controllers.payload.requests.company.UpdateJobPositionRequest;
import org.capstone.job_fair.controllers.payload.responses.GenericResponse;
import org.capstone.job_fair.models.dtos.company.CompanyDTO;
import org.capstone.job_fair.models.dtos.company.CompanyEmployeeDTO;
import org.capstone.job_fair.models.dtos.company.job.JobPositionDTO;
import org.capstone.job_fair.models.dtos.util.ParseFileResult;
import org.capstone.job_fair.models.enums.JobLevel;
import org.capstone.job_fair.services.interfaces.company.CompanyEmployeeService;
import org.capstone.job_fair.services.interfaces.company.job.JobPositionService;
import org.capstone.job_fair.services.mappers.company.job.JobPositionMapper;
import org.capstone.job_fair.utils.MessageUtil;
import org.capstone.job_fair.validators.XSSConstraint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@RestController
public class JobController {

    @Autowired
    private JobPositionService jobPositionService;

    @Autowired
    private JobPositionMapper jobPositionMapper;

    @Autowired
    private CompanyEmployeeService companyEmployeeService;

    @GetMapping(ApiEndPoint.Job.JOB_POSITION_ENDPOINT + "/{jobId}")
    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).COMPANY_MANAGER) or hasAuthority(T(org.capstone.job_fair.models.enums.Role).COMPANY_EMPLOYEE) ")
    public ResponseEntity<?> getJobPositionById(@PathVariable("jobId") String jobPositionId) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String companyId = userDetails.getCompanyId();
        Optional<JobPositionDTO> jobPositionOpt = jobPositionService.getByIdAndCompanyId(jobPositionId, companyId);

        return jobPositionOpt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }


    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).COMPANY_MANAGER) or hasAuthority(T(org.capstone.job_fair.models.enums.Role).ADMIN) ")
    @PostMapping(ApiEndPoint.Job.JOB_POSITION_ENDPOINT)
    public ResponseEntity<?> createJobPosition(@Validated @RequestBody CreateJobPositionRequest request) {
        try {
            UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Optional<CompanyEmployeeDTO> companyEmployeeDTOOpt = companyEmployeeService.getCompanyEmployeeByAccountId(userDetails.getId());
            if (!companyEmployeeDTOOpt.isPresent()) {
                throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.AccessControlMessage.UNAUTHORIZED_ACTION));
            }

            JobPositionDTO jobPositionDTO = jobPositionMapper.toDTO(request);
            String companyId = userDetails.getCompanyId();
            CompanyDTO companyDTO = new CompanyDTO();
            companyDTO.setId(companyId);
            jobPositionDTO.setCompanyDTO(companyDTO);
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
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String companyId = userDetails.getCompanyId();
        jobPositionService.deleteJobPosition(jobPositionId, companyId);
        return GenericResponse.build(MessageUtil.getMessage(MessageConstant.Job.DELETE_JOB_SUCCESSFULLY), HttpStatus.OK);
    }

    @GetMapping(ApiEndPoint.Job.JOB_POSITION_ENDPOINT)
    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).COMPANY_MANAGER) or hasAuthority(T(org.capstone.job_fair.models.enums.Role).COMPANY_EMPLOYEE) ")
    public ResponseEntity<?> getAllOwnJobPositionsOfCompany(@RequestParam(value = "jobTypeId", required = false) Integer jobTypeId,
                                                            @RequestParam(value = "jobLevelId", required = false) JobLevel jobLevel,
                                                            @RequestParam(value = "offset", defaultValue = JobPositionConstant.DEFAULT_SEARCH_OFFSET_VALUE) int offset,
                                                            @RequestParam(value = "pageSize", required = false, defaultValue = JobPositionConstant.DEFAULT_SEARCH_PAGE_SIZE_VALUE) int pageSize,
                                                            @RequestParam(value = "sortBy", required = false, defaultValue = JobPositionConstant.DEFAULT_SEARCH_SORT_BY_VALUE) String sortBy,
                                                            @RequestParam(value = "direction", required = false, defaultValue = JobPositionConstant.DEFAULT_SEARCH_SORT_DIRECTION) Sort.Direction direction,
                                                            @RequestParam(value = "jobTitle", required = false) @XSSConstraint String jobTitle) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String companyId = userDetails.getCompanyId();
        Page<JobPositionDTO> jobPositions = jobPositionService.getAllJobPositionOfCompany(companyId, jobTypeId, jobLevel, jobTitle, pageSize, offset, sortBy, direction);
        return ResponseEntity.ok(jobPositions);
    }

    @PostMapping(ApiEndPoint.Job.CREAT_JOB_POSITION_UPLOAD_CSV)
    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).COMPANY_MANAGER)")
    public ResponseEntity<?> createMultipleJobPositionFromCSVFile(@RequestParam("file") MultipartFile file) throws IOException {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String companyId = userDetails.getCompanyId();

        ParseFileResult<JobPositionDTO> result = jobPositionService.createNewJobPositionsFromFile(file, companyId);
        if (!result.isHasError()) {
            return ResponseEntity.ok(result.getResult());
        } else {
            return ResponseEntity.badRequest().body(result);
        }
    }
}

