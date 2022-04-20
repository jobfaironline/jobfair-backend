package org.capstone.job_fair.controllers.job_fair;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.capstone.job_fair.config.jwt.details.UserDetailsImpl;
import org.capstone.job_fair.constants.AWSConstant;
import org.capstone.job_fair.constants.ApiEndPoint;
import org.capstone.job_fair.constants.DataConstraint;
import org.capstone.job_fair.controllers.payload.requests.job_fair.CreateLayoutMetaDataRequest;
import org.capstone.job_fair.controllers.payload.requests.job_fair.UpdateLayoutMetaDataRequest;
import org.capstone.job_fair.controllers.payload.requests.layout.PickJobFairLayoutRequest;
import org.capstone.job_fair.controllers.payload.responses.GenericResponse;
import org.capstone.job_fair.models.dtos.company.CompanyDTO;
import org.capstone.job_fair.models.dtos.job_fair.LayoutDTO;
import org.capstone.job_fair.services.interfaces.job_fair.LayoutService;
import org.capstone.job_fair.services.interfaces.util.FileStorageService;
import org.capstone.job_fair.services.mappers.job_fair.LayoutMapper;
import org.capstone.job_fair.utils.ImageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
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
import java.util.concurrent.ExecutionException;

@RestController
@Slf4j
public class LayoutController {

    @Autowired
    private LayoutService layoutService;

    @Autowired
    private LayoutMapper layoutMapper;

