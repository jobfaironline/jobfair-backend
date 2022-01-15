package org.capstone.job_fair.controllers.company;

import org.capstone.job_fair.constants.ApiEndPoint;
import org.capstone.job_fair.constants.MessageConstant;
import org.capstone.job_fair.controllers.payload.responses.GenericResponse;
import org.capstone.job_fair.controllers.payload.requests.CreateJobPositionRequest;
import org.capstone.job_fair.models.dtos.company.job.JobPositionDTO;
import org.capstone.job_fair.services.interfaces.company.CompanyService;
import org.capstone.job_fair.services.interfaces.company.JobPositionService;
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
    private CompanyService companyService;

    @PreAuthorize("hasAuthority('COMPANY_MANAGER')")
    @PostMapping(ApiEndPoint.Job.JOB_POSITION_ENDPOINT)
    public ResponseEntity<?> createJobPosition(@Validated @RequestBody CreateJobPositionRequest request) {

        if (companyService.getCountById(request.getCompanyId()) == 0) {
            return GenericResponse.build(
                    MessageUtil.getMessage(MessageConstant.Company.NOT_FOUND)
                    , HttpStatus.BAD_REQUEST);
        }

        if (request.getMaxSalary() < request.getMinSalary()) {
            return GenericResponse.build(
                    MessageUtil.getMessage(MessageConstant.Job.SALARY_ERROR),
                    HttpStatus.BAD_REQUEST);
        }

        JobPositionDTO jobPositionDTO = JobPositionDTO.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .requirements(request.getRequirements())
                .minSalary(request.getMinSalary())
                .maxSalary(request.getMaxSalary())
                .contactPersonName(request.getContactPerson())
                .contactEmail(request.getContactEmail())
                .language(request.getPreferredLanguage())
                .level(request.getLevel())
                .jobType(request.getJobType())
                .locationId(request.getLocationId())
                .comapnyId(request.getCompanyId())
                .build();
        jobPositionService.createNewJobPosition(jobPositionDTO);
        return GenericResponse.build(
                MessageUtil.getMessage(MessageConstant.Job.CREATE_JOB_SUCCESSFULLY),
                HttpStatus.OK);
    }
}
