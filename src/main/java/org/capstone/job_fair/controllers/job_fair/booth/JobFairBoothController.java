package org.capstone.job_fair.controllers.job_fair.booth;

import org.capstone.job_fair.config.jwt.details.UserDetailsImpl;
import org.capstone.job_fair.constants.ApiEndPoint;
import org.capstone.job_fair.controllers.payload.requests.company.BoothDescriptionRequest;
import org.capstone.job_fair.controllers.payload.responses.JobFairBoothResponse;
import org.capstone.job_fair.models.dtos.job_fair.booth.BoothJobPositionDTO;
import org.capstone.job_fair.models.dtos.job_fair.booth.JobFairBoothDTO;
import org.capstone.job_fair.services.interfaces.job_fair.JobFairVisitService;
import org.capstone.job_fair.services.interfaces.job_fair.booth.JobFairBoothService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class JobFairBoothController {

    @Autowired
    private JobFairBoothService boothService;

    @Autowired
    private JobFairVisitService jobFairVisitService;

    @GetMapping(ApiEndPoint.JobFairBooth.JOB_FAIR_BOOTH)
    public ResponseEntity<?> getByJobFairId(
            @RequestParam(value = "jobFairId") String jobFairId
    ) {
        List<JobFairBoothDTO> jobFairBoothDTOList = boothService.getCompanyBoothByJobFairId(jobFairId);
        if (jobFairBoothDTOList.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(jobFairBoothDTOList);
    }

    @GetMapping(ApiEndPoint.JobFairBooth.JOB_FAIR_BOOTH + "/{jobFairBoothId}")
    public ResponseEntity<?> getCompanyBoothById(@PathVariable("jobFairBoothId") String jobFairBoothId) {
        Optional<JobFairBoothDTO> result = boothService.getById(jobFairBoothId);
        if (!result.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(result.map(jobFairBoothDTO -> {
            JobFairBoothResponse response = new JobFairBoothResponse(jobFairBoothDTO);
            int count = jobFairVisitService.getCurrentVisitOfJobFairBooth(jobFairBoothId);
            response.setVisitCount(count);
            response.setCompanyId(jobFairBoothDTO.getJobFair().getCompany().getId());
            return response;
        }).get());
    }

    @PostMapping(ApiEndPoint.JobFairBooth.JOB_FAIR_BOOTH)
    public ResponseEntity<?> assignJobPositionToBooth(@RequestBody @Valid BoothDescriptionRequest request) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        JobFairBoothDTO jobFairBoothDTO = new JobFairBoothDTO();
        jobFairBoothDTO.setName(request.getName());
        jobFairBoothDTO.setDescription(request.getDescription());
        jobFairBoothDTO.setId(request.getBoothId());

        List<BoothJobPositionDTO> boothJobPositions = request.getJobPositions().stream().map(jobPositionRequest -> {
            BoothJobPositionDTO jobPosition = BoothJobPositionDTO
                    .builder()
                    .originJobPosition(jobPositionRequest.getId())
                    .minSalary(jobPositionRequest.getMinSalary())
                    .maxSalary(jobPositionRequest.getMaxSalary())
                    .numOfPosition(jobPositionRequest.getNumOfPosition())
                    .isHaveTest(jobPositionRequest.getIsHaveTest())
                    .note(jobPositionRequest.getNote())
                    .testTimeLength(jobPositionRequest.getTestLength())
                    .numOfQuestion(jobPositionRequest.getTestNumOfQuestion())
                    .passMark(jobPositionRequest.getPassMark())
                    .build();
            if (jobPositionRequest.getTestNumOfQuestion() != null) {
                jobPosition.setIsHaveTest(true);
            }
            return jobPosition;
        }).collect(Collectors.toList());
        jobFairBoothDTO.setBoothJobPositions(boothJobPositions);
        boothService.updateJobFairBooth(jobFairBoothDTO, userDetails.getCompanyId());
        return ResponseEntity.ok().build();
    }
}