    @Autowired
    private FileStorageService fileStorageService;

    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).ADMIN) or hasAuthority(T(org.capstone.job_fair.models.enums.Role).COMPANY_MANAGER)")
    @GetMapping(ApiEndPoint.Layout.TEMPLATE_LAYOUT)
    public ResponseEntity<List<LayoutDTO>> getTemplateLayout() {
        List<LayoutDTO> result = layoutService.getAllTemplateLayout();
        if (result.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok().body(result);
    }

    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).COMPANY_MANAGER)")
    @GetMapping(ApiEndPoint.Layout.FOR_COMPANY_MANAGER)
    public ResponseEntity<List<LayoutDTO>> getCompanyLayout() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<LayoutDTO> result = layoutService.getCompanyLayout(userDetails.getCompanyId());

        if (result.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok().body(result);
    }


    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).ADMIN) or hasAuthority(T(org.capstone.job_fair.models.enums.Role).COMPANY_MANAGER)")
    @GetMapping(ApiEndPoint.Layout.LAYOUT_ENDPOINT + "/{id}")
    public ResponseEntity<LayoutDTO> getById(@PathVariable("id") String id) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<LayoutDTO> layoutDTOOpt = layoutService.findByIdAndCompanyId(id, userDetails.getCompanyId());
        return layoutDTOOpt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).ADMIN) or hasAuthority(T(org.capstone.job_fair.models.enums.Role).COMPANY_MANAGER)")
    @PostMapping(ApiEndPoint.Layout.LAYOUT_ENDPOINT)
    public ResponseEntity<LayoutDTO> uploadMetaData(@Valid @RequestBody CreateLayoutMetaDataRequest request) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        LayoutDTO dto = layoutMapper.toDTO(request);
        if (userDetails.getCompanyId() != null){
            dto.setCompany(CompanyDTO.builder().id(userDetails.getCompanyId()).build());
        }
        dto = layoutService.createNew(dto);
        return ResponseEntity.created(URI.create(dto.getUrl())).body(dto);
    }

    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).ADMIN) or hasAuthority(T(org.capstone.job_fair.models.enums.Role).COMPANY_MANAGER)")
    @PostMapping(ApiEndPoint.Layout.LAYOUT_ENDPOINT + "/{id}/content")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file, @PathVariable("id") String id) {
        try {
            layoutService.validateAndGenerateBoothSlot(file, id);
            fileStorageService.store(file.getBytes(), AWSConstant.LAYOUT_FOLDER + "/" + id).exceptionally(throwable -> {
                log.error(throwable.getMessage());
                return null;
            });
        } catch (IllegalArgumentException e) {
            return GenericResponse.build(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (IOException e) {
            return GenericResponse.build(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.accepted().build();
    }

    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).ADMIN) or hasAuthority(T(org.capstone.job_fair.models.enums.Role).COMPANY_MANAGER)")
    @GetMapping(ApiEndPoint.Layout.LAYOUT_ENDPOINT + "/{id}/content")
    public ResponseEntity<?> getFile(@PathVariable String id) {
        Resource file;
        try {
            file = fileStorageService.loadAsResource(id).get();
        } catch (InterruptedException | ExecutionException e) {
            return GenericResponse.build(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).ADMIN) or hasAuthority(T(org.capstone.job_fair.models.enums.Role).COMPANY_MANAGER)")
    @PutMapping(ApiEndPoint.Layout.LAYOUT_ENDPOINT + "/{id}")
    public ResponseEntity<?> update(@PathVariable("id") String id, @Valid @RequestBody UpdateLayoutMetaDataRequest request) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        LayoutDTO dto = layoutMapper.toDTO(request);
        dto.setId(id);
        dto.setCompany(CompanyDTO.builder().id(userDetails.getCompanyId()).build());
        dto = layoutService.update(dto);
        return ResponseEntity.ok(dto);
    }

    @GetMapping(ApiEndPoint.Layout.GET_BY_JOB_FAIR_AND_AVAILABLE_BOOTH_SLOT + "/{id}")
    public ResponseEntity<?> getLayoutAndAvailableBoothSlotByJobFairId(@PathVariable("id") String id) {
        Optional<LayoutDTO> layoutDTOOpt = layoutService.getByJobFairIdWithAvailableBoothSlot(id);
        return layoutDTOOpt.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(ApiEndPoint.Layout.PICK_JOB_FAIR_LAYOUT)
    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).COMPANY_MANAGER)")
    public ResponseEntity<?> pickJobFairLayout(@Valid @RequestBody PickJobFairLayoutRequest request) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String companyId = userDetails.getCompanyId();
        layoutService.pickJobFairLayout(request.getJobFairId(), request.getLayoutId(), companyId);
        return ResponseEntity.ok().build();
    }

    @GetMapping(ApiEndPoint.Layout.LAYOUT_BY_JOB_FAIR + "/{id}")
    public ResponseEntity<?> getLayoutByJobFairId(@PathVariable("id") String jobFairId){
        return layoutService.getByJobFairId(jobFairId).map(dto -> ResponseEntity.ok(dto)).orElse(ResponseEntity.notFound().build());

    }

    @PostMapping(ApiEndPoint.Layout.UPLOAD_THUMBNAIL + "/{layoutId}")
    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).COMPANY_MANAGER) OR hasAuthority(T(org.capstone.job_fair.models.enums.Role).ADMIN)")
    @SneakyThrows
    public ResponseEntity<?> uploadThumbnail(@PathVariable("layoutId") String layoutId, @RequestParam("file") MultipartFile file) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String companyId = userDetails.getCompanyId();
        byte[] image = ImageUtil.convertImage(file, DataConstraint.Layout.IMAGE_TYPE, DataConstraint.Layout.WIDTH_FACTOR,
                DataConstraint.Layout.HEIGHT_FACTOR, DataConstraint.Layout.IMAGE_EXTENSION_TYPE);
        LayoutDTO layoutDTO = layoutService.createOrUpdateLayoutThumbnail(AWSConstant.LAYOUT_THUMBNAIL_FOLDER, layoutId, companyId);
        fileStorageService.store(image, AWSConstant.LAYOUT_THUMBNAIL_FOLDER + "/" + layoutDTO.getId()).exceptionally(throwable -> {
            log.error(throwable.getMessage());
            return null;
        });
        return ResponseEntity.ok(layoutDTO);
    }

}
