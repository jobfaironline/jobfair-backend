package org.capstone.job_fair.controllers.job_fair;

import org.capstone.job_fair.config.jwt.details.UserDetailsImpl;
import org.capstone.job_fair.constants.ApiEndPoint;
import org.capstone.job_fair.constants.ApplicationConstant;
import org.capstone.job_fair.constants.JobFairConstant;
import org.capstone.job_fair.constants.MessageConstant;
import org.capstone.job_fair.controllers.payload.requests.job_fair.AdminEvaluateJobFairRequest;
import org.capstone.job_fair.controllers.payload.requests.job_fair.CancelJobFairRequest;
import org.capstone.job_fair.controllers.payload.requests.job_fair.DraftJobFairPlanRequest;
import org.capstone.job_fair.controllers.payload.requests.job_fair.UpdateJobFairPlanDraftRequest;
import org.capstone.job_fair.controllers.payload.responses.GenericResponse;
import org.capstone.job_fair.controllers.payload.responses.JobFairForCompanyResponse;
import org.capstone.job_fair.controllers.payload.responses.RenderJobFairParkResponse;
import org.capstone.job_fair.models.dtos.company.CompanyBoothDTO;
import org.capstone.job_fair.models.dtos.company.CompanyBoothLayoutDTO;
import org.capstone.job_fair.models.dtos.job_fair.AdminJobFairStatusDTO;
import org.capstone.job_fair.models.dtos.job_fair.AttendantJobFairStatusDTO;
import org.capstone.job_fair.models.dtos.job_fair.JobFairDTO;
import org.capstone.job_fair.models.dtos.job_fair.LayoutDTO;
import org.capstone.job_fair.models.statuses.JobFairAdminStatus;
import org.capstone.job_fair.models.statuses.JobFairAttendantStatus;
import org.capstone.job_fair.models.statuses.JobFairCompanyStatus;
import org.capstone.job_fair.models.statuses.JobFairPlanStatus;
import org.capstone.job_fair.services.interfaces.company.CompanyBoothLayoutService;
import org.capstone.job_fair.services.interfaces.company.CompanyBoothService;
import org.capstone.job_fair.services.interfaces.job_fair.JobFairService;
import org.capstone.job_fair.services.interfaces.job_fair.LayoutService;
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

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
public class JobFairController {

    @Autowired
    private JobFairService jobFairService;

    @Autowired
    private CompanyBoothService companyBoothService;

    @Autowired
    private CompanyBoothLayoutService companyBoothLayoutService;

