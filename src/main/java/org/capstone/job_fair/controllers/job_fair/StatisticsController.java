package org.capstone.job_fair.controllers.job_fair;

import lombok.extern.slf4j.Slf4j;
import org.capstone.job_fair.config.jwt.details.UserDetailsImpl;
import org.capstone.job_fair.constants.ApiEndPoint;
import org.capstone.job_fair.models.dtos.company.CompanyStatisticsDTO;
import org.capstone.job_fair.models.dtos.job_fair.JobFairStatisticsDTO;
import org.capstone.job_fair.models.dtos.job_fair.booth.BoothStatisticsDTO;
import org.capstone.job_fair.services.interfaces.job_fair.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class StatisticsController {

    @Autowired
    private StatisticsService statisticsService;


    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).COMPANY_MANAGER)")
    @GetMapping(ApiEndPoint.JobFair.STATISTICS + "/{id}")
    public ResponseEntity<?> getJobFairStatistics(@PathVariable String id) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String companyId = userDetails.getCompanyId();
        JobFairStatisticsDTO result = statisticsService.getJobFairStatistics(id, companyId);
        return ResponseEntity.ok(result);


    }

    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).COMPANY_MANAGER)")
    @GetMapping(ApiEndPoint.JobFairBooth.STATISTICS + "/{id}")
    public ResponseEntity<?> getBoothStatistics(@PathVariable String id) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String companyId = userDetails.getCompanyId();
        BoothStatisticsDTO result = statisticsService.getJobFairBoothStatistics(id, companyId);
        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).COMPANY_MANAGER)")
    @GetMapping(ApiEndPoint.Company.STATISTICS)
    public ResponseEntity<?> getCompanyStatistics() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String companyId = userDetails.getCompanyId();
        CompanyStatisticsDTO result = statisticsService.getCompanyStatistics(companyId);
        return ResponseEntity.ok(result);
    }
}
