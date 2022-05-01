package org.capstone.job_fair.controllers.job_fair;

import lombok.extern.slf4j.Slf4j;
import org.capstone.job_fair.config.jwt.details.UserDetailsImpl;
import org.capstone.job_fair.constants.ApiEndPoint;
import org.capstone.job_fair.services.interfaces.dynamoDB.JobFairVisitService;
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

    @PostMapping(ApiEndPoint.JobFairVisit.ENTER_JOB_FAIR)
    public ResponseEntity<?> visitJobFair(@RequestParam String jobFairId) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        jobFairVisitService.visitJobFair(userDetails.getId(), jobFairId);
        return ResponseEntity.ok().build();
    }

    @PostMapping(ApiEndPoint.JobFairVisit.LEAVE_JOB_FAIR)
    public ResponseEntity<?> leaveJobFair(@RequestParam String jobFairId){
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        jobFairVisitService.leaveJobFair(userDetails.getId(), jobFairId);
        return ResponseEntity.ok().build();

    }
}
