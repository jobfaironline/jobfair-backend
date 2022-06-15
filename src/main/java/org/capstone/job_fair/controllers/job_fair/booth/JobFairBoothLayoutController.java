package org.capstone.job_fair.controllers.job_fair.booth;

import lombok.extern.slf4j.Slf4j;
import org.capstone.job_fair.config.jwt.details.UserDetailsImpl;
import org.capstone.job_fair.constants.AWSConstant;
import org.capstone.job_fair.constants.ApiEndPoint;
import org.capstone.job_fair.constants.MessageConstant;
import org.capstone.job_fair.controllers.payload.responses.GenericResponse;
import org.capstone.job_fair.models.dtos.job_fair.booth.JobFairBoothDTO;
import org.capstone.job_fair.models.dtos.job_fair.booth.JobFairBoothLayoutDTO;
import org.capstone.job_fair.models.dtos.job_fair.booth.JobFairBoothLayoutVideoDTO;
import org.capstone.job_fair.services.interfaces.job_fair.booth.JobFairBoothLayoutService;
import org.capstone.job_fair.services.interfaces.job_fair.booth.JobFairBoothService;
import org.capstone.job_fair.services.interfaces.util.FileStorageService;
import org.capstone.job_fair.utils.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
public class JobFairBoothLayoutController {

    @Autowired
    private JobFairBoothLayoutService jobFairBoothLayoutService;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private JobFairBoothService jobFairBoothService;

    @GetMapping(ApiEndPoint.JobFairBoothLayout.JOB_FAIR_BOOTH_LAYOUT)
    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).COMPANY_MANAGER) or hasAuthority(T(org.capstone.job_fair.models.enums.Role).COMPANY_EMPLOYEE)")
    public ResponseEntity<?> getAllByCompanyBoothID(@RequestParam("jobFairBoothId") String jobFairBoothId) {
        List<JobFairBoothLayoutDTO> result = jobFairBoothLayoutService.getLayoutsByCompanyBoothId(jobFairBoothId);
        if (result.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok().body(result);
    }

    @GetMapping(ApiEndPoint.JobFairBoothLayout.LATEST_VERSION)
    public ResponseEntity<?> getLatestVersionByCompanyBoothID(@RequestParam("companyBoothId") String companyBoothId) {
        Optional<JobFairBoothLayoutDTO> result = jobFairBoothLayoutService.getLatestVersionByCompanyBoothId(companyBoothId);
        return result.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping(ApiEndPoint.JobFairBoothLayout.JOB_FAIR_BOOTH_LAYOUT + "/{id}")
    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).COMPANY_MANAGER) or hasAuthority(T(org.capstone.job_fair.models.enums.Role).COMPANY_EMPLOYEE)")
    public ResponseEntity<?> getById(@PathVariable("id") String id) {
        Optional<JobFairBoothLayoutDTO> result = jobFairBoothLayoutService.getById(id);
        return result.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping(ApiEndPoint.JobFairBoothLayout.JOB_FAIR_BOOTH_LAYOUT)
    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).COMPANY_MANAGER) or hasAuthority(T(org.capstone.job_fair.models.enums.Role).COMPANY_EMPLOYEE)")
    public ResponseEntity<?> createNewLayout(@RequestParam("companyBoothId") String companyBoothId, @RequestParam("file") MultipartFile file) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String companyId = userDetails.getCompanyId();

        Optional<JobFairBoothDTO> jobFairBoothOpt = jobFairBoothService.getById(companyBoothId);


        if (!jobFairBoothOpt.isPresent()) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.CompanyBoothLayout.NOT_FOUND));
        } else {
            if (!jobFairBoothOpt.get().getJobFair().getCompany().getId().equals(companyId)) {
                throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Company.COMPANY_MISSMATCH));
            }
        }

        JobFairBoothLayoutDTO dto = new JobFairBoothLayoutDTO();
        JobFairBoothDTO jobFairBoothDTO = new JobFairBoothDTO();
        jobFairBoothDTO.setId(companyBoothId);
        dto.setJobFairBooth(jobFairBoothDTO);
        dto = jobFairBoothLayoutService.createNew(dto, file);

        try {
            fileStorageService.store(file.getBytes(), AWSConstant.COMPANY_BOOTH_LAYOUT_FOLDER + "/" + dto.getId());
        } catch (IOException e) {
            return GenericResponse.build(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return ResponseEntity.created(URI.create(dto.getUrl())).body(dto);
    }

    @PostMapping(ApiEndPoint.JobFairBoothLayout.VIDEO_LAYOUT_WITH_FILE)
    public ResponseEntity<?> createNewVideoForLayoutWithFile(@RequestParam("layoutId") String layoutId,
                                                             @RequestParam("itemName") String itemName,
                                                             @RequestParam("file") MultipartFile file) {
        JobFairBoothLayoutVideoDTO dto = JobFairBoothLayoutVideoDTO.builder().itemName(itemName).jobFairBoothLayoutId(layoutId).build();
        dto = jobFairBoothLayoutService.createNewVideoWithFile(dto);
        try {
            fileStorageService.store(file.getBytes(), AWSConstant.COMPANY_BOOTH_LAYOUT_VIDEO_FOLDER + "/" + dto.getId());
        } catch (IOException e) {
            return GenericResponse.build(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.created(URI.create(dto.getUrl())).body(dto);
    }

    @PostMapping(ApiEndPoint.JobFairBoothLayout.VIDEO_LAYOUT_WITH_URL)
    public ResponseEntity<?> createNewVideoForLayoutWithUrl(@RequestParam("layoutId") String layoutId,
                                                            @RequestParam("itemName") String itemName,
                                                            @RequestParam("url") String url) {
        JobFairBoothLayoutVideoDTO dto = JobFairBoothLayoutVideoDTO.builder()
                .itemName(itemName)
                .jobFairBoothLayoutId(layoutId)
                .url(url)
                .build();

        dto = jobFairBoothLayoutService.createNewVideoWithUrl(dto);
        return ResponseEntity.created(URI.create(dto.getUrl())).body(dto);
    }
}
