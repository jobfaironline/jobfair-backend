package org.capstone.job_fair.controllers.company;


import org.capstone.job_fair.constants.ApiEndPoint;
import org.capstone.job_fair.controllers.payload.requests.company.CreateProfessionalCategoryRequest;
import org.capstone.job_fair.models.dtos.company.ProfessionCategoryDTO;
import org.capstone.job_fair.services.interfaces.company.ProfessionalCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class ProfessionalCategoryController {

    @Autowired
    private ProfessionalCategoryService professionalCategoryService;


    @GetMapping(ApiEndPoint.ProfessionalCategory.PROFESSIONAL_CATEGORY_ENDPOINT + "/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") int id) {
        Optional<ProfessionCategoryDTO> dtoOptional = professionalCategoryService.findById(id);
        return dtoOptional.isPresent() ? ResponseEntity.ok(dtoOptional.get()) : ResponseEntity.notFound().build();
    }

    @GetMapping(ApiEndPoint.ProfessionalCategory.PROFESSIONAL_CATEGORY_ENDPOINT)
    public ResponseEntity<?> getAll() {
        List<ProfessionCategoryDTO> dtoList = professionalCategoryService.getAll();
        return dtoList.size() == 0 ? ResponseEntity.noContent().build() : ResponseEntity.ok(dtoList);
    }

    @PostMapping(ApiEndPoint.ProfessionalCategory.PROFESSIONAL_CATEGORY_ENDPOINT)
    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).ADMIN)")
    public ResponseEntity<?> create(@RequestBody @Validated CreateProfessionalCategoryRequest request) {
        ProfessionCategoryDTO dto = new ProfessionCategoryDTO(null, request.getName());
        dto = professionalCategoryService.create(dto);
        return ResponseEntity.ok(dto);
    }

    @PutMapping(ApiEndPoint.ProfessionalCategory.PROFESSIONAL_CATEGORY_ENDPOINT)
    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).ADMIN)")
    public ResponseEntity<?> update(@RequestBody @Validated ProfessionCategoryDTO request) {
        request = professionalCategoryService.update(request);
        return ResponseEntity.ok(request);
    }

    @DeleteMapping(ApiEndPoint.ProfessionalCategory.PROFESSIONAL_CATEGORY_ENDPOINT + "/{id}")
    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).ADMIN)")
    public ResponseEntity<?> delete(@PathVariable("id") int id) {
        ProfessionCategoryDTO dto = professionalCategoryService.delete(id);
        return ResponseEntity.ok(dto);
    }

}
