package org.capstone.job_fair.controllers.job_fair;

import org.capstone.job_fair.config.jwt.details.UserDetailsImpl;
import org.capstone.job_fair.constants.ApiEndPoint;
import org.capstone.job_fair.controllers.payload.requests.job_fair.AssignEmployeeRequest;
import org.capstone.job_fair.controllers.payload.requests.job_fair.UnAssignEmployRequest;
import org.capstone.job_fair.models.dtos.job_fair.AssignmentDTO;
import org.capstone.job_fair.services.interfaces.job_fair.AssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class AssignmentController {
    @Autowired
    private AssignmentService assignmentService;

    @PostMapping(ApiEndPoint.Assignment.ASSIGN)
    public ResponseEntity<?> assignEmployee(@Valid @RequestBody AssignEmployeeRequest request) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        AssignmentDTO dto = assignmentService.assignEmployee(request.getEmployeeId(), request.getJobFairBoothId(), request.getType(), userDetails.getCompanyId());
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping(ApiEndPoint.Assignment.UNASSIGN)
    public ResponseEntity<?> unassignEmployee(@Valid @RequestBody UnAssignEmployRequest request) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        AssignmentDTO dto = assignmentService.unAssignEmployee(request.getEmployeeId(), request.getJobFairBoothId(), userDetails.getCompanyId());
        return ResponseEntity.ok(dto);
    }

    @GetMapping(ApiEndPoint.Assignment.JOB_FAIR + "/{id}")
    public ResponseEntity<?> getAssignmentByJobFair(@PathVariable("id") String id) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<AssignmentDTO> result = assignmentService.getAssignmentByJobFairId(id, userDetails.getCompanyId());
        if (result.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping(ApiEndPoint.Assignment.JOB_FAIR_BOOTH + "/{id}")
    public ResponseEntity<?> getAssignmentByJobFairBooth(@PathVariable("id") String id) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<AssignmentDTO> result = assignmentService.getAssigmentByJobFairBoothId(id, userDetails.getCompanyId());
        if (result.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(result);
    }
}
