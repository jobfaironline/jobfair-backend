package org.capstone.job_fair.controllers.attendant.profile;

import org.capstone.job_fair.constants.ApiEndPoint;
import org.capstone.job_fair.models.dtos.attendant.JobLevelDTO;
import org.capstone.job_fair.models.dtos.attendant.profile.QualificationDTO;
import org.capstone.job_fair.services.interfaces.attendant.JobLevelService;
import org.capstone.job_fair.services.interfaces.attendant.profile.QualificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class QualificationController {
    @Autowired
    private QualificationService qualificationService;


    @GetMapping(ApiEndPoint.Qualification.PROFESSIONAL_CATEGORY_ENDPOINT + "/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") int id) {
        Optional<QualificationDTO> dtoOptional = qualificationService.findById(id);
        return dtoOptional.isPresent() ? ResponseEntity.ok(dtoOptional.get()) : ResponseEntity.noContent().build();
    }

    @GetMapping(ApiEndPoint.Qualification.PROFESSIONAL_CATEGORY_ENDPOINT)
    public ResponseEntity<?> getAll() {
        List<QualificationDTO> dtoList = qualificationService.getAll();
        return dtoList.size() == 0 ? ResponseEntity.noContent().build() : ResponseEntity.ok(dtoList);
    }

    @PutMapping(ApiEndPoint.Qualification.PROFESSIONAL_CATEGORY_ENDPOINT)
    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).ADMIN)")
    public ResponseEntity<?> create(@RequestBody @Validated QualificationDTO request) {
        QualificationDTO dto = new QualificationDTO(request.getId(), request.getName());
        dto = qualificationService.create(dto);
        return ResponseEntity.ok(dto);
    }

    @PostMapping(ApiEndPoint.Qualification.PROFESSIONAL_CATEGORY_ENDPOINT)
    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).ADMIN)")
    public ResponseEntity<?> update(@RequestBody @Validated QualificationDTO request) {
        request = qualificationService.update(request);
        return ResponseEntity.ok(request);
    }

    @DeleteMapping(ApiEndPoint.Qualification.PROFESSIONAL_CATEGORY_ENDPOINT + "/{id}")
    @PreAuthorize("hasAuthority(T(org.capstone.job_fair.models.enums.Role).ADMIN)")
    public ResponseEntity<?> delete(@PathVariable("id") int id) {
        QualificationDTO dto = qualificationService.delete(id);
        return ResponseEntity.ok(dto);
    }

}
