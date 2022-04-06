package org.capstone.job_fair.controllers.job_fair;

import org.capstone.job_fair.config.jwt.details.UserDetailsImpl;
import org.capstone.job_fair.constants.ApiEndPoint;
import org.capstone.job_fair.constants.JobFairConstant;
import org.capstone.job_fair.constants.MessageConstant;
import org.capstone.job_fair.controllers.payload.requests.job_fair.DraftJobFairRequest;
import org.capstone.job_fair.controllers.payload.requests.job_fair.UpdateJobFairRequest;
import org.capstone.job_fair.controllers.payload.responses.GenericResponse;
import org.capstone.job_fair.controllers.payload.responses.RenderJobFairParkResponse;
import org.capstone.job_fair.models.dtos.company.CompanyDTO;
import org.capstone.job_fair.models.dtos.company.JobFairBoothDTO;
import org.capstone.job_fair.models.dtos.company.JobFairBoothLayoutDTO;
import org.capstone.job_fair.models.dtos.job_fair.JobFairDTO;
import org.capstone.job_fair.models.dtos.job_fair.LayoutDTO;
import org.capstone.job_fair.services.interfaces.company.CompanyBoothLayoutService;
import org.capstone.job_fair.services.interfaces.company.CompanyBoothService;
import org.capstone.job_fair.services.interfaces.job_fair.JobFairService;
import org.capstone.job_fair.services.interfaces.job_fair.LayoutService;
import org.capstone.job_fair.services.mappers.job_fair.JobFairMapper;
import org.capstone.job_fair.utils.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class JobFairController {

    @Autowired
    private CompanyBoothService companyBoothService;

    @Autowired
    private CompanyBoothLayoutService companyBoothLayoutService;

    @Autowired
    private LayoutService layoutService;

    @Autowired
    private JobFairService jobFairService;

    @Autowired
    private JobFairMapper jobFairMapper;

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
            Optional<JobFairBoothDTO> companyBoothOpt = companyBoothService.getCompanyBoothByJobFairIdAndBoothId(jobFairId, boothDTO.getId());
            if (companyBoothOpt.isPresent()) {
                JobFairBoothDTO companyBooth = companyBoothOpt.get();

                RenderJobFairParkResponse.BoothData boothData = new RenderJobFairParkResponse.BoothData();
                boothData.setPosition(boothDTO.getX(), boothDTO.getY(), boothDTO.getZ());
                boothData.setSlotName(boothDTO.getName());
                boothData.setCompanyBoothId(companyBooth.getId());

                Optional<JobFairBoothLayoutDTO> layoutDTOOptional = companyBoothLayoutService.getLatestVersionByCompanyBoothId(companyBooth.getId());
                if (layoutDTOOptional.isPresent()) {
                    boothData.setBoothUrl(layoutDTOOptional.get().getUrl());
                    boothData.setCompanyBoothLayoutVideos(layoutDTOOptional.get().getCompanyBoothLayoutVideos());
                    response.addBoothDataInformation(boothData);
                }
            }
        });

        return ResponseEntity.ok(response);
    }

    @GetMapping(ApiEndPoint.JobFair.JOB_FAIR + "/{id}")
    public ResponseEntity<?> getJobFairDetailById(@PathVariable("id") String id) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<JobFairDTO> result = jobFairService.getById(id);
        if (!result.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        JobFairDTO jobFairDTO = result.get();
        if (!jobFairDTO.getCompany().getId().equals(userDetails.getCompanyId())) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(jobFairDTO);
    }

    @PostMapping(ApiEndPoint.JobFair.JOB_FAIR)
    public ResponseEntity<?> draftJobFair(@RequestBody DraftJobFairRequest request) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        JobFairDTO jobFairDTO = jobFairMapper.toDTO(request);
        jobFairDTO.setCompany(CompanyDTO.builder().id(userDetails.getCompanyId()).build());
        jobFairDTO = jobFairService.createNewJobFair(jobFairDTO);
        return ResponseEntity.ok(jobFairDTO);
    }

    @PutMapping(ApiEndPoint.JobFair.JOB_FAIR)
    public ResponseEntity<?> updateJobFair(@RequestBody UpdateJobFairRequest request) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        JobFairDTO jobFairDTO = jobFairMapper.toDTO(request);
        jobFairDTO = jobFairService.updateJobFair(jobFairDTO, userDetails.getCompanyId());
        return ResponseEntity.ok(jobFairDTO);
    }

    @DeleteMapping(ApiEndPoint.JobFair.JOB_FAIR + "/{id}")
    public ResponseEntity<?> deleteJobFair(@PathVariable("id") String id) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        JobFairDTO jobFairDTO = jobFairService.deleteJobFair(id, userDetails.getCompanyId());
        return ResponseEntity.ok(jobFairDTO);
    }

    @GetMapping(ApiEndPoint.JobFair.JOB_FAIR)
    public ResponseEntity<?> getJobFair(
            @RequestParam(value = "offset", defaultValue = JobFairConstant.DEFAULT_SEARCH_OFFSET_VALUE) int offset,
            @RequestParam(value = "pageSize", defaultValue = JobFairConstant.DEFAULT_SEARCH_PAGE_SIZE_VALUE) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = JobFairConstant.DEFAULT_SEARCH_SORT_BY_VALUE) String sortBy,
            @RequestParam(value = "direction", required = false, defaultValue = JobFairConstant.DEFAULT_SEARCH_SORT_DIRECTION) Sort.Direction direction,
            @RequestParam(value = "name", defaultValue = JobFairConstant.DEFAULT_JOBFAIR_NAME) String name
    ) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Page<JobFairDTO> result = jobFairService.findByNameAndCompanyId(name, userDetails.getCompanyId(), PageRequest.of(offset, pageSize).withSort(Sort.by(direction, sortBy)));
        return ResponseEntity.ok(result);

    }


}
