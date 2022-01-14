package org.capstone.job_fair.controllers.company;

import org.capstone.job_fair.constants.ApiEndPoint;
import org.capstone.job_fair.controllers.payload.GenericMessageResponseEntity;
import org.capstone.job_fair.controllers.payload.CreateJobPositionRequest;
import org.capstone.job_fair.models.dtos.company.job.JobPositionDTO;
import org.capstone.job_fair.services.interfaces.company.JobPositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JobController {
    private final String SUCCES_CREATE_JOB_POSITION = "Successfully";
    @Autowired
    private JobPositionService jobPositionService;
    @PostMapping(ApiEndPoint.Job.JOB_POSITION_ENDPOINT)
    public ResponseEntity<?> createJobPosition(@Validated @RequestBody CreateJobPositionRequest request){
        JobPositionDTO jobPositionDTO = JobPositionDTO.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .requirements(request.getRequirements())
                .minSalary(request.getMinSalary())
                .maxSalary(request.getMaxSalary())
                .contactPersonName(request.getContactPerson())
                .contactEmail(request.getContactEmail())
                .languageId(request.getPreferredLanguageId())
                .levelId(request.getLevelId())
                .jobTypeId(request.getJobTypeId())
                .locationId(request.getLocationId())
                .comapnyId(request.getCompanyId())
                .build();
        jobPositionService.createNewJobPosition(jobPositionDTO);
        return GenericMessageResponseEntity.build(SUCCES_CREATE_JOB_POSITION, HttpStatus.OK);
    }
}
