package org.capstone.job_fair.controllers.company;

import org.capstone.job_fair.constants.ApiEndPoint;
import org.capstone.job_fair.constants.MessageConstant;
import org.capstone.job_fair.controllers.payload.requests.company.CreateJobPositionRequest;
import org.capstone.job_fair.controllers.payload.responses.GenericResponse;
import org.capstone.job_fair.models.dtos.company.job.JobPositionDTO;
import org.capstone.job_fair.services.interfaces.company.CompanyService;
import org.capstone.job_fair.services.interfaces.company.JobPositionService;
import org.capstone.job_fair.services.mappers.company.JobPositionMapper;
import org.capstone.job_fair.utils.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JobController {
    private static final String SALARY_ERROR = "";
    @Autowired
    private JobPositionService jobPositionService;

    @Autowired
    private JobPositionMapper jobPositionMapper;

    @Autowired
    private CompanyService companyService;

    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).COMPANY_MANAGER) or hasAuthority(T(org.capstone.job_fair.models.enums.Role).ADMIN) ")
    @PostMapping(ApiEndPoint.Job.JOB_POSITION_ENDPOINT)
    public ResponseEntity<?> createJobPosition(@Validated @RequestBody CreateJobPositionRequest request) {
        try {
            JobPositionDTO jobPositionDTO = jobPositionMapper.toDTO(request);
            jobPositionService.createNewJobPosition(jobPositionDTO);
            return GenericResponse.build(
                    MessageUtil.getMessage(MessageConstant.Job.CREATE_JOB_SUCCESSFULLY),
                    HttpStatus.CREATED);
        } catch (IllegalArgumentException ex) {
            return GenericResponse.build(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }
}
