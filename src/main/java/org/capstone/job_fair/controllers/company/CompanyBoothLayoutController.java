package org.capstone.job_fair.controllers.company;

import lombok.extern.slf4j.Slf4j;
import org.capstone.job_fair.constants.AWSConstant;
import org.capstone.job_fair.constants.ApiEndPoint;
import org.capstone.job_fair.controllers.payload.requests.company.CreateCompanyBoothLayoutMetaDataRequest;
import org.capstone.job_fair.controllers.payload.responses.GenericResponse;
import org.capstone.job_fair.models.dtos.company.CompanyBoothLayoutDTO;
import org.capstone.job_fair.services.interfaces.company.CompanyBoothLayoutService;
import org.capstone.job_fair.services.interfaces.util.FileStorageService;
import org.capstone.job_fair.services.mappers.company.CompanyBoothLayoutMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @GetMapping(ApiEndPoint.CompanyBoothLayout.COMPANY_BOOTH_LAYOUT)
    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).COMPANY_MANAGER) or hasAuthority(T(org.capstone.job_fair.models.enums.Role).COMPANY_EMPLOYEE)")
    public ResponseEntity<?> getAllByCompanyBoothID(@RequestParam("companyBoothId") String companyBoothId) {
        List<CompanyBoothLayoutDTO> result = companyBoothLayoutService.getLayoutsByCompanyBoothId(companyBoothId);
        if (result.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok().body(result);
    }

    @GetMapping(ApiEndPoint.CompanyBoothLayout.LATEST_VERSION)
    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).COMPANY_MANAGER) or hasAuthority(T(org.capstone.job_fair.models.enums.Role).COMPANY_EMPLOYEE)")
    public ResponseEntity<?> getLatestVersionByCompanyBoothID(@RequestParam("companyBoothId") String companyBoothId) {
        Optional<CompanyBoothLayoutDTO> result = companyBoothLayoutService.getLatestVersionByCompanyBoothId(companyBoothId);
        return result.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping(ApiEndPoint.CompanyBoothLayout.COMPANY_BOOTH_LAYOUT + "/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") String id) {
        Optional<CompanyBoothLayoutDTO> result = companyBoothLayoutService.getById(id);
        return result.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping(ApiEndPoint.CompanyBoothLayout.COMPANY_BOOTH_LAYOUT)
    public ResponseEntity<?> uploadFileMetaData(@RequestBody @Valid CreateCompanyBoothLayoutMetaDataRequest request) {
        CompanyBoothLayoutDTO dto = companyBoothLayoutMapper.toDTO(request);
        dto = companyBoothLayoutService.createNew(dto);
        return ResponseEntity.created(URI.create(dto.getUrl())).body(dto);
    }

    @PostMapping(ApiEndPoint.CompanyBoothLayout.COMPANY_BOOTH_LAYOUT + "/{id}/content")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file, @PathVariable("id") String id){
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
