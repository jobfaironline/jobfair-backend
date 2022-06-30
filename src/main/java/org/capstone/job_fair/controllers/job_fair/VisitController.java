package org.capstone.job_fair.controllers.job_fair;

import lombok.extern.slf4j.Slf4j;
import org.capstone.job_fair.config.jwt.details.UserDetailsImpl;
import org.capstone.job_fair.constants.ApiEndPoint;
import org.capstone.job_fair.models.enums.Role;
import org.capstone.job_fair.services.interfaces.job_fair.AttendantRegistrationService;
import org.capstone.job_fair.services.interfaces.job_fair.JobFairVisitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class VisitController {
    @Autowired
    private JobFairVisitService jobFairVisitService;

    @Autowired
    private AttendantRegistrationService attendantRegistrationService;

    @PostMapping(ApiEndPoint.JobFairVisit.ENTER_JOB_FAIR)
    public ResponseEntity<?> visitJobFair(@RequestParam String jobFairId) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        jobFairVisitService.visitJobFair(userDetails.getId(), jobFairId);
        if (userDetails.getAuthorities().stream().anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(Role.ATTENDANT.getAuthority()))) {
            attendantRegistrationService.visitJobFair(userDetails.getId(), jobFairId);
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping(ApiEndPoint.JobFairVisit.LEAVE_JOB_FAIR)
    public ResponseEntity<?> leaveJobFair(@RequestParam String jobFairId) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        jobFairVisitService.leaveJobFair(userDetails.getId(), jobFairId);
        return ResponseEntity.ok().build();

    }

    @PostMapping(ApiEndPoint.JobFairVisit.ENTER_BOOTH)
    public ResponseEntity<?> visitBooth(@RequestParam String jobFairBoothId) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        jobFairVisitService.visitBooth(userDetails.getId(), jobFairBoothId);
        return ResponseEntity.ok().build();
    }

    @PostMapping(ApiEndPoint.JobFairVisit.LEAVE_BOOTH)
    public ResponseEntity<?> leaveBooth(@RequestParam String jobFairBoothId) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        jobFairVisitService.leaveBooth(userDetails.getId(), jobFairBoothId);
        return ResponseEntity.ok().build();

    }
}
