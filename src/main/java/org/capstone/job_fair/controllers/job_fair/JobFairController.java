package org.capstone.job_fair.controllers.job_fair;

import org.capstone.job_fair.constants.ApiEndPoint;
import org.capstone.job_fair.constants.MessageConstant;
import org.capstone.job_fair.controllers.payload.requests.CreateJobFairPlanRequest;
import org.capstone.job_fair.controllers.payload.responses.GenericResponse;
import org.capstone.job_fair.models.dtos.job_fair.JobFairDTO;
import org.capstone.job_fair.services.interfaces.job_fair.JobFairService;
import org.capstone.job_fair.utils.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
public class JobFairController {

    @Autowired
    private JobFairService jobFairService;

  @PostMapping(ApiEndPoint.JobFair.JOB_FAIR_PLAN)
    public ResponseEntity<?> draftJobFairPlan(@Validated @RequestBody CreateJobFairPlanRequest request){
      try{
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
          jobFairService.createJobFair(dto);

          return GenericResponse.build(MessageUtil.getMessage(MessageConstant.JobFair.CREATE_JOB_FAIR_PLAN_SUCCESSFULLY)
                  ,HttpStatus.OK);
      } catch (IllegalArgumentException ex){
          return GenericResponse.build(ex.getMessage(), HttpStatus.BAD_REQUEST);
      }
  }
    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).ADMIN) or hasAuthority(T(org.capstone.job_fair.models.enums.Role).STAFF)")
    @GetMapping(ApiEndPoint.JobFair.JOB_FAIR_PLAN)
    public ResponseEntity<?> getAllJobFairPlanOfEmployee(){
      try{
          return new ResponseEntity<>(jobFairService.getAllJobFairPlanOfCurrentAccount(),HttpStatus.OK);
      } catch (IllegalArgumentException ex){
          return GenericResponse.build(ex.getMessage(), HttpStatus.BAD_REQUEST);
      }
  }
    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).ADMIN) or hasAuthority(T(org.capstone.job_fair.models.enums.Role).STAFF)")
    @GetMapping(ApiEndPoint.JobFair.DELETE_JOB_FAIR_PLAN_DRAFT + "/{jobfairId}")
    public ResponseEntity<?> deleteJobFairPlanDraft(@PathVariable("jobfairId") String jobFairId){
        try{
            jobFairService.deleteJobFairDraft(jobFairId);
            return GenericResponse.build(MessageConstant.JobFair.DELETE_JOB_FAIR_PLAN_SUCCESSFULLY, HttpStatus.OK);
        } catch (IllegalArgumentException ex){
            return GenericResponse.build(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).ADMIN) or hasAuthority(T(org.capstone.job_fair.models.enums.Role).STAFF)")
    @GetMapping(ApiEndPoint.JobFair.SUBMIT_JOB_FAIR_PLAN_DRAFT + "/{jobfairId}")
    public ResponseEntity<?> submitJobFairPlanDraft(@PathVariable("jobfairId") String jobFairId){
        try{
            jobFairService.submitJobFairDraft(jobFairId);
            return GenericResponse.build(MessageConstant.JobFair.SUBMIT_JOB_FAIR_PLAN_SUCCESSFULLY, HttpStatus.OK);
        } catch (IllegalArgumentException ex){
            return GenericResponse.build(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).ADMIN) or hasAuthority(T(org.capstone.job_fair.models.enums.Role).STAFF)")
    @GetMapping(ApiEndPoint.JobFair.CANCEL_PENDING_JOB_FAIR_PLAN + "/{jobfairId}")
    public ResponseEntity<?> cancelJobFairPlanPending(@PathVariable("jobfairId") String jobFairId){
        try{
            jobFairService.cancelPendingJobFair(jobFairId);
            return GenericResponse.build(MessageConstant.JobFair.CANCEL_JOB_FAIR_PLAN_SUCCESSFULLY, HttpStatus.OK);
        } catch (IllegalArgumentException ex){
            return GenericResponse.build(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).ADMIN) or hasAuthority(T(org.capstone.job_fair.models.enums.Role).STAFF)")
    @GetMapping(ApiEndPoint.JobFair.RESTORE_DELETED_JOB_FAIR_PLAN + "/{jobfairId}")
    public ResponseEntity<?> restoreDeletedJobFairPlan(@PathVariable("jobfairId") String jobFairId){
        try{
            jobFairService.cancelPendingJobFair(jobFairId);
            return GenericResponse.build(MessageConstant.JobFair.CANCEL_JOB_FAIR_PLAN_SUCCESSFULLY, HttpStatus.OK);
        } catch (IllegalArgumentException ex){
            return GenericResponse.build(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
