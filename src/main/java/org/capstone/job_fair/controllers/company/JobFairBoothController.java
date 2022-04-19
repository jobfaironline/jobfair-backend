package org.capstone.job_fair.controllers.company;

import org.capstone.job_fair.config.jwt.details.UserDetailsImpl;
import org.capstone.job_fair.constants.ApiEndPoint;
import org.capstone.job_fair.controllers.payload.requests.company.BoothDescriptionRequest;
import org.capstone.job_fair.models.dtos.company.JobFairBoothDTO;
import org.capstone.job_fair.models.dtos.company.job.BoothJobPositionDTO;
import org.capstone.job_fair.services.interfaces.company.JobFairBoothService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class JobFairBoothController {

    @Autowired
    private JobFairBoothService boothService;

    @GetMapping(ApiEndPoint.JobFairBooth.COMPANY_BOOTH)
    public ResponseEntity<?> getByJobFairId(
            @RequestParam(value = "jobFairId") String jobFairId
    ) {
        List<JobFairBoothDTO> jobFairBoothDTOList = boothService.getCompanyBoothByJobFairId(jobFairId);
        if (jobFairBoothDTOList.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(jobFairBoothDTOList);
    }

    @GetMapping(ApiEndPoint.JobFairBooth.COMPANY_BOOTH + "/{companyBoothId}")
    public ResponseEntity<?> getCompanyBoothById(@PathVariable("companyBoothId") String companyBoothId) {
        Optional<JobFairBoothDTO> result = boothService.getById(companyBoothId);
        if (!result.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(result.get());
    }

    @PostMapping(ApiEndPoint.JobFairBooth.COMPANY_BOOTH)
    public ResponseEntity<?> assignJobPositionToBooth(@RequestBody @Valid BoothDescriptionRequest request){
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        JobFairBoothDTO jobFairBoothDTO = new JobFairBoothDTO();
        jobFairBoothDTO.setDescription(request.getDescription());
        jobFairBoothDTO.setId(request.getBoothId());

        List<BoothJobPositionDTO> boothJobPositions = request.getJobPositions().stream().map(jobPositionRequest -> {
            BoothJobPositionDTO jobPosition = BoothJobPositionDTO
                    .builder()
                    .originJobPosition(jobPositionRequest.getId())
                    .minSalary(jobPositionRequest.getMinSalary())
                    .maxSalary(jobPositionRequest.getMaxSalary())
                    .numOfPosition(jobPositionRequest.getNumOfPosition())
                    .note(jobPositionRequest.getNote())
                    .testTimeLength(jobPositionRequest.getTestLength())
                    .numOfQuestion(jobPositionRequest.getNumOfPosition())
                    .passMark(jobPositionRequest.getPassMark())
                    .build();
            if (jobPositionRequest.getTestNumOfQuestion() != null){
                jobPosition.setIsHaveTest(true);
            }
            return jobPosition;
        }).collect(Collectors.toList());
        jobFairBoothDTO.setBoothJobPositions(boothJobPositions);
        boothService.updateJobFairBooth(jobFairBoothDTO, userDetails.getCompanyId());
        return ResponseEntity.ok().build();
    }
}
