package org.capstone.job_fair.controllers.attendant.cv;

import org.capstone.job_fair.config.jwt.details.UserDetailsImpl;
import org.capstone.job_fair.constants.ApiEndPoint;
import org.capstone.job_fair.constants.ApplicationConstant;
import org.capstone.job_fair.constants.MessageConstant;
import org.capstone.job_fair.controllers.payload.requests.account.cv.CreateApplicationRequest;
import org.capstone.job_fair.controllers.payload.responses.ApplicationForCompanyResponse;
import org.capstone.job_fair.controllers.payload.responses.GenericResponse;
import org.capstone.job_fair.models.dtos.attendant.cv.ApplicationDTO;
import org.capstone.job_fair.models.enums.Application;
import org.capstone.job_fair.services.interfaces.attendant.ApplicationService;
import org.capstone.job_fair.utils.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ApplicationController {

    @Autowired
    private ApplicationService applicationService;


    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).ATTENDANT)")
    @PostMapping(ApiEndPoint.Application.APPLICATION_ENDPOINT)
    public ResponseEntity create(@Validated @RequestBody CreateApplicationRequest request) {
        return null;
    }


    @GetMapping(ApiEndPoint.Application.APPLICATION_ENDPOINT)
    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).ATTENDANT)")
    public ResponseEntity<?> getAllApplicationsOfCompanyByCriteria(
            @RequestParam(value = "status", defaultValue = ApplicationConstant.DEFAULT_SEARCH_STATUS_VALUE) Application status,
            @RequestParam(value = "fromTime", defaultValue = ApplicationConstant.DEFAULT_SEARCH_FROM_TIME_VALUE) long fromTime,
            @RequestParam(value = "toTime", defaultValue = ApplicationConstant.DEFAULT_SEARCH_TO_TIME_VALUE) long toTime,
            @RequestParam(value = "offset", defaultValue = ApplicationConstant.DEFAULT_SEARCH_OFFSET_VALUE) int offset,
            @RequestParam(value = "pageSize", defaultValue = ApplicationConstant.DEFAULT_SEARCH_PAGE_SIZE_VALUE) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = ApplicationConstant.DEFAULT_SEARCH_SORT_BY_VALUE) String sortBy) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Page<ApplicationDTO> applicationDTOList = applicationService.getAllApplicationsOfAttendantByCriteria(userDetails.getId(), status, fromTime, toTime, offset, pageSize, sortBy);
        return ResponseEntity.ok(applicationDTOList);
    }


    @GetMapping(ApiEndPoint.Application.GET_APPLICATION_FOR_COMPANY_BY_CRITERIA)
    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).ATTENDANT)")
    public ResponseEntity<?> getAllApplicationForACompanyByCriteria(
            @RequestParam(value = "status", defaultValue = ApplicationConstant.DEFAULT_SEARCH_STATUS_VALUE) List<Application> statusList,

            @RequestParam(value = "jobPositionName", required = false, defaultValue = ApplicationConstant.DEFAULT_JOB_POSITION_SEARCH_NAME) String jobPositionName,
            @RequestParam(value = "jobFairName", required = false, defaultValue = ApplicationConstant.DEFAULT_JOB_FAIR_SEARCH_NAME) String jobFairName,

            @RequestParam(value = "jobPositionId", required = false) String jobPositionId,

            @RequestParam(value = "jobFairId", required = false) String jobFairId,

            @RequestParam(value = "offset", defaultValue = ApplicationConstant.DEFAULT_SEARCH_OFFSET_VALUE) int offset,
            @RequestParam(value = "pageSize", defaultValue = ApplicationConstant.DEFAULT_SEARCH_PAGE_SIZE_VALUE) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = ApplicationConstant.DEFAULT_SEARCH_SORT_BY_VALUE) String sortBy,
            @RequestParam(value = "direction", required = false, defaultValue = ApplicationConstant.DEFAULT_SORT_DIRECTION) Sort.Direction direction) {

        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String companyId = userDetails.getCompanyId();

        Page<ApplicationForCompanyResponse> applicationForCompanyResponses = null;

        if (jobPositionId != null && jobFairId != null) {
            return GenericResponse.build(MessageUtil.getMessage(MessageConstant.Application.JOB_POSITION_ID_AND_JOBFAIR_ID_BOTH_PRESENT_ERROR), HttpStatus.BAD_REQUEST);
        }
        if (jobPositionId != null)
            applicationForCompanyResponses = applicationService.
                    getApplicationOfCompanyByJobPositionIdAndStatus(companyId, jobPositionId, statusList, pageSize, offset, sortBy, direction);
        if (jobFairId != null)
            applicationForCompanyResponses = applicationService.
                    getApplicationOfCompanyByJobFairIdAndStatus(companyId, jobFairId, statusList, pageSize, offset);

        applicationForCompanyResponses = applicationService.
                getApplicationOfCompanyByJobFairNameAndJobPositionNameAndStatus(companyId, jobFairName, jobPositionName, statusList, pageSize, offset, sortBy, direction);

        if (applicationForCompanyResponses.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(applicationForCompanyResponses);
    }


}
