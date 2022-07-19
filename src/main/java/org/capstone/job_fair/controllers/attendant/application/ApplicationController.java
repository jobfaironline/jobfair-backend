package org.capstone.job_fair.controllers.attendant.application;

import com.amazonaws.util.json.Jackson;
import lombok.SneakyThrows;
import org.capstone.job_fair.config.jwt.details.UserDetailsImpl;
import org.capstone.job_fair.constants.ApiEndPoint;
import org.capstone.job_fair.constants.ApplicationConstant;
import org.capstone.job_fair.constants.MessageConstant;
import org.capstone.job_fair.controllers.payload.requests.account.cv.CreateApplicationRequest;
import org.capstone.job_fair.controllers.payload.requests.attendant.EvaluateApplicationRequest;
import org.capstone.job_fair.controllers.payload.responses.GenericResponse;
import org.capstone.job_fair.models.dtos.account.AccountDTO;
import org.capstone.job_fair.models.dtos.attendant.AttendantDTO;
import org.capstone.job_fair.models.dtos.attendant.application.ApplicationDTO;
import org.capstone.job_fair.models.dtos.company.CompanyEmployeeDTO;
import org.capstone.job_fair.models.dtos.dynamoDB.NotificationMessageDTO;
import org.capstone.job_fair.models.dtos.job_fair.booth.BoothJobPositionDTO;
import org.capstone.job_fair.models.entities.attendant.application.ApplicationEntity;
import org.capstone.job_fair.models.enums.ApplicationStatus;
import org.capstone.job_fair.models.enums.NotificationAction;
import org.capstone.job_fair.models.enums.NotificationType;
import org.capstone.job_fair.models.enums.Role;
import org.capstone.job_fair.services.interfaces.attendant.application.ApplicationService;
import org.capstone.job_fair.services.interfaces.job_fair.booth.AssignmentService;
import org.capstone.job_fair.services.interfaces.matching_point.MatchingPointService;
import org.capstone.job_fair.services.interfaces.notification.NotificationService;
import org.capstone.job_fair.services.mappers.attendant.application.ApplicationMapper;
import org.capstone.job_fair.utils.MessageUtil;
import org.capstone.job_fair.validators.XSSConstraint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class ApplicationController {

    @Autowired
    private ApplicationService applicationService;

    @Autowired
    private ApplicationMapper applicationMapper;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private MatchingPointService matchingPointService;

    @Autowired
    private AssignmentService assignmentService;


    @GetMapping(ApiEndPoint.Application.APPLICATION_ENDPOINT + "/{id}")
    public ResponseEntity<?> getApplicationById(@PathVariable("id") String id) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        UserDetailsImpl user = (UserDetailsImpl) securityContext.getAuthentication().getPrincipal();
        String accountId = user.getId();
        Optional<ApplicationDTO> applicationOpt = Optional.empty();
        if (user.hasRole(Role.ATTENDANT)) {
            applicationOpt = applicationService.getApplicationByIdForAttendant(id, accountId);
        } else if (user.hasRole(Role.COMPANY_EMPLOYEE)) {
            applicationOpt = applicationService.getApplicationByIdForCompanyEmployee(id, accountId);
        }
        if (!applicationOpt.isPresent()) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Application.APPLICATION_NOT_FOUND));
        }
        return ResponseEntity.ok(applicationOpt.get());

    }

    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).ATTENDANT)")
    @PostMapping(ApiEndPoint.Application.DRAFT_APPLICATION)
    public ResponseEntity<?> draft(@Validated @RequestBody CreateApplicationRequest request) {
        //get accountId from Jwt
        SecurityContext securityContext = SecurityContextHolder.getContext();
        UserDetailsImpl user = (UserDetailsImpl) securityContext.getAuthentication().getPrincipal();
        String accountId = user.getId();

        //call applicationDTO
        ApplicationDTO dto = new ApplicationDTO();

        AttendantDTO attendantDTO = new AttendantDTO();
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setId(accountId);
        attendantDTO.setAccount(accountDTO);
        dto.setAttendant(attendantDTO);

        //call registrationJobPositionDTO + setId from request
        BoothJobPositionDTO regisDTO = new BoothJobPositionDTO();
        regisDTO.setId(request.getBoothJobPositionId());
        //set summary, create date, status, attendantDTO, registrationJobPositionDTO for ApplicationDTO
        dto.setCreateDate(new Date().getTime());
        dto.setStatus(ApplicationStatus.DRAFT);
        dto.setOriginCvId(request.getCvId());
        dto.setBoothJobPositionDTO(regisDTO);
        //call create method
        ApplicationDTO result = applicationService.createNewApplication(dto);
        matchingPointService.calculateFromApplication(result.getId()).subscribe().dispose();

        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).ATTENDANT)")
    @PostMapping(ApiEndPoint.Application.SUBMIT_APPLICATION + "/{id}")
    @SneakyThrows
    public ResponseEntity<?> submitApplication(@PathVariable("id") String applicationId) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        UserDetailsImpl user = (UserDetailsImpl) securityContext.getAuthentication().getPrincipal();
        ApplicationDTO application = applicationService.submitApplication(applicationId, user.getId());

        List<CompanyEmployeeDTO> employees = assignmentService.getAvailableInterviewer(application.getBoothJobPositionDTO().getJobFairBooth().getId());
        NotificationMessageDTO notificationMessage = NotificationMessageDTO.builder()
                .title(NotificationAction.APPLICATION.toString())
                .message(Jackson.getObjectMapper().writeValueAsString(application))
                .notificationType(NotificationType.NOTI)
                .build();
        List<String> ids = employees.stream().map(CompanyEmployeeDTO::getAccountId).collect(Collectors.toList());
        notificationService.createNotification(notificationMessage, ids);


        return ResponseEntity.ok().build();
    }


    @GetMapping(ApiEndPoint.Application.GET_APPLICATION_FOR_COMPANY_BY_CRITERIA)
    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).COMPANY_MANAGER) OR hasAuthority(T(org.capstone.job_fair.models.enums.Role).COMPANY_EMPLOYEE)")
    public ResponseEntity<?> getAllApplicationForACompanyByCriteria(@RequestParam(value = "status", required = false) List<ApplicationStatus> statusList,
                                                                    @RequestParam(value = "jobPositionName", required = false, defaultValue = ApplicationConstant.DEFAULT_JOB_POSITION_SEARCH_NAME) String jobPositionName,
                                                                    @RequestParam(value = "jobFairName", required = false, defaultValue = ApplicationConstant.DEFAULT_JOB_FAIR_SEARCH_NAME) String jobFairName,
                                                                    @RequestParam(value = "jobPositionId", required = false) String jobPositionId,
                                                                    @RequestParam(value = "jobFairId", required = false) String jobFairId,
                                                                    @RequestParam(value = "offset", defaultValue = ApplicationConstant.DEFAULT_SEARCH_OFFSET_VALUE) int offset,
                                                                    @RequestParam(value = "pageSize", defaultValue = ApplicationConstant.DEFAULT_SEARCH_PAGE_SIZE_VALUE) int pageSize,
                                                                    @RequestParam(value = "sortBy", defaultValue = ApplicationConstant.DEFAULT_SEARCH_SORT_BY_VALUE_OF_APPLICATION_FOR_COMPANY) String sortBy,
                                                                    @RequestParam(value = "direction", required = false, defaultValue = ApplicationConstant.DEFAULT_SORT_DIRECTION) Sort.Direction direction) {

        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String companyId = userDetails.getCompanyId();

        Page<ApplicationEntity> applicationForCompanyResponses = null;

        if (jobPositionId != null && jobFairId != null) {
            return GenericResponse.build(MessageUtil.getMessage(MessageConstant.Application.JOB_POSITION_ID_AND_JOBFAIR_ID_BOTH_PRESENT_ERROR), HttpStatus.BAD_REQUEST);
        }
        if (jobPositionId != null)
            applicationForCompanyResponses = applicationService.getApplicationOfCompanyByJobPositionIdAndStatus(companyId, jobPositionId, statusList, pageSize, offset, sortBy, direction);
        if (jobFairId != null)
            applicationForCompanyResponses = applicationService.getApplicationOfCompanyByJobFairIdAndStatus(companyId, jobFairId, statusList, pageSize, offset, sortBy, direction);
        if (jobFairId == null && jobPositionId == null)
            applicationForCompanyResponses = applicationService.getApplicationOfCompanyByJobFairNameAndJobPositionNameAndStatus(companyId, jobFairName, jobPositionName, statusList, pageSize, offset, sortBy, direction);

        if (applicationForCompanyResponses.isEmpty()) return ResponseEntity.noContent().build();

        return ResponseEntity.ok(applicationForCompanyResponses.map(entity -> applicationMapper.toApplicationForCompanyResponse(entity)));
    }

    @GetMapping(ApiEndPoint.Application.GET_APPLICATION_GENERAL_DATA + "/{applicationId}")
    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).COMPANY_MANAGER) OR hasAuthority(T(org.capstone.job_fair.models.enums.Role).COMPANY_EMPLOYEE)")
    public ResponseEntity<?> getApplicationWithGeneralDataByIdOfCompany(@PathVariable("applicationId") String applicationId) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String companyId = userDetails.getCompanyId();
        Optional<ApplicationEntity> applicationEntityOptional = applicationService.getApplicationWithGeneralDataByIdOfCompany(companyId, applicationId);
        if (!applicationEntityOptional.isPresent()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(applicationMapper.toApplicationWithGenralDataOfApplicantResponse(applicationEntityOptional.get()));
    }

    @PostMapping(ApiEndPoint.Application.EVALUTATE)
    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).COMPANY_MANAGER) OR hasAuthority(T(org.capstone.job_fair.models.enums.Role).COMPANY_EMPLOYEE)")
    public ResponseEntity<?> evaluateApplication(@RequestBody @Validated EvaluateApplicationRequest request) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId = userDetails.getId();

        ApplicationDTO dto = new ApplicationDTO();
        dto.setId(request.getApplicationId());
        dto.setEvaluateMessage(request.getEvaluateMessage());
        dto.setStatus(request.getStatus());

        dto = applicationService.evaluateApplication(dto, userId);

        //create notification message
        NotificationMessageDTO notificationMessageDTO = new NotificationMessageDTO();
        notificationMessageDTO.setMessage(MessageUtil.getMessage(MessageConstant.Application.EVALUATE_MESSAGE_TO_ATTENDANT));
        notificationMessageDTO.setNotificationType(NotificationType.NOTI);

        //send notification to attendant
        notificationService.createNotification(notificationMessageDTO, dto.getAttendant().getAccount().getId());

        return ResponseEntity.ok().build();
    }


    @GetMapping(ApiEndPoint.Application.APPLICATION_ENDPOINT)
    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).ATTENDANT)")
    public ResponseEntity<?> getAllApplicationOfAttendant(@RequestParam(value = "jobPositionName", required = false, defaultValue = ApplicationConstant.DEFAULT_JOB_POSITION_SEARCH_NAME) @XSSConstraint String jobPositionName,
                                                          @RequestParam(value = "jobFairName", required = false, defaultValue = ApplicationConstant.DEFAULT_JOB_FAIR_SEARCH_NAME) @XSSConstraint String jobFairName,
                                                          @RequestParam(value = "status", required = false) List<ApplicationStatus> statusList,
                                                          @RequestParam(value = "fromTime", required = false, defaultValue = ApplicationConstant.DEFAULT_SEARCH_FROM_TIME_VALUE) Long fromTime,
                                                          @RequestParam(value = "toTime", required = false, defaultValue = ApplicationConstant.DEFAULT_SEARCH_TO_TIME_VALUE) Long toTime,
                                                          @RequestParam(value = "offset", defaultValue = ApplicationConstant.DEFAULT_SEARCH_OFFSET_VALUE) int offset,
                                                          @RequestParam(value = "pageSize", defaultValue = ApplicationConstant.DEFAULT_SEARCH_PAGE_SIZE_VALUE) int pageSize,
                                                          @RequestParam(value = "sortBy", defaultValue = ApplicationConstant.DEFAULT_SEARCH_SORT_BY_VALUE) String sortBy,
                                                          @RequestParam(value = "direction", required = false, defaultValue = ApplicationConstant.DEFAULT_SORT_DIRECTION) Sort.Direction direction) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Page<ApplicationEntity> applicationEntityPage = applicationService.getAllApplicationsOfAttendantByCriteria(userDetails.getId(), jobFairName, jobPositionName, statusList, fromTime, toTime, offset, pageSize, sortBy, direction);

        if (applicationEntityPage.isEmpty()) return ResponseEntity.noContent().build();

        return ResponseEntity.ok(applicationEntityPage.map(entity -> applicationMapper.toApplicationForAttendantResponse(entity)));
    }

}
