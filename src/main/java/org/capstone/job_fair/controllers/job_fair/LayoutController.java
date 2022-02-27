package org.capstone.job_fair.controllers.job_fair;

import lombok.extern.slf4j.Slf4j;
import org.capstone.job_fair.constants.AWSConstant;
import org.capstone.job_fair.constants.ApiEndPoint;
import org.capstone.job_fair.constants.MessageConstant;
import org.capstone.job_fair.controllers.payload.requests.job_fair.CreateLayoutMetaDataRequest;
import org.capstone.job_fair.controllers.payload.requests.job_fair.UpdateLayoutMetaDataRequest;
import org.capstone.job_fair.controllers.payload.responses.GenericResponse;
import org.capstone.job_fair.models.dtos.job_fair.LayoutDTO;
import org.capstone.job_fair.services.interfaces.job_fair.LayoutService;
import org.capstone.job_fair.services.interfaces.util.FileStorageService;
import org.capstone.job_fair.services.mappers.job_fair.LayoutMapper;
import org.capstone.job_fair.utils.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
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

    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).ADMIN) or hasAuthority(T(org.capstone.job_fair.models.enums.Role).STAFF)")
    @GetMapping(ApiEndPoint.Layout.LAYOUT_ENDPOINT)
    public ResponseEntity<List<LayoutDTO>> getAll() {
        List<LayoutDTO> result = layoutService.getAll();
        if (result.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok().body(result);
    }

    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).ADMIN) or hasAuthority(T(org.capstone.job_fair.models.enums.Role).STAFF)")
    @GetMapping(ApiEndPoint.Layout.LAYOUT_ENDPOINT + "/{id}")
    public ResponseEntity<LayoutDTO> getById(@PathVariable("id") String id) {
        Optional<LayoutDTO> layoutDTOOpt = layoutService.findById(id);
        return layoutDTOOpt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).ADMIN) or hasAuthority(T(org.capstone.job_fair.models.enums.Role).STAFF)")
    @PostMapping(ApiEndPoint.Layout.LAYOUT_ENDPOINT)
    public ResponseEntity<LayoutDTO> uploadMetaData(@Valid @RequestBody CreateLayoutMetaDataRequest request) {
        LayoutDTO dto = layoutMapper.toDTO(request);
        dto = layoutService.createNew(dto);
        return ResponseEntity.created(URI.create(dto.getUrl())).body(dto);
    }

    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).ADMIN) or hasAuthority(T(org.capstone.job_fair.models.enums.Role).STAFF)")
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

    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).ADMIN) or hasAuthority(T(org.capstone.job_fair.models.enums.Role).STAFF)")
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

    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).ADMIN) or hasAuthority(T(org.capstone.job_fair.models.enums.Role).STAFF)")
    @PutMapping(ApiEndPoint.Layout.LAYOUT_ENDPOINT + "/{id}")
    public ResponseEntity<?> update(@PathVariable("id") String id, @Valid @RequestBody UpdateLayoutMetaDataRequest request) {
        try {
            LayoutDTO dto = layoutMapper.toDTO(request);
            dto.setId(id);
            layoutService.update(dto);
            return GenericResponse.build(
                    MessageUtil.getMessage(MessageConstant.Layout.UPDATE_SUCCESSFULLY),
                    HttpStatus.OK);
        } catch (IllegalArgumentException ex) {
            return GenericResponse.build(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(ApiEndPoint.Layout.GET_BY_JOB_FAIR_ID + "/{id}")
    public ResponseEntity<?> getByJobFairId(@PathVariable(value = "id") String jobFairId){
        System.out.println(jobFairId);
        Optional<LayoutDTO> layoutDTOOpt = layoutService.findByJobFairId(jobFairId);
        if (!layoutDTOOpt.isPresent()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(layoutDTOOpt.get());
    }

}
