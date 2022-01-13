package org.capstone.job_fair.controllers.company;

import org.capstone.job_fair.constants.ApiEndPoint;
import org.capstone.job_fair.controllers.payload.JobPositionRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JobController {
    @PostMapping(ApiEndPoint.Job.JOB_POSITION_ENDPOINT)
    public ResponseEntity<?> createJobPosition(@Validated @RequestBody JobPositionRequest request){
        return null;
    }
}
