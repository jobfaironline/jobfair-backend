package org.capstone.job_fair.controllers.job_fair;

import org.capstone.job_fair.config.jwt.details.UserDetailsImpl;
import org.capstone.job_fair.constants.ApiEndPoint;
import org.capstone.job_fair.controllers.payload.requests.job_fair.AssignEmployeeRequest;
import org.capstone.job_fair.controllers.payload.requests.job_fair.UnAssignEmployRequest;
import org.capstone.job_fair.controllers.payload.responses.JobFairAssignmentStatisticsResponse;
import org.capstone.job_fair.models.dtos.company.CompanyEmployeeDTO;
import org.capstone.job_fair.models.dtos.job_fair.AssignmentDTO;
import org.capstone.job_fair.services.interfaces.company.CompanyEmployeeService;
import org.capstone.job_fair.services.interfaces.company.JobFairBoothService;
import org.capstone.job_fair.services.interfaces.job_fair.AssignmentService;
import org.capstone.job_fair.services.interfaces.job_fair.JobFairService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class AssignmentController {
    @Autowired
    private AssignmentService assignmentService;

    @Autowired
    private CompanyEmployeeService companyEmployeeService;

    @Autowired
    private JobFairBoothService jobFairBoothService;


    @Autowired
    private JobFairService jobFairService;

    @PostMapping(ApiEndPoint.Assignment.ASSIGN)
    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).COMPANY_MANAGER)")
    public ResponseEntity<?> assignEmployee(@Valid @RequestBody AssignEmployeeRequest request) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        AssignmentDTO dto = assignmentService.assignEmployee(request.getEmployeeId(), request.getJobFairBoothId(), request.getType(), userDetails.getCompanyId());
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping(ApiEndPoint.Assignment.UNASSIGN)
    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).COMPANY_MANAGER)")
    public ResponseEntity<?> unassignEmployee(@Valid @RequestBody UnAssignEmployRequest request) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        AssignmentDTO dto = assignmentService.unAssignEmployee(request.getEmployeeId(), request.getJobFairBoothId(), userDetails.getCompanyId());
        return ResponseEntity.ok(dto);
    }

    @GetMapping(ApiEndPoint.Assignment.JOB_FAIR + "/{id}")
    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).COMPANY_MANAGER)")
    public ResponseEntity<?> getAssignmentByJobFair(@PathVariable("id") String id) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<AssignmentDTO> result = assignmentService.getAssignmentByJobFairId(id, userDetails.getCompanyId());
        if (result.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping(ApiEndPoint.Assignment.JOB_FAIR_BOOTH + "/{id}")
    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).COMPANY_MANAGER)")
    public ResponseEntity<?> getAssignmentByJobFairBooth(@PathVariable("id") String id) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<AssignmentDTO> result = assignmentService.getAssigmentByJobFairBoothId(id, userDetails.getCompanyId());
        if (result.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping(ApiEndPoint.Assignment.JOB_FAIR_AVAILABLE + "/{id}")
    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).COMPANY_MANAGER)")
    public ResponseEntity<?> getAvailableEmployeeByJobFair(@PathVariable("id") String jobFairId) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<CompanyEmployeeDTO> companyEmployees = assignmentService.getAvailableCompanyByJobFairId(jobFairId, userDetails.getCompanyId());
        if (companyEmployees.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(companyEmployees);
    }

    @GetMapping(ApiEndPoint.Assignment.JOB_FAIR_STATISTICS + "/{id}")
    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).COMPANY_MANAGER)")
    public ResponseEntity<?> getAssignmentStatistics(@PathVariable("id") String jobFairId) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        int boothTotal = jobFairBoothService.getBoothCountByJobFair(jobFairId);
        int assignedBoothNum = assignmentService.getCountAssignedBoothByJobFair(jobFairId);
        int employeeTotal = companyEmployeeService.getCompanyEmployeeCount(userDetails.getCompanyId());
        int assignedEmployeeNum = assignmentService.getCountAssignedEmployeeByJobFair(jobFairId);
        JobFairAssignmentStatisticsResponse response = JobFairAssignmentStatisticsResponse
                .builder()
                .assignedBoothNum(assignedBoothNum)
                .assignedEmployeeNum(assignedEmployeeNum)
                .employeeTotal(employeeTotal)
                .boothTotal(boothTotal)
                .build();
        return ResponseEntity.ok(response);
    }
}
