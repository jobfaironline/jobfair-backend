package org.capstone.job_fair.controllers.attendant.cv;

import org.capstone.job_fair.config.jwt.details.UserDetailsImpl;
import org.capstone.job_fair.constants.ApiEndPoint;
import org.capstone.job_fair.constants.ApplicationConstant;
import org.capstone.job_fair.constants.MessageConstant;
import org.capstone.job_fair.controllers.payload.requests.account.cv.CreateApplicationRequest;
import org.capstone.job_fair.controllers.payload.requests.attendant.cv.EvaluateApplicationRequest;
import org.capstone.job_fair.controllers.payload.responses.GenericResponse;
import org.capstone.job_fair.models.dtos.account.AccountDTO;
import org.capstone.job_fair.models.dtos.attendant.AttendantDTO;
import org.capstone.job_fair.models.dtos.attendant.cv.ApplicationDTO;
import org.capstone.job_fair.models.dtos.attendant.cv.CvDTO;
import org.capstone.job_fair.models.dtos.company.CompanyRegistrationDTO;
import org.capstone.job_fair.models.dtos.company.job.RegistrationJobPositionDTO;
import org.capstone.job_fair.models.entities.attendant.cv.ApplicationEntity;
import org.capstone.job_fair.models.enums.ApplicationStatus;
import org.capstone.job_fair.services.interfaces.attendant.ApplicationService;
import org.capstone.job_fair.services.mappers.attendant.cv.ApplicationMapper;
import org.capstone.job_fair.utils.MessageUtil;
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
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
public class ApplicationController {

    @Autowired
    private ApplicationService applicationService;

    @Autowired
    private ApplicationMapper applicationMapper;


    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).ATTENDANT)")
    @PostMapping(ApiEndPoint.Application.APPLICATION_ENDPOINT)
    public ResponseEntity create(@Validated @RequestBody CreateApplicationRequest request) {
        try {
            //get accountId from Jwt
            SecurityContext securityContext = SecurityContextHolder.getContext();
            UserDetailsImpl user = (UserDetailsImpl) securityContext.getAuthentication().getPrincipal();
            String accountId = user.getId();

            //call applicationDTO and cvDTO
            ApplicationDTO dto = new ApplicationDTO();
            CvDTO cvDTO = new CvDTO();

            cvDTO.setId(request.getCvId());

            AttendantDTO attendantDTO = new AttendantDTO();
            AccountDTO accountDTO = new AccountDTO();
            accountDTO.setId(accountId);

            attendantDTO.setAccount(accountDTO);
            cvDTO.setAttendant(attendantDTO);
            //call registrationJobPositionDTO + setId from request
            RegistrationJobPositionDTO regisDTO = new RegistrationJobPositionDTO();
            regisDTO.setId(request.getRegistrationJobPositionId());
            //set summary, create date, status, attendantDTO, registrationJobPositionDTO for ApplicationDTO
            dto.setSummary(request.getSummary());
            dto.setCreateDate(new Date().getTime());
            dto.setStatus(ApplicationStatus.DRAFT);
            dto.setCvDTO(cvDTO);
            dto.setRegistrationJobPositionDTO(regisDTO);
            //call create method
            ApplicationDTO result = applicationService.createNewApplication(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } catch (NoSuchElementException | IllegalArgumentException ex) {
            return GenericResponse.build(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping(ApiEndPoint.Application.APPLICATION_ENDPOINT)
    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).ATTENDANT)")
    public ResponseEntity<?> getAllApplicationsForAttendant(
            @RequestParam(value = "status", defaultValue = ApplicationConstant.DEFAULT_SEARCH_STATUS_VALUE) ApplicationStatus status,
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
    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).COMPANY_MANAGER) OR hasAuthority(T(org.capstone.job_fair.models.enums.Role).COMPANY_EMPLOYEE)")
    public ResponseEntity<?> getAllApplicationForACompanyByCriteria(
            @RequestParam(value = "status", required = false) List<ApplicationStatus> statusList,

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
            applicationForCompanyResponses = applicationService.
                    getApplicationOfCompanyByJobPositionIdAndStatus(companyId, jobPositionId, statusList, pageSize, offset, sortBy, direction);
        if (jobFairId != null)
            applicationForCompanyResponses = applicationService.
                    getApplicationOfCompanyByJobFairIdAndStatus(companyId, jobFairId, statusList, pageSize, offset, sortBy, direction);

        applicationForCompanyResponses = applicationService.
                getApplicationOfCompanyByJobFairNameAndJobPositionNameAndStatus(companyId, jobFairName, jobPositionName, statusList, pageSize, offset, sortBy, direction);

        if (applicationForCompanyResponses.isEmpty()) return ResponseEntity.noContent().build();

        return ResponseEntity.ok(applicationForCompanyResponses.
                map(entity -> applicationMapper.toApplicationForCompanyResponse(entity)));
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
    @PostMapping(ApiEndPoint.Application.EVALUTATE + "/{applicationId}")
    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).COMPANY_MANAGER) OR hasAuthority(T(org.capstone.job_fair.models.enums.Role).COMPANY_EMPLOYEE)")
    public ResponseEntity<?> evaluateApplication(@PathVariable("applicationId") String applicationId, @Validated EvaluateApplicationRequest request) {

        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ApplicationDTO dto = new ApplicationDTO();
        dto.setId(applicationId);
        AccountDTO authorizer = new AccountDTO();
        dto.setAuthorizer(authorizer);
        dto.getAuthorizer().setId(userDetails.getId());
        dto.setEvaluateMessage(request.getEvaluateMessage());
        dto.setStatus(request.getStatus());
        dto.setEvaluateDate(new Date().getTime());
        RegistrationJobPositionDTO registrationJobPositionDTO = new RegistrationJobPositionDTO();
        CompanyRegistrationDTO companyRegistrationDTO = new CompanyRegistrationDTO();
        companyRegistrationDTO.setCompanyId(userDetails.getCompanyId());
        registrationJobPositionDTO.setCompanyRegistration(companyRegistrationDTO);
        dto.setRegistrationJobPositionDTO(registrationJobPositionDTO);

        applicationService.evaluateApplication(dto);

        return ResponseEntity.ok().build();

    }


}
