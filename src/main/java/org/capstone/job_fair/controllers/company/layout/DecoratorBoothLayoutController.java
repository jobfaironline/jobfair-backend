package org.capstone.job_fair.controllers.company.layout;

import lombok.extern.slf4j.Slf4j;
import org.capstone.job_fair.config.jwt.details.UserDetailsImpl;
import org.capstone.job_fair.constants.AWSConstant;
import org.capstone.job_fair.constants.ApiEndPoint;
import org.capstone.job_fair.controllers.payload.responses.GenericResponse;
import org.capstone.job_fair.models.dtos.company.layout.DecoratorBoothLayoutDTO;
import org.capstone.job_fair.models.dtos.company.layout.DecoratorBoothLayoutVideoDTO;
import org.capstone.job_fair.services.interfaces.company.layout.DecoratorBoothLayoutService;
import org.capstone.job_fair.services.interfaces.util.FileStorageService;
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
public class DecoratorBoothLayoutController {

    @Autowired
    private DecoratorBoothLayoutService decoratorBoothLayoutService;

    @Autowired
    private FileStorageService fileStorageService;

    @GetMapping(ApiEndPoint.DecoratorBoothLayout.DECORATOR_BOOTH_LAYOUT)
    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).COMPANY_EMPLOYEE)")
    public ResponseEntity<?> getAllOfCompanyEmployee() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<DecoratorBoothLayoutDTO> result = decoratorBoothLayoutService.getLayoutsByCompanyEmployeeId(userDetails.getId());
        if (result.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping(ApiEndPoint.DecoratorBoothLayout.DECORATOR_BOOTH_LAYOUT + "/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") String id) {
        Optional<DecoratorBoothLayoutDTO> layoutOpt = decoratorBoothLayoutService.getById(id);
        if (!layoutOpt.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(layoutOpt.get());
    }


    @PostMapping(ApiEndPoint.DecoratorBoothLayout.DECORATOR_BOOTH_LAYOUT)
    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).COMPANY_EMPLOYEE)")
    public ResponseEntity<?> createNewLayout(@RequestParam("name") String name,
                                             @RequestPart("file") MultipartFile file) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        DecoratorBoothLayoutDTO dto = decoratorBoothLayoutService.create(userDetails.getId(), name, file);
        try {
            fileStorageService.store(file.getBytes(), AWSConstant.DECORATOR_LAYOUT_FOLDER + "/" + dto.getId());
        } catch (IOException e) {
            return GenericResponse.build(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.created(URI.create(dto.getUrl())).body(dto);
    }


    @DeleteMapping(ApiEndPoint.DecoratorBoothLayout.DECORATOR_BOOTH_LAYOUT + "/{id}")
    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).COMPANY_EMPLOYEE)")
    public ResponseEntity<?> deleteLayout(@PathVariable("id") String id) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        DecoratorBoothLayoutDTO dto = decoratorBoothLayoutService.delete(userDetails.getId(), id);
        return ResponseEntity.ok(dto);

    }


    @PostMapping(ApiEndPoint.DecoratorBoothLayout.VIDEO_LAYOUT_WITH_FILE)
    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).COMPANY_EMPLOYEE)")
    public ResponseEntity<?> createNewVideoForLayoutWithFile(
            @RequestParam("layoutId") String layoutId,
            @RequestParam("itemName") String itemName,
            @RequestPart("file") MultipartFile file) {
        DecoratorBoothLayoutVideoDTO dto = decoratorBoothLayoutService.createNewVideoWithFile(layoutId, itemName);
        try {
            fileStorageService.store(file.getBytes(), AWSConstant.DECORATOR_BOOTH_LAYOUT_VIDEO_FOLDER + "/" + dto.getId());
        } catch (IOException e) {
            return GenericResponse.build(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.created(URI.create(dto.getUrl())).body(dto);
    }

    @PostMapping(ApiEndPoint.DecoratorBoothLayout.VIDEO_LAYOUT_WITH_URL)
    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).COMPANY_EMPLOYEE)")
    public ResponseEntity<?> createNewVideoForLayoutWithUrl(@RequestParam("layoutId") String layoutId,
                                                            @RequestParam("itemName") String itemName,
                                                            @RequestParam("url") String url) {
        DecoratorBoothLayoutVideoDTO dto = decoratorBoothLayoutService.createNewVideoWithUrl(layoutId, itemName, url);
        return ResponseEntity.created(URI.create(dto.getUrl())).body(dto);
    }


}
