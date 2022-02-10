package org.capstone.job_fair.controllers.job_fair;

import org.capstone.job_fair.constants.AWSConstant;
import org.capstone.job_fair.constants.ApiEndPoint;
import org.capstone.job_fair.constants.MessageConstant;
import org.capstone.job_fair.controllers.payload.requests.CreateLayoutMetaDataRequest;
import org.capstone.job_fair.controllers.payload.requests.UpdateLayoutMetaDataRequest;
import org.capstone.job_fair.controllers.payload.responses.GenericResponse;
import org.capstone.job_fair.models.dtos.job_fair.LayoutDTO;
import org.capstone.job_fair.services.interfaces.job_fair.LayoutService;
import org.capstone.job_fair.services.interfaces.util.FileStorageService;
import org.capstone.job_fair.services.mappers.LayoutMapper;
import org.capstone.job_fair.utils.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
public class LayoutController {

    @Autowired
    private LayoutService layoutService;

    @Autowired
    private LayoutMapper layoutMapper;

    @Autowired
    private FileStorageService fileStorageService;

    @GetMapping(ApiEndPoint.Layout.LAYOUT_ENDPOINT)
    public ResponseEntity<List<LayoutDTO>> getAll() {
        List<LayoutDTO> result = layoutService.getAll();
        if (result.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok().body(result);
    }

    @GetMapping(ApiEndPoint.Layout.LAYOUT_ENDPOINT + "/{id}")
    public ResponseEntity<LayoutDTO> getById(@PathVariable("id") String id) {
        Optional<LayoutDTO> layoutDTOOpt = layoutService.findById(id);
        return layoutDTOOpt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping(ApiEndPoint.Layout.LAYOUT_ENDPOINT)
    public ResponseEntity<LayoutDTO> uploadMetaData(@Valid @RequestBody CreateLayoutMetaDataRequest request) {
        LayoutDTO dto = layoutMapper.toDTO(request);
        dto = layoutService.createNew(dto);
        return ResponseEntity.created(URI.create(dto.getUrl())).body(dto);
    }

    @PostMapping(ApiEndPoint.Layout.LAYOUT_ENDPOINT + "/{id}/content")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file, @PathVariable("id") String id) {
        fileStorageService.store(file, AWSConstant.LAYOUT_FOLDER + "/" + id);
        return ResponseEntity.accepted().build();
    }

    @GetMapping(ApiEndPoint.Layout.LAYOUT_ENDPOINT + "/{id}/content")
    public ResponseEntity<Resource> getFile(@PathVariable String id) {
        Resource file = fileStorageService.loadAsResource(id);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

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


}
