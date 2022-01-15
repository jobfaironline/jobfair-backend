package org.capstone.job_fair.controllers.company;

import org.capstone.job_fair.constants.ApiEndPoint;
import org.capstone.job_fair.controllers.payload.GenericMessageResponseEntity;
import org.capstone.job_fair.controllers.payload.CreateJobPositionRequest;
import org.capstone.job_fair.models.dtos.company.SkillTagDTO;
import org.capstone.job_fair.models.dtos.company.SubCategoryDTO;
import org.capstone.job_fair.models.dtos.company.job.JobPositionDTO;
import org.capstone.job_fair.services.interfaces.company.CompanyService;
import org.capstone.job_fair.services.interfaces.company.JobPositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

@RestController
public class JobController {
    private static final String SUCCES_CREATE_JOB_POSITION = "Successfully";
    private static final String COMPANY_NOT_EXISTED = "Company does not existed";
    private static final String SALARY_ERROR = "Max salary cannot smaller than min salary";
    @Autowired
    private JobPositionService jobPositionService;

    @Autowired
    private CompanyService companyService;

    @PreAuthorize("hasAuthority('COMPANY_MANAGER')")
    @PostMapping(ApiEndPoint.Job.JOB_POSITION_ENDPOINT)
    public ResponseEntity<?> createJobPosition(@Validated @RequestBody CreateJobPositionRequest request) {

        if (companyService.getCountById(request.getCompanyId()) == 0) {
            return GenericMessageResponseEntity.build(COMPANY_NOT_EXISTED, HttpStatus.BAD_REQUEST);
        }

        if (request.getMaxSalary() < request.getMinSalary()) {
            return GenericMessageResponseEntity.build(SALARY_ERROR, HttpStatus.BAD_REQUEST);
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
                .subCategoryDTOs(request.getSubCategoryIds().stream().map(SubCategoryDTO::new).collect(Collectors.toList()))
                .skillTagDTOS(request.getSkillTagIds().stream().map(SkillTagDTO::new).collect(Collectors.toList()))
                .build();
        jobPositionService.createNewJobPosition(jobPositionDTO);
        return GenericMessageResponseEntity.build(SUCCES_CREATE_JOB_POSITION, HttpStatus.OK);
    }
}
