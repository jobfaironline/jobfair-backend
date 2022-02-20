package org.capstone.job_fair.controllers.company;

import lombok.extern.slf4j.Slf4j;
import org.capstone.job_fair.config.jwt.details.UserDetailsImpl;
import org.capstone.job_fair.constants.AWSConstant;
import org.capstone.job_fair.constants.ApiEndPoint;
import org.capstone.job_fair.constants.MessageConstant;
import org.capstone.job_fair.controllers.payload.requests.company.CreateCompanyBoothLayoutMetaDataRequest;
import org.capstone.job_fair.controllers.payload.responses.GenericResponse;
import org.capstone.job_fair.models.dtos.company.CompanyBoothLayoutDTO;
import org.capstone.job_fair.models.dtos.company.CompanyDTO;
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

import javax.validation.Valid;
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

        List<CompanyBoothLayoutDTO> result = companyBoothLayoutService.getLayoutsByCompanyBoothId(companyBoothId);
        if (result.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok().body(result);
    }

    @GetMapping(ApiEndPoint.CompanyBoothLayout.LATEST_VERSION)
    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).COMPANY_MANAGER) or hasAuthority(T(org.capstone.job_fair.models.enums.Role).COMPANY_EMPLOYEE)")
    public ResponseEntity<?> getLatestVersionByCompanyBoothID(@RequestParam("companyBoothId") String companyBoothId) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String companyId = userDetails.getCompanyId();

        Optional<String> companyIdOpt = companyService.getIdByCompanyBoothID(companyBoothId);
        if (!companyIdOpt.isPresent()) {
            return ResponseEntity.notFound().build();
        } else {
            if (!companyIdOpt.get().equals(companyId)) {
                throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Company.COMPANY_MISSMATCH));
            }
        }

        Optional<CompanyBoothLayoutDTO> result = companyBoothLayoutService.getLatestVersionByCompanyBoothId(companyBoothId);
        return result.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping(ApiEndPoint.CompanyBoothLayout.COMPANY_BOOTH_LAYOUT + "/{id}")
    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).COMPANY_MANAGER) or hasAuthority(T(org.capstone.job_fair.models.enums.Role).COMPANY_EMPLOYEE)")
    public ResponseEntity<?> getById(@PathVariable("id") String id) {
        Optional<CompanyBoothLayoutDTO> result = companyBoothLayoutService.getById(id);
        return result.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping(ApiEndPoint.CompanyBoothLayout.COMPANY_BOOTH_LAYOUT)
    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).COMPANY_MANAGER) or hasAuthority(T(org.capstone.job_fair.models.enums.Role).COMPANY_EMPLOYEE)")
    public ResponseEntity<?> uploadFileMetaData(@RequestBody @Valid CreateCompanyBoothLayoutMetaDataRequest request) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String companyId = userDetails.getCompanyId();

        Optional<String> companyIdOpt = companyService.getIdByCompanyBoothID(request.getCompanyBoothId());
        if (!companyIdOpt.isPresent()) {
            return ResponseEntity.noContent().build();
        } else {
            if (!companyIdOpt.get().equals(companyId)) {
                throw new IllegalArgumentException(MessageUtil.getMessage(MessageConstant.Company.COMPANY_MISSMATCH));
            }
        }

        CompanyBoothLayoutDTO dto = companyBoothLayoutMapper.toDTO(request);
        dto = companyBoothLayoutService.createNew(dto);
        return ResponseEntity.created(URI.create(dto.getUrl())).body(dto);
    }

    @PostMapping(ApiEndPoint.CompanyBoothLayout.COMPANY_BOOTH_LAYOUT + "/{id}/content")
    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).COMPANY_MANAGER) or hasAuthority(T(org.capstone.job_fair.models.enums.Role).COMPANY_EMPLOYEE)")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file, @PathVariable("id") String id) {
        try {
            fileStorageService.store(file.getBytes(), AWSConstant.COMPANY_BOOTH_LAYOUT_FOLDER + "/" + id).exceptionally(throwable -> {
                log.error(throwable.getMessage());
                return null;
            });
        } catch (IOException e) {
            return GenericResponse.build(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.accepted().build();
    }
}
