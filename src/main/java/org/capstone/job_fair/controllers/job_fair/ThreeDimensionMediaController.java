package org.capstone.job_fair.controllers.job_fair;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.capstone.job_fair.constants.*;
import org.capstone.job_fair.controllers.payload.requests.job_fair.CreateDecoratedItemMetaDataRequest;
import org.capstone.job_fair.controllers.payload.requests.job_fair.UpdateDecoratedItemMetaDataRequest;
import org.capstone.job_fair.controllers.payload.responses.GenericResponse;
import org.capstone.job_fair.models.dtos.job_fair.ThreeDimensionMediaDTO;
import org.capstone.job_fair.models.statuses.ThreeDimensionMediaType;
import org.capstone.job_fair.services.interfaces.job_fair.ThreeDimensionMediaService;
import org.capstone.job_fair.services.interfaces.util.FileStorageService;
import org.capstone.job_fair.services.mappers.job_fair.ThreeDimensionMediaMapper;
import org.capstone.job_fair.utils.ImageUtil;
import org.capstone.job_fair.utils.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
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

@RestController
@Slf4j
public class ThreeDimensionMediaController {

    @Autowired
    private ThreeDimensionMediaService threeDimensionMediaService;

    @Autowired
    private ThreeDimensionMediaMapper threeDimensionMediaMapper;

    @Autowired
    private FileStorageService fileStorageService;

    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).ADMIN)")
    @GetMapping(ApiEndPoint.ThreeDimensionMedia.THREE_DIMENSION_MEDIA_ENDPOINT)
    public ResponseEntity<List<ThreeDimensionMediaDTO>> getAll() {
        List<ThreeDimensionMediaDTO> result = threeDimensionMediaService.getAll();
        if (result.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok().body(result);
    }

    @GetMapping(ApiEndPoint.ThreeDimensionMedia.THREE_DIMENSION_MEDIA_ENDPOINT + "/{id}")
    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).ADMIN)")
    public ResponseEntity<ThreeDimensionMediaDTO> getById(@PathVariable("id") String id) {
        Optional<ThreeDimensionMediaDTO> decoratedItemDTOOpt = threeDimensionMediaService.findById(id);
        return decoratedItemDTOOpt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping(ApiEndPoint.ThreeDimensionMedia.THREE_DIMENSION_MEDIA_ENDPOINT)
    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).ADMIN)")
    public ResponseEntity<ThreeDimensionMediaDTO> uploadMetaData(@RequestBody @Valid CreateDecoratedItemMetaDataRequest request) {
        ThreeDimensionMediaDTO dto = threeDimensionMediaMapper.toDTO(request);
        dto = threeDimensionMediaService.createNew(dto);
        return ResponseEntity.created(URI.create(dto.getUrl())).body(dto);
    }

    @PostMapping(ApiEndPoint.ThreeDimensionMedia.THREE_DIMENSION_MEDIA_ENDPOINT + "/{id}/content")
    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).ADMIN)")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file, @PathVariable("id") String id) {
        try {
            fileStorageService.store(file.getBytes(), AWSConstant.DECORATED_ITEMS_FOLDER + "/" + id);
        } catch (IOException e) {
            return GenericResponse.build(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.accepted().build();
    }

    @GetMapping(ApiEndPoint.ThreeDimensionMedia.THREE_DIMENSION_MEDIA_ENDPOINT + "/{id}/content")
    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).ADMIN)")
    public ResponseEntity<?> getFile(@PathVariable String id) {
        Resource file = fileStorageService.loadAsResource(id);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @PutMapping(ApiEndPoint.ThreeDimensionMedia.THREE_DIMENSION_MEDIA_ENDPOINT + "/{id}")
    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).ADMIN)")
    public ResponseEntity<?> update(@Valid @RequestBody UpdateDecoratedItemMetaDataRequest request, @PathVariable String id) {
        try {
            ThreeDimensionMediaDTO dto = threeDimensionMediaMapper.toDTO(request);
            dto.setId(id);
            threeDimensionMediaService.update(dto);
            return GenericResponse.build(
                    MessageUtil.getMessage(MessageConstant.DecoratedItem.UPDATE_SUCCESSFULLY),
                    HttpStatus.OK);
        } catch (IllegalArgumentException ex) {
            return GenericResponse.build(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(ApiEndPoint.ThreeDimensionMedia.GET_BY_TYPE)
    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).ADMIN)")
    public ResponseEntity<?> getByType(@RequestParam(value = "type", required = false, defaultValue = ThreeDimensionMediaConstant.DEFAULT_SEARCH_TYPE) ThreeDimensionMediaType type,
                                       @RequestParam(value = "offset", defaultValue = ThreeDimensionMediaConstant.DEFAULT_SEARCH_OFFSET_VALUE) int offset,
                                       @RequestParam(value = "pageSize", defaultValue = ThreeDimensionMediaConstant.DEFAULT_SEARCH_PAGE_SIZE_VALUE) int pageSize,
                                       @RequestParam(value = "sortBy", defaultValue = ThreeDimensionMediaConstant.DEFAULT_SEARCH_SORT_BY_VALUE) String sortBy,
                                       @RequestParam(value = "direction", required = false, defaultValue = ThreeDimensionMediaConstant.DEFAULT_SORT_DIRECTION) Sort.Direction direction) {
        Page<ThreeDimensionMediaDTO> threeDimensionMediaDTOPage = threeDimensionMediaService.findByType(type, offset, pageSize, sortBy, direction);
        if (threeDimensionMediaDTOPage.getTotalElements() == 0) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(threeDimensionMediaDTOPage);
    }


    @PostMapping(ApiEndPoint.ThreeDimensionMedia.UPLODAD_THUMBNAIL + "/{id}")
    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).ADMIN)")
    @SneakyThrows
    @SuppressWarnings("unchecked")
    public ResponseEntity<?> uploadThumbnail(@PathVariable("id") String id, @RequestParam("file") MultipartFile file) {
        byte[] image = ImageUtil.convertImage(file, DataConstraint.Layout.IMAGE_TYPE, DataConstraint.Layout.WIDTH_FACTOR,
                DataConstraint.Layout.HEIGHT_FACTOR, DataConstraint.Layout.IMAGE_EXTENSION_TYPE);
        ThreeDimensionMediaDTO dto = threeDimensionMediaService.createOrUpdateThumbnail(AWSConstant.DECORATED_THUMBNAILS_FOLDER, id);
        fileStorageService.store(image, AWSConstant.DECORATED_THUMBNAILS_FOLDER + "/" + dto.getId());
        return ResponseEntity.ok(dto);
    }


}
