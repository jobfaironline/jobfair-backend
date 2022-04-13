package org.capstone.job_fair.controllers.company;

import org.capstone.job_fair.constants.ApiEndPoint;
import org.capstone.job_fair.models.dtos.company.JobFairBoothDTO;
import org.capstone.job_fair.services.interfaces.company.JobFairBoothService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

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
}