    @Autowired
    private LayoutService layoutService;

    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).ADMIN) or hasAuthority(T(org.capstone.job_fair.models.enums.Role).STAFF)")
    @PostMapping(ApiEndPoint.JobFair.JOB_FAIR_PLAN)
    public ResponseEntity<?> draftJobFairPlan(@Validated @RequestBody DraftJobFairPlanRequest request) {
        try {
            JobFairDTO dto = JobFairDTO.builder()
                    .attendantRegisterStartTime(request.getAttendantRegisterStartTime())
                    .companyBuyBoothEndTime(request.getCompanyBuyBoothEndTime())
                    .companyBuyBoothStartTime(request.getCompanyBuyBoothStartTime())
                    .companyRegisterEndTime(request.getCompanyRegisterEndTime())
                    .companyRegisterStartTime(request.getCompanyRegisterStartTime())
                    .description(request.getDescription())
                    .endTime(request.getEndTime())
                    .startTime(request.getStartTime())
                    .layoutId(request.getLayoutId())
                    .thumbnail(request.getThumbnail())
                    .name(request.getName())
                    .estimateParticipant(request.getEstimateParticipant())
                    .targetAttendant(request.getTargetAttendant())
                    .targetCompany(request.getTargetCompany())
                    .build();
            jobFairService.draftJobFair(dto);

            return GenericResponse.build(MessageUtil.getMessage(MessageConstant.JobFair.CREATE_JOB_FAIR_PLAN_SUCCESSFULLY)
                    , HttpStatus.OK);
        } catch (IllegalArgumentException ex) {
            return GenericResponse.build(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).ADMIN) or hasAuthority(T(org.capstone.job_fair.models.enums.Role).STAFF)")
    @PutMapping(ApiEndPoint.JobFair.JOB_FAIR_PLAN)
    public ResponseEntity<?> updateJobFairPlanDraft(@Validated @RequestBody UpdateJobFairPlanDraftRequest request) {
        try {
            JobFairDTO dto = JobFairDTO.builder()
                    .id(request.getJobFairID())
                    .attendantRegisterStartTime(request.getAttendantRegisterStartTime())
                    .companyBuyBoothEndTime(request.getCompanyBuyBoothEndTime())
                    .companyBuyBoothStartTime(request.getCompanyBuyBoothStartTime())
                    .companyRegisterEndTime(request.getCompanyRegisterEndTime())
                    .companyRegisterStartTime(request.getCompanyRegisterStartTime())
                    .description(request.getDescription())
                    .endTime(request.getEndTime())
                    .startTime(request.getStartTime())
                    .layoutId(request.getLayoutId())
                    .thumbnail(request.getThumbnail())
                    .name(request.getName())
                    .estimateParticipant(request.getEstimateParticipant())
                    .targetAttendant(request.getTargetAttendant())
                    .targetCompany(request.getTargetCompany())
                    .build();
            jobFairService.updateJobFairDraft(dto);

            return GenericResponse.build(MessageUtil.getMessage(MessageConstant.JobFair.UPDATE_JOB_FAIR_PLAN_SUCCESSFULLY)
                    , HttpStatus.OK);
        } catch (IllegalArgumentException ex) {
            return GenericResponse.build(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).ADMIN) or hasAuthority(T(org.capstone.job_fair.models.enums.Role).STAFF)")
    @GetMapping(ApiEndPoint.JobFair.GET_OWN_PLAN)
    public ResponseEntity<?> getAllJobFairPlanOfEmployee(
            @RequestParam(value = "offset", defaultValue = JobFairConstant.DEFAULT_SEARCH_OFFSET_VALUE) int offset,
            @RequestParam(value = "pageSize", defaultValue = JobFairConstant.DEFAULT_SEARCH_PAGE_SIZE_VALUE) int pageSize,
            @RequestParam(value = "status", required = false) JobFairPlanStatus status,
            @RequestParam(value = "sortBy", defaultValue = JobFairConstant.DEFAULT_SEARCH_SORT_BY_VALUE) String sortBy,
            @RequestParam(value = "direction", defaultValue = JobFairConstant.DEFAULT_SEARCH_SORT_DIRECTION) Sort.Direction direction
    ) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Page<JobFairDTO> result = jobFairService.getJobFairPlanByCreatorIdAndStatus(userDetails.getId(), status, offset, pageSize, sortBy, direction);
        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).ADMIN) or hasAuthority(T(org.capstone.job_fair.models.enums.Role).STAFF)")
    @GetMapping(ApiEndPoint.JobFair.DELETE_JOB_FAIR_PLAN_DRAFT + "/{jobfairId}")
    public ResponseEntity<?> deleteJobFairPlanDraft(@PathVariable("jobfairId") String jobFairId) {
        try {
            jobFairService.deleteJobFairDraft(jobFairId);
            return GenericResponse.build(MessageUtil.getMessage(MessageConstant.JobFair.DELETE_JOB_FAIR_PLAN_SUCCESSFULLY), HttpStatus.OK);
        } catch (IllegalArgumentException ex) {
            return GenericResponse.build(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).ADMIN) or hasAuthority(T(org.capstone.job_fair.models.enums.Role).STAFF)")
    @GetMapping(ApiEndPoint.JobFair.SUBMIT_JOB_FAIR_PLAN_DRAFT + "/{jobfairId}")
    public ResponseEntity<?> submitJobFairPlanDraft(@PathVariable("jobfairId") String jobFairId) {
        try {
            jobFairService.submitJobFairDraft(jobFairId);
            return GenericResponse.build(MessageUtil.getMessage(MessageConstant.JobFair.SUBMIT_JOB_FAIR_PLAN_SUCCESSFULLY), HttpStatus.OK);
        } catch (IllegalArgumentException ex) {
            return GenericResponse.build(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).ADMIN) or hasAuthority(T(org.capstone.job_fair.models.enums.Role).STAFF)")
    @PostMapping(ApiEndPoint.JobFair.CANCEL_PENDING_JOB_FAIR_PLAN)
    public ResponseEntity<?> cancelJobFairPlanPending(@RequestBody @Valid CancelJobFairRequest request) {
        try {
            jobFairService.cancelPendingJobFair(request.getId(), request.getMessage());
            return GenericResponse.build(MessageUtil.getMessage(MessageConstant.JobFair.CANCEL_JOB_FAIR_PLAN_SUCCESSFULLY), HttpStatus.OK);
        } catch (IllegalArgumentException ex) {
            return GenericResponse.build(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).ADMIN) or hasAuthority(T(org.capstone.job_fair.models.enums.Role).STAFF)")
    @GetMapping(ApiEndPoint.JobFair.RESTORE_DELETED_JOB_FAIR_PLAN + "/{jobfairId}")
    public ResponseEntity<?> restoreDeletedJobFairPlan(@PathVariable("jobfairId") String jobFairId) {
        try {
            jobFairService.restoreDeletedJobFair(jobFairId);
            return GenericResponse.build(MessageUtil.getMessage(MessageConstant.JobFair.CANCEL_JOB_FAIR_PLAN_SUCCESSFULLY), HttpStatus.OK);
        } catch (IllegalArgumentException ex) {
            return GenericResponse.build(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).ADMIN)")
    @GetMapping(ApiEndPoint.JobFair.JOB_FAIR_PLAN)
    public ResponseEntity<?> getAllJobFairPlan(@RequestParam(value = "status", required = false) JobFairPlanStatus status,
                                               @RequestParam(value = "offset", defaultValue = JobFairConstant.DEFAULT_SEARCH_OFFSET_VALUE) int offset,
                                               @RequestParam(value = "pageSize", defaultValue = JobFairConstant.DEFAULT_SEARCH_PAGE_SIZE_VALUE) int pageSize,
                                               @RequestParam(value = "sortBy", required = false, defaultValue = JobFairConstant.DEFAULT_SEARCH_SORT_BY_VALUE) String sortBy,
                                               @RequestParam(value = "direction", required = false, defaultValue = JobFairConstant.DEFAULT_SEARCH_SORT_DIRECTION) Sort.Direction direction) {
        Page<JobFairDTO> result = jobFairService.getAllForAdmin(status, offset, pageSize, sortBy, direction);
        if (result.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping(ApiEndPoint.JobFair.JOB_FAIR_PLAN + "/{id}")
    public ResponseEntity<?> getJobFairPlanById(@PathVariable("id") String id) {
        Optional<JobFairDTO> jobFairDTOOptional = jobFairService.getJobFairByID(id);
        if (!jobFairDTOOptional.isPresent()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(jobFairDTOOptional);
    }

    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).ADMIN)")
    @PostMapping(ApiEndPoint.JobFair.EVALUATE_JOB_FAIL_PLAN)
    public ResponseEntity<?> evaluate(@RequestBody @Valid AdminEvaluateJobFairRequest request) {
        try {
            jobFairService.adminEvaluateJobFair(request.getJobFairId(), request.getStatus(), request.getMessage());
            return GenericResponse.build(MessageUtil.getMessage(MessageConstant.JobFair.EVALUATE_SUCCESSFULLY), HttpStatus.OK);
        } catch (IllegalArgumentException ex) {
            return GenericResponse.build(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).COMPANY_MANAGER) or hasAuthority(T(org.capstone.job_fair.models.enums.Role).COMPANY_EMPLOYEE)")
    @GetMapping(ApiEndPoint.JobFair.GET_APPROVE_JOB_FAIR_PLAN)
    public ResponseEntity<?> getAllApprovedJobFair() {
        try {
            List<JobFairDTO> jobFairDTOS = jobFairService.getAllJobFairByStatus(JobFairPlanStatus.APPROVE);
            if (jobFairDTOS.isEmpty()) return ResponseEntity.notFound().build();
            return new ResponseEntity<>(jobFairDTOS, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return GenericResponse.build(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(ApiEndPoint.JobFair.FOR_3D_MAP + "/{id}")
    public ResponseEntity<?> getJobFairInformationFor3DMap(@PathVariable("id") String jobFairId) {
        RenderJobFairParkResponse response = new RenderJobFairParkResponse();

        Optional<LayoutDTO> layoutDTOOpt = layoutService.getByJobFairId(jobFairId);

        if (!layoutDTOOpt.isPresent()) {
            return GenericResponse.build(MessageUtil.getMessage(MessageConstant.Layout.NOT_FOUND), HttpStatus.NOT_FOUND);
        }

        LayoutDTO layoutDTO = layoutDTOOpt.get();
        response.setJobFairLayoutUrl(layoutDTO.getUrl());

        layoutDTO.getBooths().forEach(boothDTO -> {
            Optional<CompanyBoothDTO> companyBoothOpt = companyBoothService.getCompanyBoothByJobFairIdAndBoothId(jobFairId, boothDTO.getId());
            if (companyBoothOpt.isPresent()) {
                CompanyBoothDTO companyBooth = companyBoothOpt.get();

                RenderJobFairParkResponse.BoothData boothData = new RenderJobFairParkResponse.BoothData();
                boothData.setPosition(boothDTO.getX(), boothDTO.getY(), boothDTO.getZ());
                boothData.setSlotName(boothDTO.getName());
                boothData.setCompanyBoothId(companyBooth.getId());

                Optional<CompanyBoothLayoutDTO> layoutDTOOptional = companyBoothLayoutService.getLatestVersionByCompanyBoothId(companyBooth.getId());
                if (layoutDTOOptional.isPresent()) {
                    boothData.setBoothUrl(layoutDTOOptional.get().getUrl());
                    boothData.setCompanyBoothLayoutVideos(layoutDTOOptional.get().getCompanyBoothLayoutVideos());
                    response.addBoothDataInformation(boothData);
                }
            }
        });

        return ResponseEntity.ok(response);

    }

    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).COMPANY_MANAGER) or hasAuthority(T(org.capstone.job_fair.models.enums.Role).STAFF)")
    @GetMapping(ApiEndPoint.JobFair.AVAILABLE_JOB_FAIR_FOR_REGISTRATION)
    public ResponseEntity<?> getAllAvalaibleForRegistration(@RequestParam(value = "fromTime", required = false) String fromTime,
                                                            @RequestParam(value = "toTime", required = false) String toTime) {
        List<JobFairDTO> result = jobFairService.getAllAvailableForRegistration(fromTime, toTime);
        if (result.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping(ApiEndPoint.JobFair.COMPANY_END_POINT)
    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).COMPANY_MANAGER) or hasAuthority(T(org.capstone.job_fair.models.enums.Role).COMPANY_EMPLOYEE)")
    public ResponseEntity<?> getJobFairPlanOfCompany(
            @RequestParam(value = "offset", defaultValue = ApplicationConstant.DEFAULT_SEARCH_OFFSET_VALUE) int offset,
            @RequestParam(value = "pageSize", defaultValue = ApplicationConstant.DEFAULT_SEARCH_PAGE_SIZE_VALUE) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = JobFairConstant.DEFAULT_SEARCH_SORT_BY_VALUE) String sortBy,
            @RequestParam(value = "direction", defaultValue = JobFairConstant.DEFAULT_SEARCH_SORT_DIRECTION) Sort.Direction direction,
            @RequestParam(value = "filterStatus", required = false) JobFairCompanyStatus status
    ) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Page<JobFairForCompanyResponse> data = jobFairService
                .getJobFairForCompany(userDetails.getCompanyId(), status, offset, pageSize, sortBy, direction)
                .map(dto -> {
                    JobFairForCompanyResponse response = new JobFairForCompanyResponse();
                    response.setId(dto.getJobFair().getId());
                    response.setStatus(dto.getStatus());
                    response.setDescription(dto.getJobFair().getDescription());

                    return response;
                });
        return ResponseEntity.ok(data);
    }

    @GetMapping(ApiEndPoint.JobFair.ATTENDANT_END_POINT)
    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).ATTENDANT)")
    public ResponseEntity<?> getJobFairForAttendant(
            @RequestParam(value = "offset", defaultValue = ApplicationConstant.DEFAULT_SEARCH_OFFSET_VALUE) int offset,
            @RequestParam(value = "pageSize", defaultValue = ApplicationConstant.DEFAULT_SEARCH_PAGE_SIZE_VALUE) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = JobFairConstant.DEFAULT_SEARCH_SORT_BY_VALUE) String sortBy,
            @RequestParam(value = "direction", defaultValue = JobFairConstant.DEFAULT_SEARCH_SORT_DIRECTION) Sort.Direction direction,
            @RequestParam(value = "filterStatus", required = false) JobFairAttendantStatus status
    ) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Page<AttendantJobFairStatusDTO> data = jobFairService.getJobFairForAttendant(userDetails.getId(), status, offset, pageSize, sortBy, direction);
        return ResponseEntity.ok(data);
    }

    @GetMapping(ApiEndPoint.JobFair.ADMIN_END_POINT)
    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).ADMIN) or hasAuthority(T(org.capstone.job_fair.models.enums.Role).STAFF)")
    public ResponseEntity<?> getJobFairForAdmin(
            @RequestParam(value = "offset", defaultValue = ApplicationConstant.DEFAULT_SEARCH_OFFSET_VALUE) int offset,
            @RequestParam(value = "pageSize", defaultValue = ApplicationConstant.DEFAULT_SEARCH_PAGE_SIZE_VALUE) int pageSize,
            @RequestParam(value = "filterStatus", required = false) List<JobFairAdminStatus> statuses,
            @RequestParam(value = "sortBy", defaultValue = JobFairConstant.DEFAULT_SEARCH_SORT_BY_VALUE) String sortBy,
            @RequestParam(value = "direction", defaultValue = JobFairConstant.DEFAULT_SEARCH_SORT_DIRECTION) Sort.Direction direction
    ) {
        Page<AdminJobFairStatusDTO> data = jobFairService.getJobFairForAdmin(statuses, offset, pageSize, sortBy, direction);
        return ResponseEntity.ok(data);
    }


}
