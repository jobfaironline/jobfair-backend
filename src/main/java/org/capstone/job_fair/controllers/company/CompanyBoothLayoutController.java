package org.capstone.job_fair.controllers.company;

import lombok.extern.slf4j.Slf4j;
import org.capstone.job_fair.config.jwt.details.UserDetailsImpl;
import org.capstone.job_fair.constants.AWSConstant;
import org.capstone.job_fair.constants.ApiEndPoint;
import org.capstone.job_fair.constants.MessageConstant;
import org.capstone.job_fair.controllers.payload.responses.GenericResponse;
import org.capstone.job_fair.models.dtos.company.JobFairBoothDTO;
import org.capstone.job_fair.models.dtos.company.JobFairBoothLayoutDTO;
import org.capstone.job_fair.models.dtos.company.JobFairBoothLayoutVideoDTO;
import org.capstone.job_fair.services.interfaces.company.CompanyBoothLayoutService;
import org.capstone.job_fair.services.interfaces.company.CompanyService;
import org.capstone.job_fair.services.interfaces.util.FileStorageService;
import org.capstone.job_fair.services.mappers.company.CompanyBoothLayoutMapper;
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
public class CompanyBoothLayoutController {

    @Autowired
    private CompanyBoothLayoutService companyBoothLayoutService;

    @Autowired
    private CompanyBoothLayoutMapper companyBoothLayoutMapper;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private CompanyService companyService;

    @GetMapping(ApiEndPoint.CompanyBoothLayout.COMPANY_BOOTH_LAYOUT)
    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).COMPANY_MANAGER) or hasAuthority(T(org.capstone.job_fair.models.enums.Role).COMPANY_EMPLOYEE)")
    public ResponseEntity<?> getAllByCompanyBoothID(@RequestParam("companyBoothId") String companyBoothId) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String companyId = userDetails.getCompanyId();

        Optional<String> companyIdOpt = companyService.getIdByCompanyBoothID(companyBoothId);
        if (!companyIdOpt.isPresent()) {
            return ResponseEntity.noContent().build();
        } else {
            if (!companyIdOpt.get().equals(companyId)) {
                throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Company.COMPANY_MISSMATCH));
            }
        }

        List<JobFairBoothLayoutDTO> result = companyBoothLayoutService.getLayoutsByCompanyBoothId(companyBoothId);
        if (result.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok().body(result);
    }

    @GetMapping(ApiEndPoint.CompanyBoothLayout.LATEST_VERSION)
    public ResponseEntity<?> getLatestVersionByCompanyBoothID(@RequestParam("companyBoothId") String companyBoothId) {
        Optional<JobFairBoothLayoutDTO> result = companyBoothLayoutService.getLatestVersionByCompanyBoothId(companyBoothId);
        return result.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping(ApiEndPoint.CompanyBoothLayout.COMPANY_BOOTH_LAYOUT + "/{id}")
    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).COMPANY_MANAGER) or hasAuthority(T(org.capstone.job_fair.models.enums.Role).COMPANY_EMPLOYEE)")
    public ResponseEntity<?> getById(@PathVariable("id") String id) {
        Optional<JobFairBoothLayoutDTO> result = companyBoothLayoutService.getById(id);
        return result.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping(ApiEndPoint.CompanyBoothLayout.COMPANY_BOOTH_LAYOUT)
    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).COMPANY_MANAGER) or hasAuthority(T(org.capstone.job_fair.models.enums.Role).COMPANY_EMPLOYEE)")
    public ResponseEntity<?> createNewLayout(@RequestParam("companyBoothId") String companyBoothId, @RequestParam("file") MultipartFile file) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String companyId = userDetails.getCompanyId();

        Optional<String> companyIdOpt = companyService.getIdByCompanyBoothID(companyBoothId);
        if (!companyIdOpt.isPresent()) {
            throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.CompanyBoothLayout.NOT_FOUND));
        } else {
            if (!companyIdOpt.get().equals(companyId)) {
                throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Company.COMPANY_MISSMATCH));
            }
        }

        JobFairBoothLayoutDTO dto = new JobFairBoothLayoutDTO();
        JobFairBoothDTO jobFairBoothDTO = new JobFairBoothDTO();
        jobFairBoothDTO.setId(companyBoothId);
        dto.setJobFairBooth(jobFairBoothDTO);
        dto = companyBoothLayoutService.createNew(dto, file);

        try {
            fileStorageService.store(file.getBytes(), AWSConstant.COMPANY_BOOTH_LAYOUT_FOLDER + "/" + dto.getId()).exceptionally(throwable -> {
                log.error(throwable.getMessage());
                return null;
            });
        } catch (IOException e) {
            return GenericResponse.build(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return ResponseEntity.created(URI.create(dto.getUrl())).body(dto);
    }

    @PostMapping(ApiEndPoint.CompanyBoothLayout.VIDEO_LAYOUT_WITH_FILE)
    public ResponseEntity<?> createNewVideoForLayoutWithFile(@RequestParam("layoutId") String layoutId,
                                                     @RequestParam("itemName") String itemName,
                                                     @RequestParam("file") MultipartFile file ){
        JobFairBoothLayoutVideoDTO dto = JobFairBoothLayoutVideoDTO.builder().itemName(itemName).jobFairBoothLayoutId(layoutId).build();
        dto = companyBoothLayoutService.createNewVideoWithFile(dto);
        try {
            fileStorageService.store(file.getBytes(), AWSConstant.COMPANY_BOOTH_LAYOUT_VIDEO_FOLDER + "/" + dto.getId()).exceptionally(throwable -> {
                log.error(throwable.getMessage());
                return null;
            });
        } catch (IOException e) {
            return GenericResponse.build(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.created(URI.create(dto.getUrl())).body(dto);
    }

    @PostMapping(ApiEndPoint.CompanyBoothLayout.VIDEO_LAYOUT_WITH_URL)
    public ResponseEntity<?> createNewVideoForLayoutWithUrl(@RequestParam("layoutId") String layoutId,
                                                            @RequestParam("itemName") String itemName,
                                                            @RequestParam("url") String url){
        JobFairBoothLayoutVideoDTO dto = JobFairBoothLayoutVideoDTO.builder()
                .itemName(itemName)
                .jobFairBoothLayoutId(layoutId)
                .url(url)
                .build();

        dto = companyBoothLayoutService.createNewVideoWithUrl(dto);
        return ResponseEntity.created(URI.create(dto.getUrl())).body(dto);
    }
}
