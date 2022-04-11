package org.capstone.job_fair.controllers.company;

import org.capstone.job_fair.config.jwt.details.UserDetailsImpl;
import org.capstone.job_fair.constants.ApiEndPoint;
import org.capstone.job_fair.models.dtos.company.JobFairBoothDTO;
import org.capstone.job_fair.services.interfaces.company.JobFairBoothService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @GetMapping(ApiEndPoint.CompanyBooth.COMPANY_BOOTH)
    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).COMPANY_EMPLOYEE) or hasAuthority(T(org.capstone.job_fair.models.enums.Role).COMPANY_MANAGER)")
    public ResponseEntity<?> getByJobFairIdAndCompanyId(
            @RequestParam(value = "jobFairId") String jobFairId
    ) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<JobFairBoothDTO> jobFairBoothDTOList = boothService.getCompanyBoothByJobFairIdAndCompanyId(jobFairId, userDetails.getCompanyId());
        if (jobFairBoothDTOList.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(jobFairBoothDTOList);
    }

    @GetMapping(ApiEndPoint.CompanyBooth.COMPANY_BOOTH + "/{companyBoothId}")
    public ResponseEntity<?> getCompanyBoothById(@PathVariable("companyBoothId") String companyBoothId) {
        Optional<JobFairBoothDTO> result = boothService.getById(companyBoothId);
        if (!result.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(result.get());
    }
}