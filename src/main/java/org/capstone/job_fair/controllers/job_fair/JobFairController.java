package org.capstone.job_fair.controllers.job_fair;

import org.capstone.job_fair.constants.ApiEndPoint;
import org.capstone.job_fair.constants.MessageConstant;
import org.capstone.job_fair.controllers.payload.requests.job_fair.AdminEvaluateJobFairRequest;
import org.capstone.job_fair.controllers.payload.requests.job_fair.CancelJobFairRequest;
import org.capstone.job_fair.controllers.payload.requests.job_fair.DraftJobFairPlanRequest;
import org.capstone.job_fair.controllers.payload.requests.job_fair.UpdateJobFairPlanDraftRequest;
import org.capstone.job_fair.controllers.payload.responses.GenericResponse;
import org.capstone.job_fair.controllers.payload.responses.RenderJobFairParkResponse;
import org.capstone.job_fair.models.dtos.company.CompanyBoothDTO;
import org.capstone.job_fair.models.dtos.company.CompanyBoothLayoutDTO;
import org.capstone.job_fair.models.dtos.job_fair.JobFairDTO;
import org.capstone.job_fair.models.dtos.job_fair.LayoutDTO;
import org.capstone.job_fair.models.statuses.JobFairStatus;
import org.capstone.job_fair.services.interfaces.company.CompanyBoothLayoutService;
import org.capstone.job_fair.services.interfaces.company.CompanyBoothService;
import org.capstone.job_fair.services.interfaces.job_fair.JobFairService;
import org.capstone.job_fair.services.interfaces.job_fair.LayoutService;
import org.capstone.job_fair.utils.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    public ResponseEntity<?> getAllJobFairPlanOfEmployee() {
        try {
            return new ResponseEntity<>(jobFairService.getAllJobFairPlanOfCurrentAccount(), HttpStatus.OK);
        } catch (IllegalArgumentException ex) {
            return GenericResponse.build(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
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
    public ResponseEntity<?> getAll() {
        List<JobFairDTO> result = jobFairService.getAll();
        if (result.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(result);
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
            List<JobFairDTO> jobFairDTOS = jobFairService.getAllJobFairByStatus(JobFairStatus.APPROVE);
            if (jobFairDTOS.isEmpty()) return ResponseEntity.notFound().build();
            return new ResponseEntity<>(jobFairDTOS, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return GenericResponse.build(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(ApiEndPoint.JobFair.FOR_3D_MAP + "/{id}")
    public ResponseEntity<?> getJobFairById(@PathVariable("id") String jobFairId) {
        RenderJobFairParkResponse response = new RenderJobFairParkResponse();

        Optional<LayoutDTO> layoutDTOOpt = layoutService.findByJobFairId(jobFairId);

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
                    response.addBoothDataInformation(boothData);
                }
            }
        });

        return ResponseEntity.ok(response);

    }

    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).COMPANY_MANAGER) or hasAuthority(T(org.capstone.job_fair.models.enums.Role).STAFF)")
    @GetMapping(ApiEndPoint.JobFair.AVALAIBLE_JOB_FAIR_FOR_REGISTRATION)
    public ResponseEntity<?> getAllAvalaibleForRegistration(@RequestParam(value = "fromTime", required = false) String fromTime,
                                                            @RequestParam(value = "toTime", required = false) String toTime) {
        List<JobFairDTO> result = jobFairService.getAllAvalaibleForRegistration(fromTime, toTime);
        if (result.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(result);
    }

}
