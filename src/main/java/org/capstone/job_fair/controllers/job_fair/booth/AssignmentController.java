package org.capstone.job_fair.controllers.job_fair.booth;

import org.capstone.job_fair.config.jwt.details.UserDetailsImpl;
import org.capstone.job_fair.constants.ApiEndPoint;
import org.capstone.job_fair.constants.AssignmentConstant;
import org.capstone.job_fair.controllers.payload.requests.job_fair.AssignEmployeeRequest;
import org.capstone.job_fair.controllers.payload.requests.job_fair.UnAssignEmployRequest;
import org.capstone.job_fair.controllers.payload.responses.JobFairAssignmentStatisticsResponse;
import org.capstone.job_fair.models.dtos.company.CompanyEmployeeDTO;
import org.capstone.job_fair.models.dtos.job_fair.booth.AssignmentDTO;
import org.capstone.job_fair.models.enums.AssignmentType;
import org.capstone.job_fair.services.interfaces.company.CompanyEmployeeService;
import org.capstone.job_fair.services.interfaces.job_fair.booth.JobFairBoothService;
import org.capstone.job_fair.services.interfaces.job_fair.booth.AssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
public class AssignmentController {
    @Autowired
    private AssignmentService assignmentService;

    @Autowired
    private CompanyEmployeeService companyEmployeeService;

    @Autowired
    private JobFairBoothService jobFairBoothService;



    @PostMapping(ApiEndPoint.Assignment.ASSIGN)
    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).COMPANY_MANAGER)")
    public ResponseEntity<?> assignEmployee(@Valid @RequestBody AssignEmployeeRequest request) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        AssignmentDTO dto = assignmentService.assignEmployee(request.getEmployeeId(), request.getJobFairBoothId(), request.getType(), userDetails.getCompanyId(), request.getBeginTime(), request.getEndTime());
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

    @GetMapping(ApiEndPoint.Assignment.OF_EMPLOYEE)
    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).COMPANY_EMPLOYEE)")
    public ResponseEntity<?> getAssignmentByEmployeeId(
            @RequestParam(value = "offset", defaultValue = AssignmentConstant.DEFAULT_SEARCH_OFFSET_VALUE) int offset,
            @RequestParam(value = "pageSize", defaultValue = AssignmentConstant.DEFAULT_SEARCH_PAGE_SIZE_VALUE) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AssignmentConstant.DEFAULT_SEARCH_SORT_BY_VALUE) String sortBy,
            @RequestParam(value = "direction", required = false, defaultValue = AssignmentConstant.DEFAULT_SEARCH_SORT_DIRECTION) Sort.Direction direction,
            @RequestParam(value = "type", required = false) AssignmentType type
            ) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Page<AssignmentDTO> result = assignmentService.getAssignmentByEmployeeIdAndType(userDetails.getId(), type, PageRequest.of(offset, pageSize).withSort(Sort.by(direction, sortBy)));
        return ResponseEntity.ok(result);
    }

    @GetMapping(ApiEndPoint.Assignment.ASSIGNMENT + "/{id}")
    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).COMPANY_MANAGER) OR hasAuthority(T(org.capstone.job_fair.models.enums.Role).COMPANY_EMPLOYEE)")
    public ResponseEntity<?> getAssignmentById(@PathVariable("id") String id) {
        Optional<AssignmentDTO> assignmentOpt = assignmentService.getAssignmentById(id);
        if (!assignmentOpt.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(assignmentOpt.get());
    }

}
