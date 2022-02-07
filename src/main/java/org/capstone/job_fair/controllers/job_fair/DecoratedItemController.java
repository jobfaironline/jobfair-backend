package org.capstone.job_fair.controllers.job_fair;

import org.capstone.job_fair.constants.ApiEndPoint;
import org.capstone.job_fair.constants.MessageConstant;
import org.capstone.job_fair.controllers.payload.requests.CreateDecoratedItemMetaDataRequest;
import org.capstone.job_fair.controllers.payload.requests.UpdateDecoratedItemMetaDataRequest;
import org.capstone.job_fair.controllers.payload.responses.GenericResponse;
import org.capstone.job_fair.models.dtos.job_fair.DecoratedItemDTO;
import org.capstone.job_fair.services.interfaces.job_fair.DecoratedItemService;
import org.capstone.job_fair.services.interfaces.util.FileStorageService;
import org.capstone.job_fair.services.mappers.DecoratedItemMapper;
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
public class DecoratedItemController {

    @Autowired
    private DecoratedItemService decoratedItemService;

    @Autowired
    private DecoratedItemMapper decoratedItemMapper;

    @Autowired
    private FileStorageService fileStorageService;

    @GetMapping(ApiEndPoint.DecoratedItem.DECORATED_ITEM_ENDPOINT)
    public ResponseEntity<List<DecoratedItemDTO>> getAll() {
        List<DecoratedItemDTO> result = decoratedItemService.getAll();
        if (result.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok().body(result);
    }

    @GetMapping(ApiEndPoint.DecoratedItem.DECORATED_ITEM_ENDPOINT + "/{id}")
    public ResponseEntity<DecoratedItemDTO> getById(@PathVariable("id") String id) {
        Optional<DecoratedItemDTO> decoratedItemDTOOpt = decoratedItemService.findById(id);
        if (!decoratedItemDTOOpt.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(decoratedItemDTOOpt.get());
    }

    @PostMapping(ApiEndPoint.DecoratedItem.DECORATED_ITEM_ENDPOINT)
    public ResponseEntity<DecoratedItemDTO> uploadMetaData(@RequestBody @Valid CreateDecoratedItemMetaDataRequest request) {
        DecoratedItemDTO dto = decoratedItemMapper.toDTO(request);
        dto = decoratedItemService.createNew(dto);
        return ResponseEntity.created(URI.create(dto.getUrl())).body(dto);
    }

    @PostMapping(ApiEndPoint.DecoratedItem.DECORATED_ITEM_ENDPOINT + "/{id}/content")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file, @PathVariable("id") String id) {
        String fileExtension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);
        String fileName = id + "." + fileExtension;
        fileStorageService.store(file, fileName);
        return ResponseEntity.accepted().build();
    }

    @GetMapping(ApiEndPoint.DecoratedItem.DECORATED_ITEM_ENDPOINT + "/{id}/content")
    public ResponseEntity<Resource> getFile(@PathVariable String id) {
        Resource file = fileStorageService.loadAsResource(id);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @PutMapping(ApiEndPoint.DecoratedItem.UPDATE)
    public ResponseEntity<?> update(@RequestBody UpdateDecoratedItemMetaDataRequest request) {
        try {
            DecoratedItemDTO dto = decoratedItemMapper.toDTO(request);
            decoratedItemService.update(dto);
            return GenericResponse.build(
                    MessageUtil.getMessage(MessageConstant.DecoratedItem.UPDATE_SUCCESSFULLY),
                    HttpStatus.OK);
        } catch (IllegalArgumentException ex) {
            return GenericResponse.build(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }




}
