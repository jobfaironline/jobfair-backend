package org.capstone.job_fair.controllers.job_fair;

import org.capstone.job_fair.config.jwt.details.UserDetailsImpl;
import org.capstone.job_fair.constants.ApiEndPoint;
import org.capstone.job_fair.constants.ScheduleConstant;
import org.capstone.job_fair.controllers.payload.requests.job_fair.ChangeInterviewScheduleRequest;
import org.capstone.job_fair.models.dtos.job_fair.InterviewScheduleDTO;
import org.capstone.job_fair.models.enums.Role;
import org.capstone.job_fair.services.interfaces.job_fair.InterviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;

@RestController
public class InterviewController {

    @Autowired
    private InterviewService interviewService;

    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).COMPANY_EMPLOYEE) or hasAuthority(T(org.capstone.job_fair.models.enums.Role).ATTENDANT)")
    @GetMapping(ApiEndPoint.Interview.SCHEDULE)
    public ResponseEntity<?> getInterviewSchedule(@RequestParam(defaultValue = ScheduleConstant.BEGIN_TIME) Long beginTime,
                                                  @RequestParam(defaultValue = ScheduleConstant.END_TIME) Long endTime) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<InterviewScheduleDTO> result = Collections.emptyList();
        if (userDetails.hasRole(Role.ATTENDANT)) {
            result = interviewService.getInterviewScheduleForAttendant(userDetails.getId(), beginTime, endTime);
        }
        if (userDetails.hasRole(Role.COMPANY_EMPLOYEE)) {
            result = interviewService.getInterviewScheduleForCompanyEmployee(userDetails.getId(), beginTime, endTime);

        }
        if (result.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(result);
    }


    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).COMPANY_EMPLOYEE) or hasAuthority(T(org.capstone.job_fair.models.enums.Role).ATTENDANT)")
    @PostMapping(ApiEndPoint.Interview.SCHEDULE)
    public ResponseEntity<?> requestChangeInterviewSchedule(@Valid @RequestBody ChangeInterviewScheduleRequest request) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean isAttendant = userDetails.hasRole(Role.ATTENDANT);
        interviewService.requestChangeSchedule(request.getApplicationId(), userDetails.getId(), isAttendant, request.getRequestMessage(), request.getBeginTime(), request.getEndTime());
        return ResponseEntity.ok().build();
    }


}
